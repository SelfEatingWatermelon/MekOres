package com.selfeatingwatermelon.mekores.integration;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.Loader;

import com.google.common.collect.Lists;
import com.selfeatingwatermelon.mekores.config.DefaultOres;

public enum ModSupport {
	EXTREME_REACTORS("bigreactors", "Extreme Reactors", DefaultOres.Yellorium),
	INDUSTRIALCRAFT2("IC2", "IndustrialCraft 2", DefaultOres.Uranium),
	THERMAL_FOUNDATION("thermalfoundation", "Thermal Foundation", DefaultOres.Aluminum, DefaultOres.Platinum, DefaultOres.Nickel, DefaultOres.Mithril, DefaultOres.Iridium),
	TINKERS_CONSTRUCT("tconstruct", "Tinkers Construct", DefaultOres.Cobalt, DefaultOres.Ardite);

	final String modId;
	final String modName;
	final ArrayList<DefaultOres> oreList;
	boolean enabled = true;
	
	ModSupport(String modId, String modName, DefaultOres... oreList) {
		this.modId = modId;
		this.modName = modName;
		this.oreList = Lists.newArrayList(oreList);
	}
	
	public String getModId() {
		return modId;
	}
	
	public String getModName() {
		return modName;
	}
	
	public ArrayList<DefaultOres> getOreList() {
		return oreList;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isAvailable() {
		return enabled && Loader.isModLoaded(modId);
	}
}
