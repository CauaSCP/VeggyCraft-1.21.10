package net.klayil.veggycraft.item;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RepairableItemsExtension extends Item {
    private final Item repairItem;

    public RepairableItemsExtension(Properties itemProperties, int maxDamage, Item repairItem) {
        super(
            createSettings(
                itemProperties, maxDamage, repairItem
            )
        );

        this.repairItem = repairItem;
    }

    private static Properties createSettings(Properties itemProperties, int maxDamage, Item repairItem) {
        itemProperties = itemProperties.component(
                DataComponents.REPAIRABLE,
                new Repairable(HolderSet.direct(Holder.direct(repairItem)))
        );


        return itemProperties.durability(maxDamage);
    }

    public Item getRepairItem() {
        return repairItem;
    }

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
}

