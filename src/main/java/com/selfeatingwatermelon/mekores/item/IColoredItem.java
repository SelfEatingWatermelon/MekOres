package com.selfeatingwatermelon.mekores.item;

import net.minecraft.item.ItemStack;

public interface IColoredItem {
	int getColorFromItemstack(ItemStack stack, int tintIndex);
}
