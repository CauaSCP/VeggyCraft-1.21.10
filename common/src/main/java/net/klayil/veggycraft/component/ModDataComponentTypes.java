package net.klayil.veggycraft.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build()
        );
    }

    public record ItemHealth(int value, int max) {
        public static final Codec<ItemHealth> CODEC = RecordCodecBuilder.create(
                itemHealthInstance -> itemHealthInstance.group(
                        Codec.INT.fieldOf("value").forGetter(ItemHealth::value),
                        Codec.INT.fieldOf("max").forGetter(ItemHealth::max)
                ).apply(itemHealthInstance, ItemHealth::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemHealth> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, ItemHealth::value,
                ByteBufCodecs.INT, ItemHealth::max,
                ItemHealth::new
        );
    }

    public static DeferredRegister<DataComponentType<?>> DATA_COMPONENTS;

    public static RegistrySupplier<DataComponentType<ItemHealth>> HEALTH /* = register("health", itemHealthBuilder ->

            itemHealthBuilder
                    .persistent(ItemHealth.CODEC)
                    .networkSynchronized(ItemHealth.STREAM_CODEC)
    )*/;

    public static void registerDataComponentTypes() {
        DATA_COMPONENTS = DeferredRegister.create(VeggyCraft.MOD_ID, Registries.DATA_COMPONENT_TYPE);
        HEALTH = DATA_COMPONENTS.register("health", () ->
                DataComponentType.<ItemHealth>builder()
                        .persistent(ItemHealth.CODEC)
                        .networkSynchronized(ItemHealth.STREAM_CODEC)
                        .build()
        );

        DATA_COMPONENTS.register();
    }
}
