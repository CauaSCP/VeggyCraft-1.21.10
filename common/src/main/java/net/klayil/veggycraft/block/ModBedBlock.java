package net.klayil.veggycraft.block;


//import com.mojang.math.OctahedralGroup;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.entities.ModBedEntity;
import net.klayil.veggycraft.compat.BedCompat;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBedBlock extends BedBlock {
    @Nullable public Float awakenDamage;
    public ModBedEntity bedEntity;

    public boolean creativePlayerBroke = false;

    public static final Map<Direction, VoxelShape> SHAPES;
    public static final MapCodec<BedBlock> CODEC = BlockBehaviour.simpleCodec(ModBedBlock::new);

    public static final EnumProperty<BedPart> PART;
    public static final BooleanProperty OCCUPIED;

    static {
        PART = BlockStateProperties.BED_PART;
        OCCUPIED = BlockStateProperties.OCCUPIED;
        SHAPES = Util.make(() -> {
            VoxelShape voxelShape = Block.box(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 3.0F);
            VoxelShape voxelShape2 = Shapes.rotate(
                    voxelShape,
                    BedCompat.fromXYAngles(
                            Quadrant.R0,
                            Quadrant.R90
                    )
            );
            return Shapes.rotateHorizontal(Shapes.or(Block.column(16.0F, 3.0F, 9.0F), voxelShape, voxelShape2));
        });
    }

    public ModBedBlock(float awakenDamage, Properties properties) {
        super(DyeColor.LIME, properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
        this.awakenDamage = awakenDamage;
    }

    private ModBedBlock(Properties properties) {
        super(DyeColor.LIME, properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
        this.awakenDamage = 0.5f;
    }

    @Override
    public @NotNull MapCodec<BedBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        this.bedEntity = new ModBedEntity(pos, state);

         return this.bedEntity;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS_SERVER;
        } else {
            if (state.getValue(PART) != BedPart.HEAD) {
                pos = pos.relative(state.getValue(FACING));
                state = level.getBlockState(pos);
                if (!state.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(level)) {
                level.removeBlock(pos, false);
                BlockPos blockPos = pos.relative(state.getValue(FACING).getOpposite());
                if (level.getBlockState(blockPos).is(this)) {
                    level.removeBlock(blockPos, false);
                }

                Vec3 vec3 = pos.getCenter();
                level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return useWithoutItemHelper(player, level);
            } else if (state.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return useWithoutItemHelper(player, level);
            } else {
                player.startSleepInBed(pos).ifLeft((bedSleepingProblem) -> {
                    if (bedSleepingProblem.getMessage() != null) {
                        player.displayClientMessage(bedSleepingProblem.getMessage(), true);
                    }

                });
                return useWithoutItemHelper(player, level);
            }
        }
    }


    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.getFirst().stopSleeping();
            return true;
        }
    }


    private InteractionResult useWithoutItemHelper(Player player, Level level) {
        if (bedEntity.whoIsSleeping == null & !((level.getDayTime() % 24000 < 1000) & !(level.isThundering()))) {

            bedEntity.whoIsSleeping = player;

            ( (PlayerForSleepingBlock) player ).veggycraft$setSleepingBlock(this);
            bedEntity.whoIsSleeping.setSleepingPos(this.bedEntity.getBlockPos());


//            Runnable task = () -> {
//                if ((level.getDayTime() % 24000 < 1000) & !(level.isThundering())) {
////                    bedEntity.whoIsSleeping.stopSleeping();
//
//                    VeggyCraft.LOGGER.warn("#Awaken");
//
//                    scheduler.shutdown();
//                }
//            };
//
//            scheduler.scheduleWithFixedDelay(task, 0, 70, TimeUnit.MILLISECONDS);
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance * (double)0.5F);
    }

    public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityMovementAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }

    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < (double)0.0F) {
            double d = entity instanceof LivingEntity ? (double)1.0F : 0.8;
            entity.setDeltaMovement(vec3.x, -vec3.y * (double)0.66F * d, vec3.z);
        }

    }

    protected @NotNull BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART) ? state.setValue(OCCUPIED, neighborState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
        }
    }

    private static Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) creativePlayerBroke = true;

        if (!level.isClientSide()) {
            BedPart part = state.getValue(PART);
            Direction facing = state.getValue(FACING);

            BlockPos otherPos = pos.relative(getNeighbourDirection(part, facing));
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this) & otherState.getValue(PART) == BedPart.FOOT) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

//    @Override
//    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
//
//    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(direction);
        Level level = context.getLevel();
        return level.getBlockState(blockPos2).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockPos2) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(getConnectedDirection(state).getOpposite());
    }

    public static @NotNull Direction getConnectedDirection(BlockState state) {
        Direction direction = state.getValue(FACING);
        return state.getValue(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
    }

    private static boolean isBunkBed(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() instanceof ModBedBlock;
    }

    private static @NotNull Optional<Vec3> findStandUpPositionOptional(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos pos, Direction direction, float yRot) {
        Direction direction2 = direction.getClockWise();
        Direction direction3 = direction2.isFacingAngle(yRot) ? direction2.getOpposite() : direction2;
        if (isBunkBed(collisionGetter, pos)) {
            return findBunkBedStandUpPosition(entityType, collisionGetter, pos, direction, direction3);
        } else {
            int[][] is = bedStandUpOffsets(direction, direction3);
            Optional<Vec3> optional = findStandUpPositionAtOffset(entityType, collisionGetter, pos, is, true);
            return optional.isPresent() ? optional : findStandUpPositionAtOffset(entityType, collisionGetter, pos, is, false);
        }
    }

    public static @Nullable Vec3 findStandUpPositionMod(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos pos, Direction direction, float yRot) {
        Optional<Vec3> res = findStandUpPositionOptional(entityType, collisionGetter, pos, direction, yRot);

        VeggyCraft.LOGGER.info("#findStandUpPosMod");

        return res.orElse(null);
    }


        private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos pos, Direction stateFacing, Direction entityFacing) {
        int[][] is = bedSurroundStandUpOffsets(stateFacing, entityFacing);
        Optional<Vec3> optional = findStandUpPositionAtOffset(entityType, collisionGetter, pos, is, true);
        if (optional.isPresent()) {
            return optional;
        } else {
            BlockPos blockPos = pos.below();
            Optional<Vec3> optional2 = findStandUpPositionAtOffset(entityType, collisionGetter, blockPos, is, true);
            if (optional2.isPresent()) {
                return optional2;
            } else {
                int[][] js = bedAboveStandUpOffsets(stateFacing);
                Optional<Vec3> optional3 = findStandUpPositionAtOffset(entityType, collisionGetter, pos, js, true);
                if (optional3.isPresent()) {
                    return optional3;
                } else {
                    Optional<Vec3> optional4 = findStandUpPositionAtOffset(entityType, collisionGetter, pos, is, false);
                    if (optional4.isPresent()) {
                        return optional4;
                    } else {
                        Optional<Vec3> optional5 = findStandUpPositionAtOffset(entityType, collisionGetter, blockPos, is, false);
                        return optional5.isPresent() ? optional5 : findStandUpPositionAtOffset(entityType, collisionGetter, pos, js, false);
                    }
                }
            }
        }
    }

    private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> entityType, CollisionGetter collisionGetter, BlockPos pos, int[][] offsets, boolean simulate) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int[] is : offsets) {
            mutableBlockPos.set(pos.getX() + is[0], pos.getY(), pos.getZ() + is[1]);
            Vec3 vec3 = DismountHelper.findSafeDismountLocation(entityType, collisionGetter, mutableBlockPos, simulate);
            if (vec3 != null) {
                return Optional.of(vec3);
            }
        }

        return Optional.empty();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            BlockPos blockPos = pos.relative(state.getValue(FACING));
            level.setBlock(blockPos, state.setValue(PART, BedPart.HEAD), 3);
            level.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }

    }

    protected long getSeed(BlockState state, BlockPos pos) {
        BlockPos blockPos = pos.relative(
                state.getValue(FACING),
                state.getValue(PART) == BedPart.HEAD ? 0 : 1
        );

        return blockPos.asLong();
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    private static int[][] bedStandUpOffsets(Direction firstDir, Direction secondDir) {
        return ArrayUtils.addAll(bedSurroundStandUpOffsets(firstDir, secondDir), bedAboveStandUpOffsets(firstDir));
    }

    private static int[][] bedSurroundStandUpOffsets(Direction firstDir, Direction secondDir) {
        return new int[][]{{secondDir.getStepX(), secondDir.getStepZ()}, {secondDir.getStepX() - firstDir.getStepX(), secondDir.getStepZ() - firstDir.getStepZ()}, {secondDir.getStepX() - firstDir.getStepX() * 2, secondDir.getStepZ() - firstDir.getStepZ() * 2}, {-firstDir.getStepX() * 2, -firstDir.getStepZ() * 2}, {-secondDir.getStepX() - firstDir.getStepX() * 2, -secondDir.getStepZ() - firstDir.getStepZ() * 2}, {-secondDir.getStepX() - firstDir.getStepX(), -secondDir.getStepZ() - firstDir.getStepZ()}, {-secondDir.getStepX(), -secondDir.getStepZ()}, {-secondDir.getStepX() + firstDir.getStepX(), -secondDir.getStepZ() + firstDir.getStepZ()}, {firstDir.getStepX(), firstDir.getStepZ()}, {secondDir.getStepX() + firstDir.getStepX(), secondDir.getStepZ() + firstDir.getStepZ()}};
    }

    private static int[][] bedAboveStandUpOffsets(Direction dir) {
        return new int[][]{{0, 0}, {-dir.getStepX(), -dir.getStepZ()}};
    }
}
