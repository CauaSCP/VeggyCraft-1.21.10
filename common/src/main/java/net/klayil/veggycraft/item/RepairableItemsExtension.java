package net.klayil.veggycraft.item;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RepairableItemsExtension extends Item implements CustomCraftingRemainder {
    protected final @Nullable Item repairItem;

    public @Nullable Item getRepairItem() {
        return repairItem;
    }

    @Override
    public boolean initialized() {
        return getInitialized();
    }

    public RepairableItemsExtension(Properties itemProperties, int maxDamage, Supplier<?> repairItemSupplier) {
        super(
                createSettings(
                        itemProperties, maxDamage, repairItemSupplier
                )
        );

        if (repairItemSupplier != null) {
            this.repairItem = (Item) ((Supplier<?>) repairItemSupplier.get()).get();

            return;
        }

        repairItem = null;

        init();
        setInitialized(true);
    }

    public static Properties createSettings(Properties itemProperties, int maxDamage, @Nullable Supplier<?> repairItemSupplier) {
        if (repairItemSupplier == null) {
            return itemProperties.durability(maxDamage);
        }

        Item repairItem = (Item) ((Supplier<?>) repairItemSupplier.get()).get();

//        VeggyCraft.LOGGER.info("#RepairItem: %s".formatted(repairItem));

        itemProperties = itemProperties.component(
                DataComponents.REPAIRABLE,
                new Repairable(
                        HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(repairItem))
                )
        );

        return itemProperties.durability(maxDamage);
    }

    /* *
    private final String flourBagItemEnding = "%s_items_stacked_of_flour";
    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        final String itemNameFromId = BuiltInRegistries.ITEM.getKey(
            player.getItemInHand(hand).getItem()
        ).getPath();

        if (!level.isClientSide()  && itemNameFromId.endsWith(flourBagItemEnding.formatted(""))) {
            int newStartingNum = Integer.parseInt(itemNameFromId.substring(0, 2));
            newStartingNum -= 8;

            final String newBagName = flourBagItemEnding.formatted(
                "%02d".formatted(newStartingNum)
            );

//            VeggyCraft.LOGGER.info("#TEST: "+newBagName);

            ItemStack newItemInHand = null;

            if (newStartingNum < 8) {
                newItemInHand = ItemStack.EMPTY;
            } else if (ResourceLocation.isValidPath(newBagName)) {
                newItemInHand = new ItemStack(BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, newBagName)));

                newItemInHand.setDamageValue(64 - newStartingNum + 1);
            }

            assert newItemInHand != null;
            player.setItemInHand(hand, newItemInHand);

            MinecraftServer server = ((ServerLevel) level).getServer();
            CommandSourceStack commandSourceStack = server.createCommandSourceStack().withPermission(4).withSuppressedOutput();

            server.getCommands().performPrefixedCommand(commandSourceStack, "give %s veggycraft:wheat_flour 8".formatted(player.getName().getString()));

        }

        return super.use(level, player, hand);
    }
    */
}

