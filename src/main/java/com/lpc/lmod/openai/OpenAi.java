package com.lpc.lmod.openai;

import com.lpc.lmod.utils.Typeg;

import static com.lpc.lmod.utils.MainUtils.toBlack;
import static com.lpc.lmod.utils.MainUtils.toResponse;


public class OpenAi {
    private static Generator generator;

    public OpenAi() {
        String token = "token";

        if(token == null || !token.startsWith("sk-")) { throw new RuntimeException("Token Not Found Error");}

        generator = new Generator(token);
    }

    public void aiConversation(String startphrase, Typeg type, double heat, boolean filter) {
        String start = startphrase;
        String response = "";

        for(int i = 0; i<5; i++) {
            try { response = generator.generateResponse(start, type, heat); }
            catch (InterruptedException e) { e.printStackTrace(); }

            if(filter) response = generator.bypassFilter(response);

            start = response;

            System.out.println(toResponse(response));
        }
    }

    public String respond(String phrase, Typeg type, double heat, boolean filter){
        String response = "";

        try { response = generator.generateResponse(phrase, type, heat);}
        catch (InterruptedException e) { e.printStackTrace(); }

        if(filter) response = generator.bypassFilter(response);

        System.out.println(toBlack("OPENAI CLASS | " + Thread.currentThread().getId()));
        return response;
    }
    
    public String random(){
        String r1 = generator.randomResponse();
        String r2 = generator.bypassFilter(r1);
        return r2;
    }
}