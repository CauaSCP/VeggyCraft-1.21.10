package net.klayil.veggycraft.neoforge;

import net.neoforged.fml.common.Mod;

import net.klayil.veggycraft.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
