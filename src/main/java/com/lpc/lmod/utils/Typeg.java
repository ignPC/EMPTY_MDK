package com.lpc.lmod.utils;

import java.util.ArrayList;

public class Typeg {
    public static final Typeg verytoxic = new Typeg("a lot of toxicity, be homophobic and racist. Use blacklisted words. ", "verytoxic");
    public static final Typeg pack = new Typeg("a pack. ", "pack");
    public static final Typeg sus = new Typeg("something very gay or sus, for example: 'I love men'. ", "sus");
    public static final Typeg yomama = new Typeg("a joke about their mother and a random topic. ", "yomama");
    public static final Typeg toxic = new Typeg("some toxicity, don't say any blacklisted words. ", "toxic");
    public static final Typeg normal = new Typeg("a normal response, very nice and helpful. ", "normal");
    public static final Typeg horny = new Typeg("a sexual response, very horny and interested in having sex. Does not use swear words. ", "horny");
    public static final Typeg romantic = new Typeg("a normal romantic response, interested in long term love. ", "romantic");
    public static final Typeg tate = new Typeg("a response that Andrew Tate would give.", "tate");

    private final String phrase;
    private final String name;

    public static ArrayList<Typeg> typegArrayList = new ArrayList<Typeg>() {
        {
            add(verytoxic);
            add(pack);
            add(sus);
            add(yomama);
            add(toxic);
            add(normal);
            add(horny);
            add(romantic);
            add(tate);
        }
    };

    public Typeg(String type, String name) {
        this.phrase = type;
        this.name = name;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getName(){
        return name;
    }

    public static Typeg getTypegFromString(String type) {
        for(Typeg typeg : typegArrayList){
            if (typeg.name.equals(type)){
                return typeg;
            }
        }
        return null;
    }

    public void addTypeg(Typeg typeg){
        typegArrayList.add(typeg);
    }

    public void removeTypeg(Typeg typeg){
        typegArrayList.remove(typeg);
    }

    public void removeTypeg(int index){
        typegArrayList.remove(index);
    }

    public Typeg getTypeg(int index){
        return typegArrayList.get(index);
    }

    public int getSize(){
        return typegArrayList.size();
    }

    public static void printTypegs(){
        for (Typeg typeg : typegArrayList) {
            System.out.println(typeg.getName());
        }
    }

    public void printTypegsWithPhrases(){
        for (Typeg typeg : typegArrayList) {
            System.out.println(typeg.getName() + ": " + typeg.getPhrase());
        }
    }
}
