package net.klayil.veggycraft.item.potions.effect;

import dev.architectury.registry.registries.DeferredRegister;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(VeggyCraft.MOD_ID, Registries.MOB_EFFECT);

    public static Holder<MobEffect> PLACEHOLDER_EFFECT;

    public static void init() {
        MOB_EFFECTS.register("placeholder", () -> new PlaceholderEffect(MobEffectCategory.NEUTRAL));

        MOB_EFFECTS.register();
    }
}