package com.selfeatingwatermelon.mekores.integration;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class Mekanism {

	public static void addEnrichmentChamberRecipe(ItemStack input, ItemStack output) {
		addRecipe("EnrichmentChamberRecipe", input, output);
	}

	public static void addCrusherRecipe(ItemStack input, ItemStack output) {
		addRecipe("CrusherRecipe", input, output);
	}

	public static void addPurificationChamberRecipe(ItemStack input, Gas gas, ItemStack output) {
		addRecipe("PurificationChamberRecipe", input, gas, output);
	}

	public static void addChemicalInjectionChamberRecipe(ItemStack input, Gas gas, ItemStack output) {
		addRecipe("ChemicalInjectionChamberRecipe", input, gas, output);
	}

	public static void addChemicalDissolutionChamberRecipe(ItemStack input, GasStack output) {
		addRecipe("ChemicalDissolutionChamberRecipe", input, output);
	}

	public static void addChemicalWasherRecipe(GasStack input, GasStack output) {
		addRecipe("ChemicalWasherRecipe", input, output);
	}

	public static void addChemicalCrystallizerRecipe(GasStack input, ItemStack output) {
		addRecipe("ChemicalCrystallizerRecipe", input, output);
	}

	private static void addRecipe(String key, ItemStack input, ItemStack output) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
		nbt.setTag("output", output.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", key, nbt);
	}

	private static void addRecipe(String key, ItemStack input, Gas gas, ItemStack output) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
		nbt.setTag("gasType", gas.write(new NBTTagCompound()));
		nbt.setTag("output", output.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", key, nbt);
	}

	private static void addRecipe(String key, ItemStack input, GasStack output) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
		nbt.setTag("output", output.write(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", key, nbt);
	}

	private static void addRecipe(String key, GasStack input, GasStack output) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("input", input.write(new NBTTagCompound()));
		nbt.setTag("output", output.write(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", key, nbt);
	}

	private static void addRecipe(String key, GasStack input, ItemStack output) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("input", input.write(new NBTTagCompound()));
		nbt.setTag("output", output.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage("Mekanism", key, nbt);
	}

}
