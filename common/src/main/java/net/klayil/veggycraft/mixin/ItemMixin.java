package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    ResourceLocation veggycraft$id;

    @Unique
    Map<ResourceLocation, Component> veggycraft$hoverCases = Map.of(
            ResourceLocation.withDefaultNamespace("wheat"), Component.translatable("klay_api.smash.wheat")
            .withStyle(style -> style.withColor(TextColor.fromRgb(0x89F336)))

            ,

            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "birch_pulp_modal"), Component.translatable(
            "klay_api.description.pulp")
                    .withStyle(ChatFormatting.WHITE)
                    .withStyle(ChatFormatting.ITALIC)
                    .withStyle(ChatFormatting.BOLD)
    );

    @Inject(
            method = "appendHoverText",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag, CallbackInfo ci) {
        Item self = (Item) (Object) this;
        veggycraft$id = BuiltInRegistries.ITEM.getKey(self);

        if (!veggycraft$hoverCases.containsKey(veggycraft$id)) {
            ci.cancel();
            return;
        }

        tooltipAdder.accept(veggycraft$hoverCases.get(veggycraft$id));
    }

    @Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    private void onGetName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        Item self = (Item) (Object) this;
        ResourceLocation _id = BuiltInRegistries.ITEM.getKey(self);
        if (!_id.getNamespace().equals(VeggyCraft.MOD_ID)) return;

        veggycraft$id = _id;


        MutableComponent res = Component.empty();

        if (self instanceof BlockItem) {
            if (veggycraft$id.getPath().toLowerCase().contains("carnauba")) {
                String bfr = "veggycraft.woods.";
                String carnauba_translation = "carnauba";
                String affix = veggycraft$id.getPath().replace("_carnauba", "");

                res.append(Component.translatable(bfr + "prefix." + affix));
                res.append(Component.translatable(bfr + carnauba_translation));
                res.append(Component.translatable(bfr + "suffix." + affix));

                cir.setReturnValue(res);
            }

            if (ItemStack.isSameItem(new ItemStack(self), new ItemStack(ModItems.EVEN_STRIPPED_BIRCH_LOG))) {
                res.append(Component.translatable("even_stripped.prefix"));
                res.append(Component.translatable("block.minecraft.birch_log"));
                res.append(Component.translatable("even_stripped.suffix"));

                cir.setReturnValue(res);
            }
        }

        if(Objects.equals(veggycraft$id.getPath(), ModItems.waxID)) {
            res = res.append(Component.translatable("wax.prefix"));
            res = res.append(Component.translatable("veggycraft.woods.carnauba"));
            res = res.append(Component.translatable("wax.suffix"));

            cir.setReturnValue(res);
        }
    }



    /*
    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    public void whenInteractedLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(interactionTarget instanceof CopperGolem golem)) return;

        if (
                !ItemStack.isSameItem(
                        new ItemStack((Item)(Object)this),
                        new ItemStack(ModItems.CARNAUBA_WAX)
                )
        ) return;

        if ( ((CopperGollumMixin) golem).waxedLongVale() != -1L  ) return;


        //error: java.lang.ClassCastException: class net.minecraft.world.entity.animal.coppergolem.CopperGolem cannot be cast to class net.klayil.veggycraft.entity.CopperGolemExtended (net.minecraft.world.entity.animal.coppergolem.CopperGolem and net.klayil.veggycraft.entity.CopperGolemExtended are in unnamed module of loader 'knot' @4a0e3bd) (1746ms ago)


        Level level = player.level();

        level.playSound(
            player, golem.getX(), golem.getY(), golem.getZ(),
            SoundEvents.HONEYCOMB_WAX_ON, SoundSource.NEUTRAL,
            1F, 1F
        );

        if (level instanceof ServerLevel serverLevel) serverLevel.levelEvent(null, 3003, golem.blockPosition(), 0);

        InteractionResult res = InteractionResult.SUCCESS;

        if (level.isClientSide()) {
            cir.setReturnValue(res);
            cir.cancel();
        }

        ((CopperGollumMixin) golem).setWaxed(-2L);

        EquipmentSlot slot = (usedHand == InteractionHand.MAIN_HAND)
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND;

        stack.hurtAndBreak(1, player, slot);

        cir.setReturnValue(res);
        cir.cancel();
    }
    */

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOnMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (
            !ItemStack.isSameItem(
                new ItemStack((Item)(Object)this),
                new ItemStack(ModItems.CARNAUBA_WAX)
            )
        ) return;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        Optional<BlockState> waxedState = Optional.ofNullable(
                HoneycombItem.WAXABLES.get().get(state.getBlock())
        ).map(
                block -> block.withPropertiesOf(state)
        );

        InteractionResult res = InteractionResult.SUCCESS;

        if (waxedState.isPresent()) {
            level.playSound(
                    player, pos, SoundEvents.HONEYCOMB_WAX_ON,
                    SoundSource.BLOCKS, 1F, 1F
            );

            level.levelEvent(player, 30003, pos, 0);

            if (level.isClientSide()) {
                cir.setReturnValue(res);
                cir.cancel();
            }

            if (player != null) {
                EquipmentSlot slot = (context.getHand() == InteractionHand.MAIN_HAND)
                        ? EquipmentSlot.MAINHAND
                        : EquipmentSlot.OFFHAND;

                context.getItemInHand().hurtAndBreak(
                1,
                        player,
                       slot
                );
            }

//            state.getBlock();

            Block waxed = HoneycombItem.WAXABLES.get().get(state.getBlock());
            BlockState newState = waxed.withPropertiesOf(state);

            level.setBlockAndUpdate(pos, newState);

            cir.setReturnValue(res);
            cir.cancel();
        }
    }

//    @Override
//    public @NotNull Component getName(ItemStack stack) {
//        if ( (Item)(Object)this instanceof BlockItem self) {
//            ResourceLocation id = BuiltInRegistries.ITEM.getKey(self);
//
//            if (Objects.equals(id.getNamespace(), VeggyCraft.MOD_ID) & id.getPath().toLowerCase().contains("carnauba")) {
//                String[] splitText = id.getPath().split("_");
//
//                Component res = Component.literal("");
//
//                for (int i = 0; i < splitText.length ; i++) {
//                    Component toAppend = Component.translatable("veggycraft.woods.%s".formatted(splitText[i]));
//                    if (i != splitText.length-1) toAppend = toAppend
//                }
//            }
//        }
//
//        return (Component)stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
//    }
}
