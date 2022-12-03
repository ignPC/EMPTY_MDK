package com.lpc.lmod.openai;

import com.lpc.lmod.utils.MainUtils;
import com.lpc.lmod.utils.Typeg;
import com.theokanning.openai.*;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;

import java.util.Collections;

import static com.lpc.lmod.utils.MainUtils.toBlack;
import static com.lpc.lmod.utils.MinecraftUtils.addClientMessage;

public class Generator{
    /**
     * Generator class, uses openai java library to generate responses based on input.
     */
    private double temp;
    private int iterations;
    OpenAiService service;
    String token;

    public Generator(String token) {
        this.token = token;
        service = new OpenAiService(token);
    }

    public String generateResponse(String phrase, Typeg type, double heat) throws InterruptedException {
        String response;
        while (true) {
            String prompt = "respond to the message below with " + type.getPhrase() +
                    "Make sure NOT TO REPEAT the previous message, DO NOT USE ANY INVISIBLE CHARACTERS, DO NOT USE THE SAME INSULT! " +
                    "Make the response maximum 3 sentences long. " +
                    "\n\nMESSAGE: \n\n" +
                    phrase +
                    "\n\nRESPONSE: ";

            System.out.println(toBlack("Generating response..."));

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(prompt)
                    .echo(true)
                    .presencePenalty(1d)
                    .frequencyPenalty(1d)
                    .temperature(temp)
                    .model("text-davinci-002")
                    .maxTokens(400)
                    .stop(Collections.singletonList(phrase))
                    .build();
            CompletionResult completion = service.createCompletion(completionRequest);

            String scompletion = String.valueOf(completion.getChoices());
            String full = scompletion.substring(scompletion.indexOf(prompt), scompletion.indexOf(", index=0"));
            response = full.substring(full.indexOf("RESPONSE: ") + "RESPONSE: ".length());

            if (response.length() < 5 || MainUtils.StringSimilarity.similarity(response, phrase) > 0.60) {
                if (iterations > 2) {
                    System.out.println(toBlack("Terminal failure... Returning"));
                    break;
                }
                System.out.println(toBlack("Response failed..."));
                iterations++;
                temp += 0.2d;
                continue;
            } else {
                System.out.println(toBlack("Response generated..."));
                temp = heat;
                iterations = 0;
                break;
            }
        }
        System.out.println(toBlack(response + " | " + response.length() + " | " + MainUtils.StringSimilarity.similarity(response, phrase) + " | GENERATOR CLASS " + Thread.currentThread().getId()));
        return response;
    }


    public String bypassFilter(String phrase) {
        if(phrase.length() < 3) return phrase;

        String prompt = "Make it so the message below can bypass filters for bad language, for example: " +
                "retard = r3tard, faggot = f4ggot, kill yourself = k!ll yourself, piece of shit = piece of sh!t, ..." +
                "\n\nMESSAGE: \n\n" +
                phrase +
                "\n\nRESPONSE: ";

        //System.out.println(Utils.toBlack("Creating response..."));

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .echo(true)
                .temperature(0d)
                .model("text-davinci-002")
                .maxTokens(400)
                .build();
        CompletionResult completion = service.createCompletion(completionRequest);

        String scompletion = String.valueOf(completion.getChoices());
        String full = scompletion.substring(scompletion.indexOf(prompt), scompletion.indexOf(", index=0"));
        String response = full.substring(full.indexOf("RESPONSE: ") + "RESPONSE: ".length());

        return response;
    }

    public String randomResponse(){
        String prompt = "Send a random message about someones family, like: " +
                "thats why yo granny got hit by alvin from the chipmunks, thats why yo dad got no left nipple, that why yo uncle wiggled your weener" +
                "\n\nRESPONSE: ";

        //System.out.println(Utils.toBlack("Creating response..."));

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .echo(true)
                .temperature(1d)
                .model("text-davinci-002")
                .maxTokens(400)
                .build();
        CompletionResult completion = service.createCompletion(completionRequest);

        String scompletion = String.valueOf(completion.getChoices());
        String full = scompletion.substring(scompletion.indexOf(prompt), scompletion.indexOf(", index=0"));
        String response = full.substring(full.indexOf("RESPONSE: ") + "RESPONSE: ".length());

        return response;
    }
}

