package net.klayil.veggycraft.recipe;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.recipes.ShapelessIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class JeiIcon extends ShapelessIcon {
    public final ImmutableRect2i area;
    private final ItemStack iconItem;

    public JeiIcon(IDrawable icon, ItemStack iconItem, int x, int y) {
        super(icon, x, y);

        this.iconItem = iconItem;
        this.area = new ImmutableRect2i(x, y, icon.getWidth(), icon.getHeight());
    }

    @Override
    public void addTooltip(JeiTooltip tooltip) {
        Minecraft instance = Minecraft.getInstance();

        tooltip.addAll(iconItem.getTooltipLines(Item.TooltipContext.of(instance.level), instance.player, TooltipFlag.NORMAL));
    }
}
