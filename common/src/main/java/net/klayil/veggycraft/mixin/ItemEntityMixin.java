package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import java.util.concurrent.TimeUnit;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "tick",
            at = @At("TAIL"),
            cancellable = true
    )
    private void onTick(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;

        if (!( self.getItem().getItem().getName().toString().contains("molasses_bottle") )) ci.cancel();

        if (self.getAge()/20.0f >= MolassesToBrownSugar.itsTimeToChill ) {
            MolassesToBrownSugar.toChill(self);
            ci.cancel();
        }
    }
}