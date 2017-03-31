package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class NetherMetals extends ModBase {
	
	public NetherMetals(ModManager mod) {
		super(mod);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		for (String ore : oreList) {
			String oreName = "oreNether" + ore;
			ResourceLocation resource = new ResourceLocation(mod.getModId(), "nether_" + ore.toLowerCase() + "_ore");
			Block block = Block.REGISTRY.getObject(resource);
			if (block != null) {
				OreDictionary.registerOre(oreName, block);
			}
		}
	}

	private static List<String> oreList = Lists.newArrayList("Iron", "Gold", "Bismuth", "Copper", "Lead",
			"Mercury", "Nickel", "Platinum", "Silver", "Tin", "Zinc", "Aluminum", "Cadmium", "Chromium",
			"Iridium", "Magnesium", "Manganese", "Osmium", "Plutonium", "Rutile", "Tantalum", "Titanium",
			"Tungsten", "Uranium", "Zirconium");
}
