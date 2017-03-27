package com.selfeatingwatermelon.mekores.gas;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.ore.Ore;

import mekanism.api.gas.OreGas;

public class GasOre extends OreGas {
	
	private final Ore ore;
	private final OreStates state;

	public GasOre(Ore ore, OreStates state, GasOre cleanGas) {
		super(state.getOredictPrefix() + ore.getOreName(), "gas." + state.getOredictPrefix() + ore.getOreName() + ".name");
		this.ore = ore;
		this.state = state;
		this.setCleanGas(cleanGas);
	}
	
	public GasOre(Ore ore, OreStates state) {
		this(ore, state, null);
	}

	public String getGenericUnlocalizedName() {
		return "gas." + state.getOredictPrefix() + "Generic.name";
	}

	@Override
	public String getLocalizedName() {
		String unlocalized = getUnlocalizedName();
		String generic = getGenericUnlocalizedName();
		return I18n.canTranslate(unlocalized) ? I18n.translateToLocal(unlocalized) : String.format(I18n.translateToLocal(generic), getOreName());
	}

	@Override
	public String getOreName() {
		return ore.getOreName();
	}

}
