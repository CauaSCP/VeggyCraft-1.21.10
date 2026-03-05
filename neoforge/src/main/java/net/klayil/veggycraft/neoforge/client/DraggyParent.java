package net.klayil.veggycraft.neoforge.client;

import me.shedaniel.rei.impl.client.gui.dragging.CurrentDraggingStack;
import net.minecraft.client.gui.screens.Screen;

public class DraggyParent {
    public static class Draggy extends CurrentDraggingStack{
        static Draggy self;

        public Draggy() {
            self = this;
        }

        public static Screen getScreenStatic() {
            return self.getScreen();
        }
    }

}
