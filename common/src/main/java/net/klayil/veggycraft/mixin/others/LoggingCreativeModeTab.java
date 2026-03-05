package net.klayil.veggycraft.mixin.others;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class LoggingCreativeModeTab extends net.minecraft.world.item.CreativeModeTab  {
    public LoggingCreativeModeTab(Row row, int column, Type type, Component displayName, Supplier<ItemStack> iconGenerator, net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator displayItemGenerator) {
        super(row, column, type, displayName, iconGenerator, displayItemGenerator);
    }

    public static class DisplayItemsGenerator implements net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator {
        private final net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator original;

        public DisplayItemsGenerator(net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator original) {
            this.original = original;
        }

        @Override
        public void accept(net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters itemDisplayParameters, net.minecraft.world.item.CreativeModeTab.Output output) {
            VeggyCraft.LOGGER.warn("#ActAccept %s | %s", itemDisplayParameters, output);
            this.original.accept(itemDisplayParameters, output);
        }
    }
}
