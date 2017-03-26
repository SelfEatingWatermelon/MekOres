package com.selfeatingwatermelon.mekores.config;


public enum DefaultOres {
	Aluminum("0xe8e8f3", 1),
	Platinum("0xd3ffff", 1),
	Nickel("0xfcfccd", 1),
	Mithril("0x7ecbf7", 1),
	Iridium("0xedeaf1", 1),
	Cobalt("0x2376dd", 3),
	Ardite("0xb63b17", 3),
	Yellorium("0xb3e22b", 1);

	private String color;
	private int energyCost;
	
	private DefaultOres(String color, int energyCost)
	{
		this.color = color;
		this.energyCost = energyCost;
	}

	public String getColor()
	{
		return this.color;
	}
	
	public int getEnergyCost()
	{
		return this.energyCost;
	}
}
