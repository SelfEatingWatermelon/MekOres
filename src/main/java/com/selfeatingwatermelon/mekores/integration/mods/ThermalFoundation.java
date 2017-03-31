package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.EnumSet;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class ThermalFoundation extends ModBase {

	public ThermalFoundation(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Aluminum, defaultStates);
		oreSpec.put(DefaultOres.Iridium, defaultStates);
		oreSpec.put(DefaultOres.Mithril, defaultStates);
		oreSpec.put(DefaultOres.Nickel, defaultStates);
		oreSpec.put(DefaultOres.Platinum, defaultStates);
	}

}
