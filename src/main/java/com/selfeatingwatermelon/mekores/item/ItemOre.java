package com.selfeatingwatermelon.mekores.item;

import java.awt.Color;
import java.util.List;

import com.selfeatingwatermelon.mekores.config.OreStates;
import com.selfeatingwatermelon.mekores.ore.Ore;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemOre extends ItemBase implements IMetaItem, IColoredItem {
	
	private final Ore ore;
	private final List<OreStates> stateList = OreStates.getItemStatesToRegister();

	public ItemOre(Ore ore) {
		super();
		this.ore = ore;
		setRegistryName(ore.getOreKey());
		setUnlocalizedName(ore.getOreKey());
		setHasSubtypes(true);
	}
	
	public Ore getOre() {
		return ore;
	}
	
	public String getOreName() {
		return ore.getOreName();
	}

	public String getOredictPrefixForStack(ItemStack stack) {
		int meta = stack.getItemDamage(); 
		return (meta < stateList.size()) ? stateList.get(meta).getOredictPrefix() : "unknown";
	}
	
	public String getOredictNameForStack(ItemStack stack) {
		return getOredictPrefixForStack(stack) + ore.getOreName();
	}
	
	@Override
	public String getTexture(int meta) {
		return stateList.get(meta).getOredictPrefix();
	}

	@Override
	public int getVariants() {
		return OreStates.getItemStatesToRegister().size();
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < stateList.size(); i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = getOredictNameForStack(stack); 
		return "item." + name + ".name";
	}
	
	public String getGenericUnlocalizedName(ItemStack stack) {
		return "item." + getOredictPrefixForStack(stack) + "Generic.name";
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String unlocalized = getUnlocalizedName(stack);
		String generic = getGenericUnlocalizedName(stack);
		return I18n.canTranslate(unlocalized) ? I18n.translateToLocal(unlocalized) : String.format(I18n.translateToLocal(generic), getOreName());
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		return tintIndex == 0 ? ore.getOreColor().getRGB() : -1;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ItemOre && ((ItemOre) obj).getOre().getOreKey().equals(ore.getOreKey());
	}

	@Override
	public int hashCode() {
		return ore.getOreKey().hashCode();
	}
	
}
