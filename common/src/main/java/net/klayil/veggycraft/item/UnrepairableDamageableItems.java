package net.klayil.veggycraft.item;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class UnrepairableDamageableItems extends Item {
    private static final String FLOUR_ENDING = "_stacked_of_flour";
    private ModDataComponentTypes.ItemHealth health;

    public UnrepairableDamageableItems(Item.Properties itemProperties, int health) {
        super(
                itemProperties.component(ModDataComponentTypes.HEALTH.get(), new ModDataComponentTypes.ItemHealth(health, health))
                //, maxDamage, null
        );
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

    public static ItemStack getCraftingRemainderCommon(ItemStack stack) {
//        VeggyCraft.LOGGER.info("#StackItem: %s".formatted(stack.getItem().toString()));
        if (
            !BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith(FLOUR_ENDING)
        ) {
            return ItemStack.EMPTY;
        }

        return stack.copy();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.HEALTH.get())) return false;

        this.health = stack.get(ModDataComponentTypes.HEALTH.get());

        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        health = stack.get(ModDataComponentTypes.HEALTH.get());

        assert health != null;

        float damageVal = ( (float) health.max() - health.value() );

        return Mth.clamp(Math.round(13.0F - damageVal * 13.0F / (float)health.max()), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        health = stack.get(ModDataComponentTypes.HEALTH.get());

        assert health != null;

        int i = health.max();

        float damageVal = ( (float) i - health.value() );

        float f = Math.max(0.0F, ( (float)i - damageVal ) / (float)i);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }


//    @Override
//    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
//        ResourceLocation loc = BuiltInRegistries.ITEM.getKey(this);
//
//        if (loc.getPath().endsWith(FLOUR_ENDING)) {
//            String id = loc.getPath();
//            int amount = 0;
//            try {
//                amount = Integer.parseInt(id.substring(0, 2));
//            } catch (Exception ignored) {}
//
//            tooltipAdder.accept(
//                    Component.translatable("klay_api.description.flour_proto_bundle")
//                            .withStyle(ChatFormatting.BLUE)
//            );
//        }
//    }
}