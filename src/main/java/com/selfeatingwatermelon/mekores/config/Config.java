package com.selfeatingwatermelon.mekores.config;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.MekOres;
import com.selfeatingwatermelon.mekores.integration.ModManager;
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
	public static Configuration configUser;
	
	public static final List<Section> sections;
	
	static {
		sections = new ArrayList<Section>();
	}
	
	public static final Section sectionMain = new Section("Main Configuration", "main");
	public static final Section sectionIntegration = new Section("Integration", "integration");
	
	public static boolean registerMekanismGases = true;
	public static boolean registerMekanismNetherRecipes = true;

	public static Set<String> ignoreMekanismOres = Sets.newHashSet("Iron", "Gold", "Osmium", "Copper", "Tin", "Silver", "Lead");

	private static HashMap<String, Ore> oreMap = new HashMap<String, Ore>();

	public static void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Config());
		
		boolean exists;
		
		File fileMain = new File(event.getModConfigurationDirectory(), "MekOres/Common.cfg");
		exists = fileMain.exists();
		configMain = new Configuration(fileMain);
		if (!exists) {
			Log.info("Created configuration file: %s", fileMain.getAbsoluteFile());
		}

		File fileOres = new File(event.getModConfigurationDirectory(), "MekOres/DefaultOres.cfg");
		exists = fileOres.exists();
		configOres = new Configuration(fileOres);
		if (!exists) {
			Log.info("Created configuration file: %s", fileOres.getAbsoluteFile());
		}

		File fileUser = new File(event.getModConfigurationDirectory(), "MekOres/UserOres.cfg");
		exists = fileUser.exists();
		configUser = new Configuration(fileUser);
		if (!exists) {
			Log.info("Created configuration file: %s", fileOres.getAbsoluteFile());
		}

		syncConfig();
	}
	
	public static void syncConfig() {
		try {
			Config.processMainConfig();
			Config.processOreConfig();
		} catch (Exception e) {
			Log.error(e, MekOres.NAME + " could not load configuration");
		} finally {
			if(configMain.hasChanged()) {
				configMain.save();
			}
			if (configOres.hasChanged()) {
				configOres.save();
			}
			if (configUser.hasChanged()) {
				configUser.save();
			}
		}
	}
	
	public static void processMainConfig() {
		registerMekanismGases = configMain.getBoolean("registerMekanismGases", sectionMain.name, registerMekanismGases, "Register Mekanism gases for 5x ore processing");
		registerMekanismNetherRecipes = configMain.getBoolean("registerMekanismNetherRecipes", sectionMain.name, registerMekanismNetherRecipes, "Register Mekanism recipes for Nether ore processing (additional 2x ore)");

		for (ModManager mod : ModManager.values()) {
			mod.setEnabled(configMain.getBoolean(mod.getModName(), sectionIntegration.name, true, "Enable support for " + mod.getModName()));
			if (mod.isAvailable()) {
				Log.info("%s support enabled", mod.getModName());
			}
		}
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
			configOres.getBoolean("enabled", ore.name(), true, "Enable support for this ore");
		}

	}

	private static void processOreConfig() {
		loadOreDefaults();

		oreMap.clear();

		// Process built-in ores
		Map<DefaultOres,Set<OreStates>> availableOres = ModManager.getOreMap();
		for (String category : configOres.getCategoryNames()) {
			configOres.setCategoryRequiresMcRestart(category, true);
			String oreName = configOres.getString("oreName", category, "", "The proper name of this ore");
			Color oreColor = Color.decode(configOres.getString("color", category, "0x" + Integer.toHexString(0xa0a0a0), "The color to apply to this ore"));
			boolean enabled = configOres.getBoolean("enabled", category, true, "Enable support for this ore");

			if (!enabled) {
				continue;
			}

			DefaultOres defOre = DefaultOres.getDefaultOre(oreName);
			if (defOre == null) {
				Log.warn("%s is not a default ore, removing from configuration...", oreName);
				configOres.removeCategory(configOres.getCategory(category));
				continue;
			}

			if (availableOres.containsKey(defOre)) {
				oreMap.put(oreName, new Ore(oreName, oreColor));
			}
		}

		// Process user-defined ores
		for (String category : configUser.getCategoryNames()) {
			configUser.setCategoryRequiresMcRestart(category, true);
			String oreName = configUser.getString("oreName", category, null, "The proper name of this ore");
			Color oreColor = Color.decode(configUser.getString("color", category, "0x" + Integer.toHexString(0x808080), "The color to apply to this ore"));
			boolean enabled = configUser.getBoolean("enabled", category, true, "Enable support for this ore");

			if (!enabled) {
				continue;
			}

			if (ignoreMekanismOres.contains(oreName)) {
				Log.warn("%s ore processing is provided by Mekanism, skipping...", oreName);
				continue;
			}

			DefaultOres defOre = DefaultOres.getDefaultOre(oreName);
			if (defOre != null) {
				if (oreMap.containsKey(oreName)) {
					Log.warn("%s is a built-in ore and is enabled, skipping...", oreName);
					continue;
				}
				Log.warn("%s is a built-in ore, consider reporting this so we can add formal mod support", oreName);
			}

			oreMap.put(oreName, new Ore(oreName, oreColor));
		}
	}
	
	public static ImmutableMap<String, Ore> getOreMap() {
		return ImmutableMap.copyOf(oreMap);
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
