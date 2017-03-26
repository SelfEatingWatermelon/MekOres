package com.selfeatingwatermelon.mekores.proxy;

import com.selfeatingwatermelon.mekores.client.ColorManager;
import com.selfeatingwatermelon.mekores.ore.OreManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		// Register item models
		OreManager.registerItemModels();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		// Register item color handlers
		ColorManager colorManager = new ColorManager();
		OreManager.getOreItemList().forEach(item -> {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(colorManager, item);
		});
	}

	@Override
	public boolean isDedicatedServer() {
		return false;
	}

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().world;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}

}
