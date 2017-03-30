package com.selfeatingwatermelon.mekores.config;


public enum DefaultOres {
	Aluminum("0xdadae4"),
	Platinum("0xc6f2fa"),
	Nickel("0xedecbf"),
	Mithril("0x88ceff"),
	Iridium("0xb8bad7"),
	Cobalt("0x287ce1"),
	Ardite("0xe16802"),
	Yellorium("0xbde02e"),
	Uranium("0x607b38");

	private String color;
	
	private DefaultOres(String color)
	{
		this.color = color;
	}

	public String getColor()
	{
		return this.color;
	}
	
	public static DefaultOres getDefaultOre(String oreName) {
		for (DefaultOres ore : DefaultOres.values()) {
			if (oreName.equals(ore.name())) {
				return ore;
			}
		}
		return null;
	}
}
