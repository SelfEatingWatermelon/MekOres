package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.EnumSet;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class TinkersConstruct extends ModBase {
	
	public TinkersConstruct(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Ardite, EnumSet.of(OreStates.DUST_DIRTY, OreStates.CLUMP, OreStates.SHARD, OreStates.CRYSTAL, OreStates.SLURRY, OreStates.SLURRY_CLEAN));
		oreSpec.put(DefaultOres.Cobalt, EnumSet.of(OreStates.DUST_DIRTY, OreStates.CLUMP, OreStates.SHARD, OreStates.CRYSTAL, OreStates.SLURRY, OreStates.SLURRY_CLEAN));
	}
	
}
