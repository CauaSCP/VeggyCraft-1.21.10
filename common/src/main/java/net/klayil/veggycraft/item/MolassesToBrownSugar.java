package net.klayil.veggycraft.item;

import net.klayil.veggycraft.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MolassesToBrownSugar {
    final static public int minutesToChill = 4;
    final static public int secondsToChill = 30;
    static public int itsTimeToChill = 60 * minutesToChill + secondsToChill;

    // final static private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    final static public Map<TimeUnit, Integer> waitsValues = Map.of(
            TimeUnit.MINUTES, 4,
            TimeUnit.SECONDS, 30
    );

    public static List<MutableComponent> parsedTexts;

    static {
        List<MutableComponent> parsedRes = new ArrayList<>(List.of(Component.translatable("klay_api.wait.for").append(" ")));
        boolean firstForSpace = true;

        for (TimeUnit keyForTextForLabel : List.of(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)) {
            if (!waitsValues.containsKey(keyForTextForLabel)) continue;

            if (!firstForSpace) parsedRes.add(Component.literal("& "));
            firstForSpace = false;

            String parsedNumber = "%d".formatted(waitsValues.get(keyForTextForLabel));
            switch (keyForTextForLabel) {
                case HOURS -> parsedRes.getLast().append(Component.translatable("klay_api.hours.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.hours.suffix")));

                case MINUTES -> parsedRes.getLast().append(Component.translatable("klay_api.minutes.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.minutes.suffix")));

                case SECONDS -> parsedRes.getLast().append(Component.translatable("klay_api.seconds.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.seconds.suffix")));
            }
        }

        parsedRes.add(Component.translatable("master.inFloor"));

        parsedTexts = parsedRes;
    }

    @Unique
    public static void toChill(ItemEntity self) {
        if ( !(self.level() instanceof ServerLevel level) ) return;
        if (
                !self.getItem().getItem().getName().toString().contains("molasses_bottle")
        ) return;

        int itemCount = self.getItem().getCount();

        Vec3 pos = self.position();

//        Items.STONE_SWORD

        self.setItem(new ItemStack(ModItems.DRIED_MOLASSES, itemCount));

//        ItemEntity wow = new ItemEntity(level, pos.x, pos.y, pos.z + 0.1, new ItemStack(Items.GLASS_BOTTLE, itemCount));

//        level.addFreshEntity(wow);

//        VeggyCraft.LOGGER.warn("#POS: "+wow.position().toString());

    }
}