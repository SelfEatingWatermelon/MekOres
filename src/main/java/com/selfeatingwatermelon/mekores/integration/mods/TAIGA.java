package com.selfeatingwatermelon.mekores.integration.mods;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class TAIGA extends ModBase {

	public TAIGA(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Arcanite, defaultStates);
		oreSpec.put(DefaultOres.Bismuth, defaultStates);
		oreSpec.put(DefaultOres.Eternite, defaultStates);
		oreSpec.put(DefaultOres.Ignitite, defaultStates);
		oreSpec.put(DefaultOres.Karmesine, defaultStates);
		oreSpec.put(DefaultOres.Meteorite, defaultStates);
		oreSpec.put(DefaultOres.Mindorite, defaultStates);
		oreSpec.put(DefaultOres.Mithril, defaultStates);
		oreSpec.put(DefaultOres.Palladium, defaultStates);
		oreSpec.put(DefaultOres.Prometheum, defaultStates);
		oreSpec.put(DefaultOres.Rubium, defaultStates);
		oreSpec.put(DefaultOres.Tiberium, defaultStates);
		oreSpec.put(DefaultOres.Titanite, defaultStates);
		oreSpec.put(DefaultOres.Vibranium, defaultStates);
		oreSpec.put(DefaultOres.Violium, defaultStates);
	}

}
