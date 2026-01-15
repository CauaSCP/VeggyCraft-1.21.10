package net.klayil.veggycraft.item;

import net.klayil.veggycraft.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MolassesToBrownSugar {
    final static public int minutesToChill = 4;
    final static public int secondsToChill = 30;
    static public int itsTimeToChill = 5; // 60 * minutesToChill + secondsToChill;
    final static public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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