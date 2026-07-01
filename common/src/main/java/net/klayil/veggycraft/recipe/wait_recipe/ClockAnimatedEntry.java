package net.klayil.veggycraft.recipe.wait_recipe;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.klayil.veggycraft.recipe.MixedCategories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class ClockAnimatedEntry implements EntryRenderer<ItemStack> {
    @Override
    public void render(EntryStack<ItemStack> entryStack, GuiGraphics guiGraphics, Rectangle bounds, int x, int y, float v) {
        GlobalAnimationState.isREIRendering = true;

        GlobalAnimationState.fakeTime = (System.currentTimeMillis() % 1200L) / 1200f;

        int centerX = bounds.x + (bounds.width / 2) - 8;
        int centerY = bounds.y + (bounds.height / 2) - 8;

        guiGraphics.renderItem(entryStack.getValue(), centerX, centerY);

        GlobalAnimationState.isREIRendering = false;
    }

    @Override
    public Tooltip getTooltip(EntryStack<ItemStack> entryStack, TooltipContext tooltipContext) {
        return null;
    }
}
