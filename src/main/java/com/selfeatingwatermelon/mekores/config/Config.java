package com.selfeatingwatermelon.mekores.config;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.MekOres;
import com.selfeatingwatermelon.mekores.item.ItemOre;
import com.selfeatingwatermelon.mekores.ore.Ore;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public final class Config {
	public static Configuration configMain;
	public static Configuration configOres;
	
	public static final List<Section> sections;
	
	static {
		sections = new ArrayList<Section>();
	}
	
	public static final Section sectionMain = new Section("Main Configuration", "main");
	
	public static boolean registerMekanismGases = true;
	public static boolean registerMekanismRecipes = true;
	public static boolean registerTinkersMoltenMetals = true;
	public static boolean registerTinkersRecipes = true;

	private static Set<Ore> oreList = new HashSet<Ore>();

	public static void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Config());
		
		boolean exists;
		
		File fileMain = new File(event.getModConfigurationDirectory(), "MekOres/common.cfg");
		exists = fileMain.exists();
		configMain = new Configuration(fileMain);
		if (!exists) {
			Log.info("Created config file: %s", fileMain.getAbsoluteFile());
		}

		File fileOres = new File(event.getModConfigurationDirectory(), "MekOres/ores.cfg");
		exists = fileOres.exists();
		configOres = new Configuration(fileOres);
		if (!exists) {
			Log.info("Created config file: %s", fileOres.getAbsoluteFile());
			loadOreDefaults();
		}

		syncConfig();
	}
	
	public static void syncConfig() {
		try {
			Config.processMainConfig();
			Config.processOreConfig();
		} catch (Exception e) {
			Log.error(MekOres.NAME + " could not load configuration");
			e.printStackTrace();
		} finally {
			if(configMain.hasChanged()) {
				configMain.save();
			}
			if (configOres.hasChanged()) {
				configOres.save();
			}
		}
	}
	
	public static void processMainConfig() {
		registerMekanismGases = configMain.getBoolean("registerMekanismGases", sectionMain.name, registerMekanismGases, "Register Mekanism gases");
		registerMekanismRecipes = configMain.getBoolean("registerMekanismRecipes", sectionMain.name, registerMekanismRecipes, "Register Mekanism recipes"); 
		registerTinkersMoltenMetals =  configMain.getBoolean("registerTinkersMoltenMetals", sectionMain.name, registerTinkersMoltenMetals, "Register Tinkers Construct molten metals");
		registerTinkersRecipes =  configMain.getBoolean("registerTinkersRecipes", sectionMain.name, registerTinkersRecipes, "Register Tinkers Construct molten metals");
	}

	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(MekOres.MODID)) {
			Log.info("Updating config...");
			syncConfig();
		}
	}

	private static void loadOreDefaults() {
		// Each configuration category is an ore
		for (DefaultOres ore : DefaultOres.values()) {
			configOres.setCategoryRequiresMcRestart(ore.name(), true);
			configOres.getString("oreName", ore.name(), ore.name(), "The proper name of this ore");
			configOres.getString("color", ore.name(), ore.getColor(), "The color to apply to this ore");
			configOres.getInt("energyCost", ore.name(), ore.getEnergyCost(), 1, 6, "The energy cost factor for processing this ore");
		}

	}

	private static void processOreConfig() {
		// Each configuration category is an ore
		oreList.clear();
		for (String category : configOres.getCategoryNames()) {
			configOres.setCategoryRequiresMcRestart(category, true);
			String oreName = configOres.getString("oreName", category, null, "The proper name of this ore");
			Color oreColor = Color.decode(configOres.getString("color", category, "0x" + Integer.toHexString(0x808080), "The color to apply to this ore"));
			int energyCost = configOres.getInt("energyCost", category, 1, 1, 6, "The energy cost factor for processing this ore");
			oreList.add(new Ore(oreName, oreColor, energyCost));
		}
	}
	
	public static ImmutableSet<Ore> getOreList() {
		return ImmutableSet.copyOf(oreList);
	}
	
	public static class Section {
		public final String name;
		public final String lang;

		public Section(String name, String lang) {
			this.name = name;
			this.lang = lang;
			register();
		}

		private void register() {
			sections.add(this);
		}
	}
}
