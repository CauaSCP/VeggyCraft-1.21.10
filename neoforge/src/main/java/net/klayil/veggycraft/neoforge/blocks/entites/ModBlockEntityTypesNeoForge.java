package net.klayil.veggycraft.neoforge.blocks.entites;

import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.block.entities.ModBedEntity;
import net.klayil.veggycraft.block.entities.ModBlockEntityTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityTypesNeoForge extends ModBlockEntityTypes {
    public static void initBlockEntityTypes() {
        STRAW_BED = registerBlockEntity("straw_bed", () -> new BlockEntityType<>(ModBedEntity::new, ModBlocks.STRAW_BED.get()));

        writeRegister();
    }
}
