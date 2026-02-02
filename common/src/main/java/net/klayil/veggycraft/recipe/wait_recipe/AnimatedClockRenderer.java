package net.klayil.veggycraft.recipe.wait_recipe;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AnimatedClockRenderer implements Renderer {
    private final ItemStack stack = new ItemStack(Items.CLOCK);

    @Override
    public void render(GuiGraphics guiGraphics, Rectangle bounds, int x, int y, float v) {
        GlobalAnimationState.isREIRendering = true;

        GlobalAnimationState.fakeTime = (System.currentTimeMillis() % 1200L) / 1200f;

        int centerX = bounds.x + (bounds.width / 2) - 8;
        int centerY = bounds.y + (bounds.height / 2) - 8;

        guiGraphics.renderItem(stack, centerX, centerY);

        GlobalAnimationState.isREIRendering = false;
    }

    @Override
    public Tooltip getTooltip(TooltipContext context) {
        return null;
    }
}
