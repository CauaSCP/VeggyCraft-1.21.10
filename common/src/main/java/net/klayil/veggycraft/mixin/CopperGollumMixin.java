package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopperGolem.class)
public class CopperGollumMixin {

    @Shadow
    private long nextWeatheringTick;


    @Inject(
            method = "mobInteract",
            at = @At("TAIL"),
            cancellable = true
    )
    public void whenSelfInteracted(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack handStack = player.getItemInHand(hand);

        boolean isMyWax = ItemStack.isSameItem(handStack, new ItemStack(ModItems.CARNAUBA_WAX));

        if (handStack.is(Items.HONEYCOMB) | isMyWax
        ) if (nextWeatheringTick == -2L) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        } else if (isMyWax) {
            CopperGolem self = ((CopperGolem) (Object) this);
            Level level = self.level();

            level.levelEvent(self, 3003, self.blockPosition(), 0);
            this.nextWeatheringTick = -2L;

            EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND)
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND;

            handStack.hurtAndBreak(1, player, slot);

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}
