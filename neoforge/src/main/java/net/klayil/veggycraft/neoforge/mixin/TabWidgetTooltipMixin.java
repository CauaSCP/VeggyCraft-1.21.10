package net.klayil.veggycraft.neoforge.mixin;

import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.impl.client.gui.widget.TabWidget;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.klayil.veggycraft.neoforge.client.DraggyParent;
import net.klayil.veggycraft.recipe.ReiIconTooltipImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = TabWidget.class, remap = false)
public abstract class TabWidgetTooltipMixin {
    @Shadow(remap = false) public Component categoryName;
    @Shadow(remap = false) public DisplayCategory<?> category;

    @Shadow(remap = false) public Renderer renderer;

    @Unique Minecraft arch$inst = Minecraft.getInstance();
    @Unique ChatFormatting arch$hereColor = ChatFormatting.DARK_GRAY;

    @Inject(method = "drawTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    private void addMyCustomText(CallbackInfo ci)  {
        Tooltip tooltipBefore = renderer.getTooltip(TooltipContext.of(Item.TooltipContext.of(arch$inst.level)));

        if (tooltipBefore == null) return;

        Tooltip tooltip = Tooltip.create(categoryName);

        ReiIconTooltipImpl.run(
                tooltip, category,

                new ReiIconTooltipImpl.ArchApiMaster(arch$hereColor, arch$inst),
                new DraggyParent.Draggy(),
                new ReiIconTooltipImpl.WidMaster((WidgetWithBounds) (Object) this, (DraggableStackProviderWidget) this)
        );

        ci.cancel();
    }
}
