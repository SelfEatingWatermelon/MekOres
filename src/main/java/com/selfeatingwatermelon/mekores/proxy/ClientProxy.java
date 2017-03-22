package com.selfeatingwatermelon.mekores.proxy;

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
	public void handleEvent(FMLPreInitializationEvent event) {
		super.handleEvent(event);
		
		// Register block/item renderers
	}

	@Override
	public void handleEvent(FMLInitializationEvent event) {
		super.handleEvent(event);
	}

	@Override
	public void handleEvent(FMLPostInitializationEvent event) {
		super.handleEvent(event);
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
