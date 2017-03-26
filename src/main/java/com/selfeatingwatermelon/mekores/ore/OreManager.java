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
import com.selfeatingwatermelon.mekores.item.IMetaItem;
import com.selfeatingwatermelon.mekores.item.ItemOre;

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
	
	public static void registerRecipes() {
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
	
	public static ImmutableList<ItemOre> getOreItemList() {
		return ImmutableList.copyOf(oreItemList);
	}
	
	public static ImmutableMap<String, ItemStack> getOreStackMap() {
		return ImmutableMap.copyOf(oreStackMap);
	}
	
}
