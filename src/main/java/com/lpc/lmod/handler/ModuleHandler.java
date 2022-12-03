package com.lpc.lmod.handler;

import com.lpc.lmod.modules.chatbot.ChatBot;
import com.lpc.lmod.modules.chatbot.ChatBotCommand;
import com.lpc.lmod.utils.Typeg;
import net.minecraftforge.client.ClientCommandHandler;

import static com.lpc.lmod.utils.MainUtils.toBlack;


public class ModuleHandler {

    public ModuleHandler(){
        new ChatBot();

        System.out.println(toBlack("MAIN | " + Thread.currentThread().getId()));

    }
}
