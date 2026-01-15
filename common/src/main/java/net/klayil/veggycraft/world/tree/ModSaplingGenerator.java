package net.klayil.veggycraft.world.tree;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.world.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModSaplingGenerator {
    public static TreeGrower CARNAUBA = new TreeGrower(
            VeggyCraft.MOD_ID+":carnauba",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.CARNAUBA_KEY),
            Optional.empty()
    );
}
