package com.selfeatingwatermelon.mekores.proxy;

import com.selfeatingwatermelon.mekores.Log;
import com.selfeatingwatermelon.mekores.MekOres;
import com.selfeatingwatermelon.mekores.config.Config;
import com.selfeatingwatermelon.mekores.ore.Ore;
import com.selfeatingwatermelon.mekores.ore.OreManager;
import com.selfeatingwatermelon.mekores.remap.Remapper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		// Setup logging
		Log.preInit(event);
		
		// Pre-initialize primary config
		Config.preInit(event);

		// Register items
		OreManager.createConfiguredItems();
	}

	public void init(FMLInitializationEvent event) {
		// Register ore dictionary entries
		OreManager.registerOredictEntries();
		
		// Register recipes
		OreManager.registerVanillaRecipes();
		OreManager.registerMekanismRecipes();
	}

	public void postInit(FMLPostInitializationEvent event) {
		// Summary information
		OreManager.getOreItemList().forEach(item -> {
			Ore ore = item.getOre();
			Log.info("Added %s (color=%s)", ore.getOreName(), ore.getOreColorHex());
		});
	}
	
	public void serverStart(FMLServerStartingEvent event) {
		// Register commands
	}
	
	public void missingMappings(FMLMissingMappingsEvent event) {
		Remapper.remap(event.get());
	}

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
