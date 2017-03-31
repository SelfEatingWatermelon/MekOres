package com.selfeatingwatermelon.mekores.integration.mods;

import com.selfeatingwatermelon.mekores.config.DefaultOres;
import com.selfeatingwatermelon.mekores.integration.ModBase;
import com.selfeatingwatermelon.mekores.integration.ModManager;

public class ModernMetals extends ModBase {

	public ModernMetals(ModManager mod) {
		super(mod);
	}

	static {
		oreSpec.put(DefaultOres.Aluminum, defaultStates);
		oreSpec.put(DefaultOres.Cadmium, defaultStates);
		oreSpec.put(DefaultOres.Chromium, defaultStates);
		oreSpec.put(DefaultOres.Galvanizedsteel, defaultStates);
		oreSpec.put(DefaultOres.Iridium, defaultStates);
		oreSpec.put(DefaultOres.Magnesium, defaultStates);
		oreSpec.put(DefaultOres.Manganese, defaultStates);
		oreSpec.put(DefaultOres.Nichrome, defaultStates);
		oreSpec.put(DefaultOres.Plutonium, defaultStates);
		oreSpec.put(DefaultOres.Rutile, defaultStates);
		oreSpec.put(DefaultOres.Stainlesssteel, defaultStates);
		oreSpec.put(DefaultOres.Tantalum, defaultStates);
		oreSpec.put(DefaultOres.Titanium, defaultStates);
		oreSpec.put(DefaultOres.Tungsten, defaultStates);
		oreSpec.put(DefaultOres.Uranium, defaultStates);
		oreSpec.put(DefaultOres.Zirconium, defaultStates);
	}

}
