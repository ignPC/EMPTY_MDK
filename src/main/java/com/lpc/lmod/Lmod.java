package com.lpc.lmod;

import com.lpc.lmod.openai.OpenAi;
import com.lpc.lmod.handler.ModuleHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "L-Mod", version = "1.0", acceptedMinecraftVersions = "[1.8.9]")
public class Lmod {

    private static Lmod instance;
    private final Minecraft minecraft = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;

        new ModuleHandler();
    }
}
