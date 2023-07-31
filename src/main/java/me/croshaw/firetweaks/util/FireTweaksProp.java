package me.croshaw.firetweaks.util;

import net.minecraft.util.StringIdentifiable;

public enum FireTweaksProp implements StringIdentifiable {
    LIT("lit"),UNLIT("unlit"),SMOLDERING("smoldering"),BURNT("burnt");

    private final String name;

    private FireTweaksProp(String name) { this.name = name; }

    @Override
    public String asString() {
        return name;
    }
}