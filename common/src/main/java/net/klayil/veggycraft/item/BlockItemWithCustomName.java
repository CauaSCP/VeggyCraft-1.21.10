package net.klayil.veggycraft.item;

import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.chat.Component;

public class BlockItemWithCustomName extends BlockItem {
    private static final String FLOUR_ENDING = "_stacked_of_flour";
    private ModDataComponentTypes.ItemHealth health;
    private int healthMax = -1;
    private Component translatableName;

    public BlockItemWithCustomName(Item.Properties itemProperties, Component _translatableName, RegistrySupplier<Block> block) {
        super(
                block.get(),
                itemProperties.component(ModDataComponentTypes.HEALTH.get(), new ModDataComponentTypes.ItemHealth(1000, 1000))
        );

        this.translatableName = _translatableName;
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return this.translatableName;
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
        return false;
    }

//    @Override
//    public int getBarWidth(ItemStack stack) {
//        health = stack.get(ModDataComponentTypes.HEALTH.get());
//
//        assert health != null;
//
//        float damageVal = ( (float) health.max() - health.value() );
//
//        return Mth.clamp(Math.round(13.0F - damageVal * 13.0F / (float)health.max()), 0, 13);
//    }
//
//    @Override
//    public int getBarColor(ItemStack stack) {
//        health = stack.get(ModDataComponentTypes.HEALTH.get());
//
//        assert health != null;
//
//        int i = health.max();
//
//        float damageVal = ( (float) i - health.value() );
//
//        float f = Math.max(0.0F, ( (float)i - damageVal ) / (float)i);
//        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
//    }


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