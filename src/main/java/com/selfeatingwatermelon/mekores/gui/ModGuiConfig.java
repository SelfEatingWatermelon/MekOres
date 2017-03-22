package com.selfeatingwatermelon.mekores.gui;

import com.selfeatingwatermelon.mekores.MekOres;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {
    public ModGuiConfig(GuiScreen guiScreen) {
        super(
            guiScreen,
            MekOres.instance.config.getConfigElements(),
            MekOres.MODID,
            false,
            false,
            GuiConfig.getAbridgedConfigPath(MekOres.instance.config.getConfig().toString())
        );
    }
}
