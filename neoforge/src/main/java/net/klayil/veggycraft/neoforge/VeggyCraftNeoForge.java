package net.klayil.veggycraft.neoforge;

import net.neoforged.fml.common.Mod;

import net.klayil.veggycraft.VeggyCraft;

@Mod(VeggyCraft.MOD_ID)
public final class VeggyCraftNeoForge {
    public VeggyCraftNeoForge() {
        // Run our common setup.
        VeggyCraft.init();
    }
}
