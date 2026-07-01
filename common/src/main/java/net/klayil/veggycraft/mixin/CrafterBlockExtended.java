package net.klayil.veggycraft.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeCache;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrafterBlockExtended extends CrafterBlock {
    public EnumProperty<FrontAndTop> ORIENTATION;

    public static final MapCodec<CrafterBlock> CODEC = simpleCodec(CrafterBlock::new);
    public static final BooleanProperty CRAFTING;
    public static final BooleanProperty TRIGGERED;
    private static final int MAX_CRAFTING_TICKS = 6;
    private static final int CRAFTING_TICK_DELAY = 4;
    private static final RecipeCache RECIPE_CACHE;
    private static final int CRAFTER_ADVANCEMENT_DIAMETER = 17;

    public CrafterBlockExtended(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(ORIENTATION, FrontAndTop.NORTH_UP)).setValue(TRIGGERED, false)).setValue(CRAFTING, false));
    }

    public FrontAndTop getDirectionValue() {
        return this.getStateForPlacement(null).getValue(ORIENTATION);
    }

    @Override
    protected MapCodec<CrafterBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            return crafterBlockEntity.getRedstoneSignal();
        } else {
            return 0;
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        boolean bl = level.hasNeighborSignal(pos);
        boolean bl2 = (Boolean)state.getValue(TRIGGERED);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (bl && !bl2) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, true), 2);
            this.setBlockEntityTriggered(blockEntity, true);
        } else if (!bl && bl2) {
            level.setBlock(pos, (BlockState)((BlockState)state.setValue(TRIGGERED, false)).setValue(CRAFTING, false), 2);
            this.setBlockEntityTriggered(blockEntity, false);
        }

    }

    private void setBlockEntityTriggered(@Nullable BlockEntity blockEntity, boolean triggered) {
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            crafterBlockEntity.setTriggered(triggered);
        }

    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.dispenseFrom(state, level, pos);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, BlockEntityType.CRAFTER, CrafterBlockEntity::serverTick);
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(pos, state);
        crafterBlockEntity.setTriggered(state.hasProperty(TRIGGERED) && (Boolean)state.getValue(TRIGGERED));
        return crafterBlockEntity;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();
        Direction var10000;
        switch (direction) {
            case DOWN:
                var10000 = context.getHorizontalDirection().getOpposite();
                break;
            case UP:
                var10000 = context.getHorizontalDirection();
                break;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                var10000 = Direction.UP;
                break;
            default:
                throw new MatchException((String)null, (Throwable)null);
        }

        Direction direction2 = var10000;
        return (BlockState)((BlockState)this.defaultBlockState().setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction2))).setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if ((Boolean)state.getValue(TRIGGERED)) {
            level.scheduleTick(pos, this, 4);
        }

    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity var7 = level.getBlockEntity(pos);
            if (var7 instanceof CrafterBlockEntity) {
                CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)var7;
                player.openMenu(crafterBlockEntity);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void dispenseFrom(BlockState state, ServerLevel level, BlockPos pos) {
        BlockEntity craftingInput = level.getBlockEntity(pos);
        if (craftingInput instanceof CrafterBlockEntity crafterBlockEntity) {
            CraftingInput var11 = crafterBlockEntity.asCraftInput();
            Optional<RecipeHolder<CraftingRecipe>> optional = getPotentialResults(level, var11);
            if (optional.isEmpty()) {
                level.levelEvent(1050, pos, 0);
            } else {
                RecipeHolder<CraftingRecipe> recipeHolder = (RecipeHolder)optional.get();
                ItemStack itemStack = ((CraftingRecipe)recipeHolder.value()).assemble(var11, level.registryAccess());
                if (itemStack.isEmpty()) {
                    level.levelEvent(1050, pos, 0);
                } else {
                    crafterBlockEntity.setCraftingTicksRemaining(6);
                    level.setBlock(pos, (BlockState)state.setValue(CRAFTING, true), 2);
                    itemStack.onCraftedBySystem(level);
                    this.dispenseItem(level, pos, crafterBlockEntity, itemStack, state, recipeHolder);

                    for(ItemStack itemStack2 : ((CraftingRecipe)recipeHolder.value()).getRemainingItems(var11)) {
                        if (!itemStack2.isEmpty()) {
                            this.dispenseItem(level, pos, crafterBlockEntity, itemStack2, state, recipeHolder);
                        }
                    }

                    crafterBlockEntity.getItems().forEach((itemStackx) -> {
                        if (!itemStackx.isEmpty()) {
                            itemStackx.shrink(1);
                        }
                    });
                    crafterBlockEntity.setChanged();
                }
            }
        }
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(ORIENTATION, rotation.rotation().rotate((FrontAndTop)state.getValue(ORIENTATION)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return (BlockState)state.setValue(ORIENTATION, mirror.rotation().rotate((FrontAndTop)state.getValue(ORIENTATION)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{ORIENTATION, TRIGGERED, CRAFTING});
    }

    static {
        CRAFTING = BlockStateProperties.CRAFTING;
        TRIGGERED = BlockStateProperties.TRIGGERED;
        RECIPE_CACHE = new RecipeCache(10);
    }

    private void dispenseItem(ServerLevel level, BlockPos pos, CrafterBlockEntity crafter, ItemStack stack, BlockState state, RecipeHolder<?> recipe) {
        Direction direction = ((FrontAndTop)state.getValue(ORIENTATION)).front();
        Container container = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
        ItemStack itemStack = stack.copy();
        if (container != null && (container instanceof CrafterBlockEntity || stack.getCount() > container.getMaxStackSize(stack))) {
            while(!itemStack.isEmpty()) {
                ItemStack itemStack2 = itemStack.copyWithCount(1);
                ItemStack itemStack3 = HopperBlockEntity.addItem(crafter, container, itemStack2, direction.getOpposite());
                if (!itemStack3.isEmpty()) {
                    break;
                }

                itemStack.shrink(1);
            }
        } else if (container != null) {
            while(!itemStack.isEmpty()) {
                int i = itemStack.getCount();
                itemStack = HopperBlockEntity.addItem(crafter, container, itemStack, direction.getOpposite());
                if (i == itemStack.getCount()) {
                    break;
                }
            }
        }

        if (!itemStack.isEmpty()) {
            Vec3 vec3 = Vec3.atCenterOf(pos);
            Vec3 vec32 = vec3.relative(direction, 0.7);
            DefaultDispenseItemBehavior.spawnItem(level, itemStack, 6, direction, vec32);

            for(ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(vec3, (double)17.0F, (double)17.0F, (double)17.0F))) {
                CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(serverPlayer, recipe.id(), crafter.getItems());
            }

            level.levelEvent(1049, pos, 0);
            level.levelEvent(2010, pos, direction.get3DDataValue());
        }

    }
}
