package net.klayil.veggycraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class UnrepairableDamageableItems extends net.klayil.veggycraft.item.RepairableItemsExtension {
    private static final String FLOUR_ENDING = "_items_stacked_of_flour";

    public UnrepairableDamageableItems(Item.Properties itemProperties, int maxDamage) {
        super(itemProperties, maxDamage, null);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(this);

        if (loc.getPath().endsWith(FLOUR_ENDING)) {
            String id = loc.getPath();
            int amount = 0;
            try {
                amount = Integer.parseInt(id.substring(0, 2));
            } catch (Exception ignored) {}

            return Component.translatable("architectury.klay_api.flour_proto_bundle");
        }

        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(this);

        if (loc.getPath().endsWith(FLOUR_ENDING)) {
            String id = loc.getPath();
            int amount = 0;
            try {
                amount = Integer.parseInt(id.substring(0, 2));
            } catch (Exception ignored) {}

            tooltipAdder.accept(
                    Component.translatable("klay_api.description.flour_proto_bundle")
                            .withStyle(ChatFormatting.BLUE)
            );
        }
    }
}