package com.selfeatingwatermelon.mekores.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public enum DefaultOres {
	Adamantium("0x52a85b", "Adamantine", "Adamantite"),
	Alumina("0xc7c5cc"),
	Aluminum("0xdadae4", "Aluminium"),
	Arcanite("0x292358"),
	Ardite("0xe16802"),
	Bismuth("0xeaa798"),
	Cadmium("0xd6dadc"),
	Chromium("0x7a7a88", "Chrome"),
	Cobalt("0x287ce1"),
	Coldiron("0xb9c0e0"),
	Eternite("0xfcf458"),
	Galvanizedsteel("0xc4c8c7"),
	Ignitite("0xed6a47"),
	Iridium("0xb8bad7"),
	Karmesine("0xde7347"),
	Magnesium("0xbbbbb7"),
	Manganese("0xe8d9dd"),
	Mercury("0xd3d3d3"),
	Meteorite("0x232224"),
	Mindorite("0x78d5ca"),
	Mithril("0x88ceff", "Mythril"),
	Nichrome("0xdfc6a8"),
	Nickel("0xedecbf"),
	Palladium("0xe57825"),
	Platinum("0xc6f2fa"),
	Plutonium("0xcc98e2"),
	Prometheum("0x1a1b1b"),
	Rubium("0xc28689"),
	Rutile("0xd4c2bf"),
	Stainlesssteel("0xd3d1d2"),
	Starsteel("0x070909"),
	Tantalum("0xd6d3d5"),
	Tiberium("0x78a513"),
	Titanite("0xced4d7"),
	Titanium("0xb5b7b9"),
	Tungsten("0xb4b6b8"),
	Uranium("0x607b38"),
	Vibranium("0xdeeadf"),
	Violium("0x5fa299"),
	Yellorium("0xbde02e"),
	Zinc("0xc7c5cc"),
	Zirconium("0xbcbebc");

	private final String color;
	private final List<String> altOreNames;

	private DefaultOres(String color, String... altOreNames)
	{
		this.color = color;
		this.altOreNames = Lists.newArrayList(altOreNames);
	}

	public String getColor()
	{
		return this.color;
	}
	
	public ImmutableList<String> getAltOreNames() {
		return ImmutableList.copyOf(altOreNames);
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
