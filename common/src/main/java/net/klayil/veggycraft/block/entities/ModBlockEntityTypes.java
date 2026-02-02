package net.klayil.veggycraft.block.entities;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            VeggyCraft.MOD_ID,
            Registries.BLOCK_ENTITY_TYPE
    );

    public static RegistrySupplier<BlockEntityType<ModBedEntity>> STRAW_BED;

    public static void writeRegister() {
        BLOCK_ENTITIES.register();
    }

    public static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, Supplier<T> blockEntity){
        return BLOCK_ENTITIES.register(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, name), blockEntity);
    }
}
