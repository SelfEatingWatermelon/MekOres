package com.selfeatingwatermelon.mekores.ore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.MekOres;
import com.selfeatingwatermelon.mekores.config.Config;
import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.gas.GasOre;
import com.selfeatingwatermelon.mekores.integration.Mekanism;
import com.selfeatingwatermelon.mekores.integration.ModManager;
import com.selfeatingwatermelon.mekores.item.IMetaItem;
import com.selfeatingwatermelon.mekores.item.ItemOre;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.OreGas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class OreManager {
	
	private static final ArrayList<ItemOre> oreItemList = new ArrayList<ItemOre>();
	private static final HashMap<String, ItemStack> oreStackMap = new HashMap<String, ItemStack>();
	private static final HashMap<String, GasOre> gasMap = new HashMap<String, GasOre>();
	private static final HashMap<String, ItemStack> oredictCache = new HashMap<String, ItemStack>();
	
	private static final String orePrefix = "ore";
	private static final String ingotPrefix = "ingot";
	private static final EnumSet<OreStates> mapAlternateStates = EnumSet.of(OreStates.ORE, OreStates.INGOT, OreStates.NUGGET, OreStates.DUST);

	public static void mapAlternateOreNames() {
		Map<DefaultOres,Set<OreStates>> availableOres = ModManager.getOreMap();

		for (DefaultOres defOre : availableOres.keySet()) {
			for (OreStates oreState : mapAlternateStates) {
				List<ItemStack> myEntries = OreDictionary.getOres(oreState.getOredictPrefix() + defOre.name());
				for (String altName : defOre.getAltOreNames()) {

					// Register my entries under the alternate name
					for (ItemStack stack : myEntries) {
						OreDictionary.registerOre(oreState.getOredictPrefix() + altName, stack);
					}

					// Register alternate entries under my name
					for (ItemStack stack : OreDictionary.getOres(oreState.getOredictPrefix() + altName)) {
						OreDictionary.registerOre(oreState.getOredictPrefix() + defOre.name(), stack);
					}
				}
			}
		}
	}

	public static void createConfiguredItems() {
		for (Ore ore : Config.getOreMap().values()) {
			ItemOre item = new ItemOre(ore);
			try {
				// Items
				GameRegistry.register(item);
				oreItemList.add(item);
				for(int i = 0; i < item.getVariants(); i++) {
					ItemStack stack = new ItemStack(item, 1, i);
					String name = item.getOredictNameForStack(stack);
					oreStackMap.put(name, stack);
					OreDictionary.registerOre(name, stack);
				}
			} catch (Exception e) {
				Log.error(e, "Could not register item: ", item.getRegistryName());
			}
		}
	}
	
	public static void registerItemModels() {
		for (ItemOre item : oreItemList) {
			for(int i = 0; i < item.getVariants(); i++)
			{
				String texture = item.getTexture(i);
				if(texture == null)
				{
					continue;
				}

				try {
					ModelResourceLocation loc = new ModelResourceLocation(MekOres.MODID + ":" + texture, "inventory");
					ModelLoader.setCustomModelResourceLocation(item, i, loc);
					ModelBakery.registerItemVariants(item, new ResourceLocation(MekOres.MODID + ":" + texture));
				} catch (Exception e) {
					Log.error(e, "Could not register item model: ", item.getRegistryName());
				}
			}
		}
	}
	
	public static ItemStack findOreStack(String oreName, int amount) throws Exception {
		ItemStack stack;
		try {
			if (oreStackMap.containsKey(oreName)) {
				stack = oreStackMap.get(oreName).copy();
			} else {
				stack = ItemStack.copyItemStack(oredictCache.get(oreName));
				if (stack == null) {
					stack = ItemStack.copyItemStack(OreDictionary.getOres(oreName).get(0));
					oredictCache.put(oreName, stack.copy());
				}
			}
			stack.stackSize = amount;
		} catch (Exception e) {
			throw new Exception(oreName + " not found");
		}
		return stack;
	}
	
	public static ItemStack findOreStack(String oreName) throws Exception {
		return findOreStack(oreName, 1);
	}
	
	public static ItemStack findOreStack(String prefix, Ore ore, int amount) throws Exception {
		return findOreStack(prefix + ore.getOreName(), amount);
	}
	
	public static ItemStack findOreStack(String prefix, Ore ore) throws Exception {
		return findOreStack(prefix + ore.getOreName(), 1);
	}
	
	public static void registerVanillaRecipes() {
		oreItemList.forEach(item -> {
			Ore ore = item.getOre();

			try {
				GameRegistry.addSmelting(findOreStack("dust", ore), findOreStack("ingot", ore), 0.2F);
			} catch (Exception e) {
				Log.warn("Could not add Vanilla recipes for %s: %s", ore.getOreName(), e.getMessage());
				return;
			}
		});
	}
	
	public static void registerMekanismRecipes() {
		// Lookup some auxiliary inputs
		Gas oxygen = GasRegistry.getGas("oxygen");
		Gas hydrogenChloride = GasRegistry.getGas("hydrogenChloride");

		oreItemList.forEach(item -> {
			Ore ore = item.getOre();

			// Load the list of compatible ores from the dictionary
			List<ItemStack> oreDictEntries = OreDictionary.getOres("ore" + ore.getOreName());

			for (ItemStack stack : oreDictEntries) {
				try {
					Mekanism.addEnrichmentChamberRecipe(stack, findOreStack("dust", ore, 2));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Enrichment Chamber ore recipe for %s: %s", ore.getOreName(), e.getMessage());
				}
			}

			try {
				Mekanism.addEnrichmentChamberRecipe(findOreStack("dustDirty", ore), findOreStack("dust", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Enrichment Chamber dirty dust recipe for %s: %s", ore.getOreName(), e.getMessage());
			}

			try {
				Mekanism.addCrusherRecipe(findOreStack("clump", ore), findOreStack("dustDirty", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Crusher clump recipe for %s: %s", ore.getOreName(), e.getMessage());
			}

			for (ItemStack stack : oreDictEntries) {
				try {
					Mekanism.addPurificationChamberRecipe(stack, oxygen, findOreStack("clump", ore, 3));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Purification Chamber ore recipe for %s: %s", ore.getOreName(), e.getMessage());
				}
			}

			try {
				Mekanism.addPurificationChamberRecipe(findOreStack("shard", ore), oxygen, findOreStack("clump", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Purification Chamber shard recipe for %s: %s", ore.getOreName(), e.getMessage());
			}

			for (ItemStack stack : oreDictEntries) {
				try {
					Mekanism.addChemicalInjectionChamberRecipe(stack, hydrogenChloride, findOreStack("shard", ore, 4));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Chemical Injection Chamber ore recipe for %s: %s", ore.getOreName(), e.getMessage());
				}
			}

			try {
				Mekanism.addChemicalInjectionChamberRecipe(findOreStack("crystal", ore), hydrogenChloride, findOreStack("shard", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Chemical Injection Chamber crystal recipe for %s: %s", ore.getOreName(), e.getMessage());
			}

			if (Config.registerMekanismGases) {
				// Clean gas
				GasOre cleanGas = new GasOre(ore, OreStates.SLURRY_CLEAN);
				GasRegistry.register(cleanGas);
				gasMap.put(cleanGas.getName(), cleanGas);

				// Dirty Gas
				GasOre dirtyGas = new GasOre(ore, OreStates.SLURRY, cleanGas);
				GasRegistry.register(dirtyGas);
				gasMap.put(dirtyGas.getName(), dirtyGas);

				for (ItemStack stack : oreDictEntries) {
					Mekanism.addChemicalDissolutionChamberRecipe(stack, new GasStack(dirtyGas, 1000));
				}

				Mekanism.addChemicalWasherRecipe(new GasStack(dirtyGas, 1), new GasStack(cleanGas, 1));

				try {
					Mekanism.addChemicalCrystallizerRecipe(new GasStack(cleanGas, 200), findOreStack("crystal", ore));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Chemical Crystallizer clean slurry recipe for %s: %s", ore.getOreName(), e.getMessage());
				}
			}

			Log.info("Added processing chain for %s", ore.getOreName());
		});

		if (Config.registerMekanismNetherRecipes) {
			Set<String> checkNames = Sets.newHashSet(Config.ignoreMekanismOres);
			oreItemList.forEach(item -> {
				checkNames.add(item.getOre().getOreName());
			});

			checkNames.forEach(oreName -> {
				// Load the list of compatible ores from the dictionary
				List<ItemStack> oreDictEntries = OreDictionary.getOres("oreNether" + oreName);
				if (oreDictEntries.size() == 0) {
					return;
				}

				for (ItemStack stack : oreDictEntries) {
					try {
						Mekanism.addEnrichmentChamberRecipe(stack, findOreStack("dust" + oreName, 4));
					} catch (Exception e) {
						Log.warn("Could not add Mekanism Enrichment Chamber nether ore recipe for %s: %s", oreName, e.getMessage());
					}
				}

				for (ItemStack stack : oreDictEntries) {
					try {
						Mekanism.addPurificationChamberRecipe(stack, oxygen, findOreStack("clump" + oreName, 6));
					} catch (Exception e) {
						Log.warn("Could not add Mekanism Purification Chamber nether ore recipe for %s: %s", oreName, e.getMessage());
					}
				}

				for (ItemStack stack : oreDictEntries) {
					try {
						Mekanism.addChemicalInjectionChamberRecipe(stack, hydrogenChloride, findOreStack("shard" + oreName, 8));
					} catch (Exception e) {
						Log.warn("Could not add Mekanism Chemical Injection Chamber nether ore recipe for %s: %s", oreName, e.getMessage());
					}
				}

				if (Config.registerMekanismGases) {
					// Dirty Gas
					Gas dirtyGas = GasRegistry.containsGas("dirty" + oreName) ? GasRegistry.getGas("dirty" + oreName) : GasRegistry.getGas(oreName.toLowerCase()); 

					if (dirtyGas != null) {
						for (ItemStack stack : oreDictEntries) {
							Mekanism.addChemicalDissolutionChamberRecipe(stack, new GasStack(dirtyGas, 2000));
						}
					}
				}

				Log.info("Added processing chain for Nether %s", oreName);
			});
		}
	}

	public static ImmutableList<ItemOre> getOreItemList() {
		return ImmutableList.copyOf(oreItemList);
	}
	
	public static ImmutableMap<String, ItemStack> findOreStackMap() {
		return ImmutableMap.copyOf(oreStackMap);
	}
	
}
