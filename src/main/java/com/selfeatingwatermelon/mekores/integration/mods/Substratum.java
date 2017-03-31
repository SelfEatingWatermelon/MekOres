package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.EnumSet;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class Substratum extends ModBase {

	public Substratum(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Alumina, defaultStates);
		oreSpec.put(DefaultOres.Aluminum, defaultStates);
		oreSpec.put(DefaultOres.Chromium, defaultStates);
		oreSpec.put(DefaultOres.Nickel, defaultStates);
		oreSpec.put(DefaultOres.Platinum, defaultStates);
		oreSpec.put(DefaultOres.Zinc, defaultStates);
	}

}
