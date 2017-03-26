package com.selfeatingwatermelon.mekores.client;

import com.selfeatingwatermelon.mekores.item.IColoredItem;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorManager implements IItemColor {

	/** 
	 * Return a color value you wish the stack be colored with.<br>
	 * It will start getting called from loading the world onwards.
	 * 
	 * @param tintIndex index of the layer <i>(model file property)</i> to be colored
	 */
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		Item item = (Item)stack.getItem();
		return (item instanceof IColoredItem) ? ((IColoredItem) item).getColorFromItemstack(stack, tintIndex) : -1;
	}
	
}
