package com.selfeatingwatermelon.mekores.integration.mods;

import java.util.EnumSet;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class ExtremeReactors extends ModBase {

	public ExtremeReactors(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Yellorium, defaultStates);
	}

}
