package com.cobeffectiveness.type;

public enum Type {
    NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE,
    FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG,
    ROCK, GHOST, DRAGON, DARK, STEEL, FAIRY,
    SHADOW, COSMIC, CRYSTAL, DIGITAL, LIGHT, NUCLEAR, // Gravels Extended Battles Types
    PLASTIC, SLIME, SOUND, WIND, ELDRITCH, BLOOD; // Gravels Extended Battles Types

    public static Type fromCobblemon(String s) {
        return Type.valueOf(s.toUpperCase());
    }

}
