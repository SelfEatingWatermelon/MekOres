package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.EnumSet;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class BaseMetals extends ModBase {
	
	public BaseMetals(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Adamantium, defaultStates);
		oreSpec.put(DefaultOres.Coldiron, defaultStates);
		oreSpec.put(DefaultOres.Mercury, defaultStates);
		oreSpec.put(DefaultOres.Mithril, defaultStates);
		oreSpec.put(DefaultOres.Nickel, defaultStates);
		oreSpec.put(DefaultOres.Platinum, defaultStates);
		oreSpec.put(DefaultOres.Starsteel, defaultStates);
		oreSpec.put(DefaultOres.Zinc, defaultStates);
	}

}
