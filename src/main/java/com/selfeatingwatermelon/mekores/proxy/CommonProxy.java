package com.selfeatingwatermelon.mekores.proxy;

import com.selfeatingwatermelon.mekores.Logger;
import com.selfeatingwatermelon.mekores.MekOres;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void handleEvent(FMLPreInitializationEvent event) {}

	public void handleEvent(FMLInitializationEvent event) {}

	public void handleEvent(FMLPostInitializationEvent event) {}

	public boolean isDedicatedServer() {
		return true;
	}

	public World getClientWorld() {
		return null;
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

}
