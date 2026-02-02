package net.klayil.veggycraft.block.entities;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ModBedEntity extends BlockEntity {
    @Nullable public Player whoIsSleeping = null;

    public ModBedEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.STRAW_BED.get(), pos, blockState);
    }

//    public static class Ticker<T extends BlockEntity> implements BlockEntityTicker<T> {
//        Integer slept = null;
//
//        @Override
//        public void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
//            if (level.isClientSide()) return;
//
//            if (!(blockState.getBlock() instanceof ModBedBlock modBedBlock)) return;
//
//            if (modBedBlock.bedEntity == null) return;
//
//            if (modBedBlock.bedEntity.whoIsSleeping == null) return;
//
//            if (modBedBlock.bedEntity.whoIsSleeping instanceof Player whoIsSleeping) {
//                if (slept == null) {
//
//                    slept = 0;
//                    return;
//
//                }
//
//                if (slept >= (whoIsSleeping.SLEEP_DURATION - 20)) {
////                    DamageSource damageSource = new DamageSource(
////                            level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
////                                    .get(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(
////                                            VeggyCraft.MOD_ID,
////                                            "itchy_bed"
////                                    ))).get()
////                    );
//
//                    if (modBedBlock.bedEntity.whoIsSleeping != null & (level.getDayTime() % 24000 < 1000) & !(level.isThundering())) {
//
//                        modBedBlock.bedEntity.whoIsSleeping.setSleepingPos(blockPos);
//                        modBedBlock.bedEntity.whoIsSleeping.stopSleeping(); // line 76
//                        ((ModBedBlock) blockState.getBlock()).bedEntity.whoIsSleeping = null;
//
//                    }
//
//                }
//
//                slept++;
//
//                return;
//            }
//
//            slept = null;
//        }
//    }

}
