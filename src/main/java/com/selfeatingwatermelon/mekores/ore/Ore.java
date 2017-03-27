package com.selfeatingwatermelon.mekores.ore;

import java.awt.Color;

import com.selfeatingwatermelon.mekores.item.ItemOre;

import net.minecraft.nbt.NBTTagCompound;

public class Ore {
	
	private final String oreName;
	private final String oreKey;
	private final Color oreColor;
	private final int energyCost;
	
	public Ore (String oreName, Color oreColor, int energyCost) {
		this.oreName = oreName;
		this.oreKey = oreName.toLowerCase();
		this.oreColor = oreColor;
		this.energyCost = energyCost;
	}
	
	public static Ore createOre(String oreName, Color oreColor, int energyCost) {
		return new Ore(oreName, oreColor, energyCost);
	}
	
	public String getOreName() {
		return oreName;
	}
	
	public String getOreKey() {
		return oreKey;
	}
	
	public Color getOreColor() {
		return oreColor;
	}
	
	public String getOreColorHex() {
		return "0x" + Integer.toHexString(oreColor.getRGB() & 0x00ffffff);
	}
	
	public int getEnergyCost() {
		return energyCost;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Ore && ((Ore) obj).getOreKey().equals(oreKey);
	}

	@Override
	public int hashCode() {
		return oreKey.hashCode();
	}

}
