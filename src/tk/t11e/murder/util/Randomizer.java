package tk.t11e.murder.util;
// Created by booky10 in Murder (20:47 18.01.20)

public class Randomizer {

    public static int random(int min,int max) {
        max++;
        return (int) (min+(Math.random()*(max-min)));
    }
}