package com.selfeatingwatermelon.mekores;

import com.selfeatingwatermelon.mekores.config.Config;
import com.selfeatingwatermelon.mekores.proxy.CommonProxy;
import com.selfeatingwatermelon.mekores.remap.Remapper;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.UUID;

@Mod(modid = MekOres.MODID, name = MekOres.NAME, version = MekOres.VERSION, dependencies = MekOres.DEPENDENCIES, guiFactory = MekOres.GUIFACTORY, acceptedMinecraftVersions = MekOres.MCVERSIONS)
public class MekOres {
	public static final String MODID = "mekores";
	public static final String NAME = "MekOres";
	public static final String VERSION = "@VERSION@";
	public static final String DEPENDENCIES = "required-after:Forge@[12.18.3.2185,);after:*";
	public static final String GUIFACTORY = "com.selfeatingwatermelon.mekores.gui.ModGuiFactory";
	public static final String MCVERSIONS = "[1.10.2]";

	public static final CreativeTabMekOres creativeTab = new CreativeTabMekOres();

	@SidedProxy(clientSide = "com.selfeatingwatermelon.mekores.proxy.ClientProxy", serverSide = "com.selfeatingwatermelon.mekores.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(MODID)
	public static MekOres instance;
	
	public Config config;

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Logger.setLogger(event.getModLog());
		
		config = new Config(event.getSuggestedConfigurationFile());
		
		// Register Blocks/Tiles/Items/Fluids

		proxy.handleEvent(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register/modify recipes

		proxy.handleEvent(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.handleEvent(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		// Register commands
	}

	@EventHandler
	public void missingMappings(FMLMissingMappingsEvent event) {
		Remapper.remap(event.get());
	}
}
