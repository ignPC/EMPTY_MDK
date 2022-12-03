package com.lpc.lmod.modules.chatbot;

import com.lpc.lmod.openai.OpenAi;
import com.lpc.lmod.utils.Typeg;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.lpc.lmod.modules.chatbot.ChatBot.*;
import static com.lpc.lmod.utils.MainUtils.toBlack;
import static com.lpc.lmod.utils.MinecraftUtils.addClientMessage;
import static com.lpc.lmod.utils.MinecraftUtils.sendMessage;

public class ChatBotCommand implements ICommand {

    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final OpenAi ai = new OpenAi();
    private static boolean generating = false;

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    public String AutoUser = null;
    public String AutoType = null;
    public static boolean AutoRespond = false;

    public ChatBotCommand(){
        System.out.println(toBlack("CHATBOT | " + Thread.currentThread().getId()));
    }

    @Override
    public String getCommandName() {
        return "chatbot";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "Responds to messages using AI";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>(1);
        aliases.add("ai");
        aliases.add("chatbot");
        aliases.add("c");
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        generateAndSendMessage(strings);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        if (getCommandName().equalsIgnoreCase(o.getCommandName())) return 1;
        return 0;
    }

    public void generateAndSendMessage(String[] strings){
        executorService.execute(() ->  {
            if (strings.length == 0) {
                addClientMessage("----ChatBot----");
                addClientMessage("wrong Usage");
                addClientMessage("try :");
                addClientMessage("  ai help");
            }

            else if (generating) {
                addClientMessage("----ChatBot----");
                addClientMessage("Chill...");
            }

            else if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
                addClientMessage("----ChatBot----");
                addClientMessage("ai help");
                addClientMessage("ai help type");
                addClientMessage("ai respond");
                addClientMessage("ai respond <type>");
                addClientMessage("ai responduser <user>");
                addClientMessage("ai responduser <user> <type>");
                addClientMessage("ai autorespond <user> <type>");
            }

            else if (strings.length == 2 && strings[0].equalsIgnoreCase("help") && strings[1].equalsIgnoreCase("type")){
                addClientMessage("----ChatBot----");
                addClientMessage("ai respond <type>");
                addClientMessage("types:");
                for(Typeg typeg : Typeg.typegArrayList){
                    addClientMessage("  " + typeg.getName());
                }
            }

            else if (strings.length == 2 && (strings[0].equalsIgnoreCase("responduser") || (strings[0].equalsIgnoreCase("r")))) {
                addClientMessage("Generating Response...");
                respondUser(strings[1].toLowerCase());
            }

            else if (strings.length == 3 && (strings[0].equalsIgnoreCase("responduser") || (strings[0].equalsIgnoreCase("r")))) {
                addClientMessage("Generating Response...");
                respondUser(strings[1].toLowerCase(), strings[2].toLowerCase());
            }

            else if (strings.length == 1 && (strings[0].equalsIgnoreCase("respond") || (strings[0].equalsIgnoreCase("r")))) {
                addClientMessage("Generating Response...");
                respond();
            }

            else if (strings.length == 2 && (strings[0].equalsIgnoreCase("respond") || (strings[0].equalsIgnoreCase("r")))) {
                addClientMessage("Generating Response...");
                respond(strings[1].toLowerCase());
            }

            else if (strings.length == 3 && (strings[0].equalsIgnoreCase("autorespond") || (strings[0].equalsIgnoreCase("ar")))) {
                setAutoRespond(strings[1].toLowerCase(), strings[2].toLowerCase());
            }
        });
    }

    private void respond(){
        String lastMessage = ChatList.get(ChatList.size()-1);

        String response = "";

        generating = true;

        Future<String> result = executorService.submit(new Callable<String>() {
            public String call() {
                return ai.respond(lastMessage, Typeg.toxic, 0.9d, true);
            }});
        try { response = result.get(); generating = false; }
        catch (Exception e) { e.printStackTrace();}

        sendResponse(response);
    }

    private void respond(String type){
        if(Typeg.getTypegFromString(type) == null) {
            addClientMessage("----ChatBot----");
            addClientMessage("wrong usage, try: ");
            addClientMessage("  ai respond <type>");
            addClientMessage("types:");
            for(Typeg typeg : Typeg.typegArrayList){
                addClientMessage("  " + typeg.getName());
            }
            return;
        }
        String lastMessage = ChatList.get(ChatList.size()-1);

        String response = "";

        generating = true;
        Future<String> result = executorService.submit(new Callable<String>() {
            public String call() {
                String response = ai.respond(lastMessage, Typeg.getTypegFromString(type), 0.9d, true);
                if(response.length()>1) return response;
                response = ai.random();
                return response;
            }});
        try { response = result.get(); generating = false; }
        catch (Exception e) { e.printStackTrace();}

        sendResponse(response);
    }

    private void respondUser(String user){
        String lastMessage = null;
        for(String u : UserList) {
            if(u.toLowerCase().contains(user)){
                lastMessage = ChatList.get(UserList.indexOf(u));
            }
            else{
                addClientMessage("----ChatBot----");
                addClientMessage("wrong usage, try: ");
                addClientMessage("  ai responduser <user>");
                addClientMessage("  ai responduser <user> <type>");
                return;
            }
        }

        String response = "";

        generating = true;
        String finalLastMessage = lastMessage;
        Future<String> result = executorService.submit(new Callable<String>() {
            public String call() {
                String response = ai.respond(finalLastMessage, Typeg.normal, 0.9d, true);
                if(response.length()>1) return response;
                else response = ai.random();
                return response;
            }});
        try { response = result.get(); generating = false; }
        catch (Exception e) { e.printStackTrace();}

        sendResponse(response);
    }

    private void respondUser(String user, String type){
        String lastMessage = null;
        for(String u : UserList) {
            if(u.toLowerCase().contains(user)){
                lastMessage = ChatList.get(UserList.indexOf(u));
            }
            else{
                addClientMessage("----ChatBot----");
                addClientMessage("wrong usage, try: ");
                addClientMessage("  ai responduser <user>");
                addClientMessage("  ai responduser <user> <type>");
                return;
            }

            if(Typeg.getTypegFromString(type) == null) {
                addClientMessage("----ChatBot----");
                addClientMessage("wrong usage, try: ");
                addClientMessage("  ai responduser <user>");
                addClientMessage("  ai responduser <user> <type>");
                addClientMessage("types:");
                for(Typeg typeg : Typeg.typegArrayList){
                    addClientMessage("  " + typeg.getName());
                }
                return;
            }
        }

        String response = "";

        generating = true;
        String finalLastMessage = lastMessage;
        Future<String> result = executorService.submit(new Callable<String>() {
            public String call() {
                String response = ai.respond(finalLastMessage, Typeg.getTypegFromString(type), 0.9d, true);
                if(response.length()>1) return response;
                else response = ai.random();
                return response;
            }});
        try { response = result.get(); generating = false; }
        catch (Exception e) { e.printStackTrace();}

        sendResponse(response);
    }

    private void setAutoRespond(String user, String type){
        if (AutoRespond) {
            addClientMessage("Stopping Automatic Responses.");
            AutoRespond = false;
            AutoUser = null;
            AutoType = null;
        }
        else {
            addClientMessage("Starting Automatic Responses...");
            if(!containsAKeyword(UserList, user)) { addClientMessage("User not detected in chat"); return;}
            if(Typeg.getTypegFromString(type) == null) {
                addClientMessage("----ChatBot----");
                addClientMessage("wrong usage, try: ");
                addClientMessage("  ai responduser <user> <type>");
                addClientMessage("types:");
                for(Typeg typeg : Typeg.typegArrayList){
                    addClientMessage("  " + typeg.getName());
                }
                return;
            }
            AutoUser = user;
            AutoType = type;
            AutoRespond = true;
        }
    }

    public void callAutoResponse(){
        String lastMessage = ChatList.get(UserList.indexOf(getStringContains(UserList, AutoUser)));
        if(lastMessage == null) throw new RuntimeException("USERNOTFOUND");

        String response = "";

        generating = true;
        Future<String> result = executorService.submit(new Callable<String>() {
            public String call() {
                String response = ai.respond(lastMessage, Typeg.getTypegFromString(AutoType), 0.9d, true);
                if(response.length()>1) return response;
                else response = ai.random();
                return response;
            }});
        try { response = result.get(); generating = false; }
        catch (Exception e) { e.printStackTrace();}

        sendResponse(response);
    }

    private void sendResponse(String response){
        if(response.length() >= 100){
            int messages = (int) Math.ceil(response.length()/100);

            for(int i=0; i<=messages; i++){
                String r;
                if(i == messages) {
                    r = response.substring(100*i, response.length() - (i-1)*100);
                } else {
                    r = response.substring(100 * i, 100 * (i + 1));
                }
                System.out.println(r + " ITERATION " + i);
                sendMessage(r);
            }
        } else sendMessage(response);
    }

    public boolean containsAKeyword(List<String> list, String keyword){
        for(String string : list){
            if (string.toLowerCase().contains(keyword.toLowerCase()))return true;
        }
        return false;
    }

    public String getStringContains(List<String> list, String keyword){
        for(String string : list) {
            if(string.toLowerCase().contains(keyword)) return string;
        }
        return null;
    }
}
