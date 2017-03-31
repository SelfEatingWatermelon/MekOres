package com.selfeatingwatermelon.mekores.integration;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class ModBase {
	
	protected static final Set<OreStates> defaultStates = ImmutableSet.of(OreStates.DUST, OreStates.DUST_DIRTY, OreStates.CLUMP, OreStates.SHARD, OreStates.CRYSTAL, OreStates.SLURRY, OreStates.SLURRY_CLEAN);
	protected static final Map<DefaultOres,Set<OreStates>> oreSpec = new HashMap<DefaultOres,Set<OreStates>>();
	
	public final ModManager mod;
	
	public ModBase(ModManager mod) {
		this.mod = mod;
	}

	public void preInit(FMLPreInitializationEvent event) {}
	public void init(FMLInitializationEvent event) {}
	public void postInit(FMLPostInitializationEvent event) {}

	public static ImmutableMap<DefaultOres,Set<OreStates>> getOreSpec() {
		return ImmutableMap.copyOf(oreSpec);
	}
}
