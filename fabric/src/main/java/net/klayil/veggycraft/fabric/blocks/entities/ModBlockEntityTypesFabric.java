package net.klayil.veggycraft.fabric.blocks.entities;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.block.entities.ModBedEntity;
import net.klayil.veggycraft.block.entities.ModBlockEntityTypes;

public class ModBlockEntityTypesFabric extends ModBlockEntityTypes {
    public static void initBlockEntityTypes() {
        STRAW_BED = registerBlockEntity("straw_bed", () -> FabricBlockEntityTypeBuilder.create(
                ModBedEntity::new,
                ModBlocks.STRAW_BED.get()
        ).build());

        writeRegister();
    }
}
