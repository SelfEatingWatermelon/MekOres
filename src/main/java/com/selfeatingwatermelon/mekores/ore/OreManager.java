package com.selfeatingwatermelon.mekores.ore;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.MekOres;
import com.selfeatingwatermelon.mekores.config.Config;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.gas.GasOre;
import com.selfeatingwatermelon.mekores.integration.Mekanism;
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
	
	public static Collection<String> itentifyPotentialOres()
	{
		Set<String> ores = new LinkedHashSet<String>();
		for (String name : OreDictionary.getOreNames())
		{
			if (name.startsWith(OreStates.ORE.getOredictPrefix()) && !OreDictionary.getOres(name).isEmpty())
			{
				String oreName = name.substring(OreStates.ORE.getOredictPrefix().length());
				for (String n : OreDictionary.getOreNames())
				{
					if (n.equals(OreStates.INGOT.getOredictPrefix() + oreName) && !OreDictionary.getOres(n).isEmpty())
					{
						Log.info("Ore Scanner: %s looks interesting...", oreName);
						ores.add(oreName);
					}
				}
			}
		}

		return Collections.unmodifiableSet(ores);
	}
	
	public static void createConfiguredItems() {
		for (Ore ore : Config.getOreList()) {
			ItemOre item = new ItemOre(ore);
			try {
				// Items
				GameRegistry.register(item);
				oreItemList.add(item);
				for(int i = 0; i < item.getVariants(); i++) {
					ItemStack stack = new ItemStack(item, 1, i);
					String oredictName = item.getOredictNameForStack(stack);
					oreStackMap.put(oredictName, stack);
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
	
	public static void registerOredictEntries() {
		oreStackMap.forEach((name, stack) -> {
			try {
				OreDictionary.registerOre(name, stack);
			} catch (Exception e) {
				Log.error(e, "Could not register ore dictionary mapping: %s => %s", name, stack.getDisplayName());
			}
		});
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
		} catch (NullPointerException e) {
			throw new NullPointerException("Ore dictionary lookup failed: " + oreName);
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
		registerSmeltingRecipes();
	}
	
	public static void registerSmeltingRecipes() {
		oreItemList.forEach(item -> {
			Ore ore = item.getOre();
			try {
				GameRegistry.addSmelting(findOreStack("dust", ore), findOreStack("ingot", ore), 0.2F);
			} catch (Exception e) {
				Log.warn("Could not add vanilla dust smelting recipe for %s", ore.getOreName());
			}
		});
	}
	
	public static void registerMekanismRecipes() {
		// Lookup some auxiliary inputs
		Gas oxygen = GasRegistry.getGas("oxygen");
		Gas hydrogenChloride = GasRegistry.getGas("hydrogenChloride");

		oreItemList.forEach(item -> {
			Ore ore = item.getOre();

			for (ItemStack stack : OreDictionary.getOres("ore" + ore.getOreName())) {
				try {
					Mekanism.addEnrichmentChamberRecipe(stack, findOreStack("dust", ore, 2));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Enrichment Chamber ore recipe for %s", ore.getOreName());
				}
			}

			try {
				Mekanism.addEnrichmentChamberRecipe(findOreStack("dustDirty", ore), findOreStack("dust", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Enrichment Chamber dirty dust recipe for %s", ore.getOreName());
			}

			try {
				Mekanism.addCrusherRecipe(findOreStack("clump", ore), findOreStack("dustDirty", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Crusher clump recipe for %s", ore.getOreName());
			}

			for (ItemStack stack : OreDictionary.getOres("ore" + ore.getOreName())) {
				try {
					Mekanism.addPurificationChamberRecipe(stack, oxygen, findOreStack("clump", ore, 3));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Purification Chamber ore recipe for %s", ore.getOreName());
				}
			}

			try {
				Mekanism.addPurificationChamberRecipe(findOreStack("shard", ore), oxygen, findOreStack("clump", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Purification Chamber shard recipe for %s", ore.getOreName());
			}

			for (ItemStack stack : OreDictionary.getOres("ore" + ore.getOreName())) {
				try {
					Mekanism.addChemicalInjectionChamberRecipe(stack, hydrogenChloride, findOreStack("shard", ore, 4));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Chemical Injection Chamber ore recipe for %s", ore.getOreName());
				}
			}

			try {
				Mekanism.addChemicalInjectionChamberRecipe(findOreStack("crystal", ore), hydrogenChloride, findOreStack("shard", ore));
			} catch (Exception e) {
				Log.warn("Could not add Mekanism Chemical Injection Chamber crystal recipe for %s", ore.getOreName());
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

				for (ItemStack stack : OreDictionary.getOres("ore" + ore.getOreName())) {
					try {
						Mekanism.addChemicalDissolutionChamberRecipe(stack, new GasStack(dirtyGas, 1000));
					} catch (Exception e) {
						Log.warn("Could not add Mekanism Chemical Dissolution Chamber ore recipe for %s", ore.getOreName());
					}
				}

				try {
					Mekanism.addChemicalWasherRecipe(new GasStack(dirtyGas, 1), new GasStack(cleanGas, 1));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Chemical Washer slurry recipe for %s", ore.getOreName());
				}

				try {
					Mekanism.addChemicalCrystallizerRecipe(new GasStack(cleanGas, 200), findOreStack("crystal", ore));
				} catch (Exception e) {
					Log.warn("Could not add Mekanism Chemical Crystallizer clean slurry recipe for %s", ore.getOreName());
				}
			}

		});
	}

	public static ImmutableList<ItemOre> getOreItemList() {
		return ImmutableList.copyOf(oreItemList);
	}
	
	public static ImmutableMap<String, ItemStack> findOreStackMap() {
		return ImmutableMap.copyOf(oreStackMap);
	}
	
}
