package net.klayil.veggycraft.item;

import lombok.SneakyThrows;
import net.klayil.InitDidNotRan;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;

public interface CustomCraftingRemainder extends ItemLike {
    boolean initialized();

    default boolean getInitialized() {
        return Initialized.real;
    }

    default void setInitialized(@NotNull Boolean willBe, Boolean... ignoreInBody) {
        Initialized.real = willBe;
    }

    class BooleanContainer {
        boolean real = false;
    }

    @Final BooleanContainer Initialized = new BooleanContainer();

    default void init() {
        setInitialized(true);
    }

    @Nullable final ItemStack result = null;

    @SneakyThrows
    default ItemStack getChangedCraftingRemainder(ItemStack stack) {
        boolean initialized = false;

        Object checkItem = this;

        if (checkItem instanceof Item) {
            initialized = this.initialized();
        } else {
            try {
                //noinspection all;
                Integer ignore = ((Integer) (Object) this);
            } catch (Exception e) {
                throw e.getClass().cast(new Exception("value to get crafting remainder isn't an Item"));
            }
        }

        if (!initialized) throw new InitDidNotRan(this.getClass().getSuperclass().getName());

        if (stack.isDamageableItem()) return __craftingRemainderResultDamaged(stack);

        if (
                !BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith(UnrepairableDamageableItems.FLOUR_ENDING)
        ) {
            return stack.getItem().getCraftingRemainder();
        }

        return stack.copy();
    }

    private static ItemStack __craftingRemainderResultDamaged(ItemStack stack) {
        ItemStack result = stack.copy();
        result.setDamageValue(stack.getDamageValue() + 1);

        if (result.getDamageValue() >= result.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        return result;
    }
}
