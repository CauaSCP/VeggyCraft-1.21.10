package net.klayil.veggycraft.item.potions.effect;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PlaceholderEffect extends MobEffect {
    public PlaceholderEffect(MobEffectCategory category) {

        super(category, ModItems.OTHER_SPLASH_POTION_COLOR);

    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {

        return true;

    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        VeggyCraft.LOGGER.info("#Movement: %s", entity.getDeltaMovement());

        return super.applyEffectTick(level, entity, amplifier);
    }
}
