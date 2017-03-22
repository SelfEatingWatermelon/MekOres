package com.selfeatingwatermelon.mekores.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.selfeatingwatermelon.mekores.MekOres;

public final class Config {
    private Configuration config;

    // Categories
    //private static final String ENERGY = "energy";

    // Category: ENERGY
    //public int controllerBaseUsage;

    public Config(File configFile) {
        config = new Configuration(configFile);

        MinecraftForge.EVENT_BUS.register(this);

        loadConfig();
    }

    public Configuration getConfig() {
        return config;
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(MekOres.MODID)) {
            loadConfig();
        }
    }

    private void loadConfig() {
        // Category: ENERGY
        //controllerBaseUsage = config.getInt("controllerBase", ENERGY, 0, 0, Integer.MAX_VALUE, "The base energy used by the Controller");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SuppressWarnings("unchecked")
    public List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();

        // list.addAll(new ConfigElement(config.getCategory(ENERGY)).getChildElements());

        return list;
    }
}
