package com.selfeatingwatermelon.mekores.integration.mods;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class SimpleOres extends ModBase {

	public SimpleOres(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Adamantium, defaultStates);
		oreSpec.put(DefaultOres.Mithril, defaultStates);
	}

}
