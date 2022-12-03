package com.lpc.lmod.modules.chatbot;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lpc.lmod.utils.MainUtils.toBlack;
import static com.lpc.lmod.utils.MainUtils.toRed;
import static scala.collection.immutable.Nil.indexOf;

public class ChatBot {

    public static ArrayList<String> ChatList = new ArrayList<>();
    public static ArrayList<String> UserList = new ArrayList<>();
    public ChatBotCommand chatBotCommand = new ChatBotCommand();
    private boolean ThreadUsed;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public ChatBot(){
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler clientCommandHandler = ClientCommandHandler.instance;
        clientCommandHandler.registerCommand(chatBotCommand);
    }

    @SubscribeEvent
    public void ClientChatRecieved(ClientChatReceivedEvent e){
        String message = e.message.getUnformattedTextForChat().substring(3);
        String user = e.message.getFormattedText().toLowerCase().substring(e.message.getFormattedText().indexOf("<"), e.message.getFormattedText().indexOf("r>"));
        System.out.println(toRed(user));
        System.out.println(toBlack(message));

        ChatList.add(message);
        UserList.add(user);

        if(ChatList.size() == 1000) {
            for(int i = 100; i <= 999; i++){
                ChatList.remove(i);
                UserList.remove(i);
            }
        }

        if (chatBotCommand.AutoUser == null) return;
        if (user.contains(chatBotCommand.AutoUser) && !ThreadUsed){
            ThreadUsed = true;
            executorService.execute(() ->  {
                try {Thread.sleep(200);} catch (InterruptedException ex) {ex.printStackTrace();}
                chatBotCommand.callAutoResponse();
                ThreadUsed = false;
            });
        }
    }
}

