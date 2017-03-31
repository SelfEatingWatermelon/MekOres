package com.selfeatingwatermelon.mekores.integration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.google.common.collect.Lists;
import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.mods.*;

public enum ModManager {
	BASE_METALS("basemetals", "Base Metals", BaseMetals.class),
	EXTREME_REACTORS("bigreactors", "Extreme Reactors", ExtremeReactors.class),
	INDUSTRIALCRAFT2("IC2", "IndustrialCraft 2", IndustrialCraft2.class),
	MODERN_METALS("modernmetals", "Modern Metals", ModernMetals.class),
	NETHERCORE("nethercore", "Nether Core", NetherCore.class),
	NETHER_METALS("nethermetals", "Nether Metals", NetherMetals.class),
	THERMAL_FOUNDATION("thermalfoundation", "Thermal Foundation", ThermalFoundation.class),
	TINKERS_CONSTRUCT("tconstruct", "Tinkers Construct", TinkersConstruct.class),
	TAIGA("taiga", "TAIGA", TAIGA.class),
	SIMPLE_ORES("simpleores", "Simple Ores", SimpleOres.class),
	SUBSTRATUM("substratum", "Substratum", Substratum.class);

	private final String modId;
	private final String modName;
	private final Class<? extends ModBase> modHelper;

	boolean enabled = true;

	private static final Map<ModManager,ModBase> modMap = new HashMap<ModManager,ModBase>();

	ModManager(String modId, String modName, Class<? extends ModBase> modHelper) {
		this.modId = modId;
		this.modName = modName;
		this.modHelper = modHelper;
	}
	
	public String getModId() {
		return modId;
	}
	
	public String getModName() {
		return modName;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAvailable() {
		return modHelper != null && Loader.isModLoaded(modId) && enabled;
	}

	public static void initializeModSupport() {
		for (ModManager mod : ModManager.values()) {
			if (mod.isAvailable()) {
				try {
					Constructor ctor = mod.modHelper.getDeclaredConstructor(ModManager.class);
					ModBase modBase = (ModBase)ctor.newInstance(mod);
					modMap.put(mod, modBase);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static void preInit(FMLPreInitializationEvent event) {
		for (ModManager mod : modMap.keySet()) {
			if (mod.isAvailable()) {
				modMap.get(mod).preInit(event);
			}
		}
	}

	public static void init(FMLInitializationEvent event) {
		for (ModManager mod : modMap.keySet()) {
			if (mod.isAvailable()) {
				modMap.get(mod).init(event);
			}
		}
	}

	public static void postInit(FMLPostInitializationEvent event) {
		for (ModManager mod : modMap.keySet()) {
			if (mod.isAvailable()) {
				modMap.get(mod).postInit(event);
			}
		}
	}

	public static Map<DefaultOres,Set<OreStates>> getOreMap() {
		Map<DefaultOres,Set<OreStates>> oreMap = new HashMap<DefaultOres,Set<OreStates>>();

		for (ModManager mod : modMap.keySet()) {
			if (mod.isAvailable()) {
				Map<DefaultOres,Set<OreStates>> oreSpec = modMap.get(mod).getOreSpec();
				oreSpec.forEach((ore, states) -> {
					if (oreMap.containsKey(ore)) {
						EnumSet<OreStates> newStates = EnumSet.copyOf(oreMap.get(ore));
						newStates.addAll(states);
						oreMap.put(ore, newStates);
					} else {
						oreMap.put(ore, states);
					}
				});
			}
		}

		return oreMap;
	}

}
