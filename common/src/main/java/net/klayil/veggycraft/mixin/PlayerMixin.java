package net.klayil.veggycraft.mixin;

//import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBedBlock;
import net.klayil.veggycraft.block.PlayerForSleepingBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerForSleepingBlock {
    @Shadow public abstract int getSleepTimer();

    @Unique
    private boolean veggycraft$DealDamage = false;

    @Unique
    private Block veggycraft$sleepingBlock;

    @Override
    public Block veggycraft$getSleepingBlock() {
        return veggycraft$sleepingBlock;
    }

    @Override
    public void veggycraft$setSleepingBlock(Block b) {
        this.veggycraft$sleepingBlock = b;

    }

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void tickForSleeping(CallbackInfo ci) {
        if (veggycraft$DealDamage && this.getSleepTimer() == 0) {
            veggycraft$DealDamage = false;

            @Nullable Block block = veggycraft$getSleepingBlock();

            Level level = this.level();

            if (level.isClientSide()) return;

            if (block != null) if (block instanceof ModBedBlock modBedBlock) {
                Player player = (Player) (Object) this;

                DamageSource damageSource = new DamageSource(
                        level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(
                                        VeggyCraft.MOD_ID,
                                        "itchy_bed"
                                ))).get()
                );

                VeggyCraft.LOGGER.warn("Dealing %.7f damage from itchy_bed", modBedBlock.awakenDamage);

                if (modBedBlock.awakenDamage != null)
                    player.hurtServer((ServerLevel) level, damageSource, (float) (double) modBedBlock.awakenDamage);

//                DamageSource damageSource = new DamageSource(
//                        level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
//                                .get(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(
//                                        VeggyCraft.MOD_ID,
//                                        "itchy_bed"
//                                ))).get()
//                );
//
//                player.hurtServer((ServerLevel) level, damageSource, 4f);
            }
        }

        if (this.isSleeping() && this.getSleepTimer() >= 100) veggycraft$DealDamage = true;
    }
}
