package com.selfeatingwatermelon.mekores.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public enum OreStates {
	ORE("ore", false, false),
	INGOT("ingot", false, false),
	NUGGET("nugget", true, false),
	DUST("dust", true, false),
	DUST_DIRTY("dustDirty", true, false),
	CLUMP("clump", true, false),
	SHARD("shard", true, false),
	CRYSTAL("crystal", true, false),
	SLURRY_CLEAN("cleanSlurry", true, true),
	SLURRY("slurry", true, true);

	private String prefix;
	private boolean register;
	private boolean gaseous;
	
	private OreStates(String oredict, boolean register, boolean gaseous)
	{
		this.prefix = oredict;
		this.register = register;
		this.gaseous = gaseous;
	}

	public String getOredictPrefix()
	{
		return prefix;
	}
	
	public boolean getShouldRegister()
	{
		return register;
	}
	
	public boolean isGaseous()
	{
		return gaseous;
	}

	public static OreStates getFromOredictPrefix(String s)
	{
		for(OreStates r : values())
		{
			if(r.prefix.toLowerCase().equals(s.toLowerCase()))
			{
				return r;
			}
		}

		return null;
	}
	
	public static ImmutableList<OreStates> getItemStatesToRegister() {
		ArrayList<OreStates> states = new ArrayList<OreStates>();
		for(OreStates r : values()) {
			if (r.getShouldRegister() && !r.isGaseous()) {
				states.add(r);
			}
		}
		return ImmutableList.copyOf(states);
	}

	public static ImmutableList<OreStates> getGasStatesToRegister() {
		ArrayList<OreStates> states = new ArrayList<OreStates>();
		for(OreStates r : values()) {
			if (r.getShouldRegister() && r.isGaseous()) {
				states.add(r);
			}
		}
		return ImmutableList.copyOf(states);
	}

}
