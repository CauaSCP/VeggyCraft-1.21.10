package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    ResourceLocation id;

    @Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    private void onGetName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        Item self = (Item) (Object) this;
        ResourceLocation _id = BuiltInRegistries.ITEM.getKey(self);
        if (!_id.getNamespace().equals(VeggyCraft.MOD_ID)) return;

        id = _id;


        MutableComponent res = Component.empty();

        if (self instanceof BlockItem)
            if (id.getPath().toLowerCase().contains("carnauba")) {
                String[] splitText = id.getPath().split("_");

                for (int i = 0; i < splitText.length; i++) {
                    // Append the translated part
                    res.append(Component.translatable("veggycraft.woods." + splitText[i]));

                    // Add a space between words, but not after the last word
                    if (i < splitText.length - 1) {
                        res.append(Component.literal(" "));
                    }
                }

                // Return your custom component and stop further execution
                cir.setReturnValue(res);
            }

        if(Objects.equals(id.getPath(), ModItems.waxID)) {
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
