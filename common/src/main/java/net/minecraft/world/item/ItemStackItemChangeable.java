package net.minecraft.world.item;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.Container;

abstract public class ItemStackItemChangeable implements Container {
    protected ItemStackItemChangeable init() {
        return this;
    }

    @Getter @Setter
    private Item item;
}
