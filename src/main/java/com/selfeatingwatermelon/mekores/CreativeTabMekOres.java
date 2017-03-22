package com.selfeatingwatermelon.mekores;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class CreativeTabMekOres extends CreativeTabs {

	public CreativeTabMekOres() {
		super(MekOres.MODID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return Items.GLOWSTONE_DUST;
	}

}
