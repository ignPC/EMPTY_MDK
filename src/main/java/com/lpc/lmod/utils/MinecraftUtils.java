package com.lpc.lmod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.awt.*;

public class MinecraftUtils {
    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public static void addClientMessage(String message) {
        minecraft.thePlayer.addChatMessage(new ChatComponentText("[LMOD] " + message));
    }

    public static void sendMessage(String message) {
        minecraft.thePlayer.sendChatMessage(message);
    }
}
