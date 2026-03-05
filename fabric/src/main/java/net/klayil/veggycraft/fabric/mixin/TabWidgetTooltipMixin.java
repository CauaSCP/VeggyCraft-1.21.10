package net.klayil.veggycraft.fabric.mixin;
/* *
    final ArrayList<Component> toAdd = new ArrayList<>();
    int componentsTextIndex = tooltipBefore.entries().size() + 2;

    tooltipBefore.entries().forEach(entry -> {

    });

    for (int i = 0; i < tooltipBefore.entries().size(); i++) {
        Tooltip.Entry entry = tooltipBefore.entries().get(i);

        MutableComponent parsedEntry = Component.literal("");

        if (!toAdd.isEmpty()) {
            parsedEntry.append("");
        }

        if (!Stream.of(ChatFormatting.WHITE, arch$hereColor)
                .map(TextColor::fromLegacyFormat)
                .toList()
                .contains(entry.getAsText().getStyle().getColor())

            &&

                entry.getAsText().getString().equalsIgnoreCase(VeggyCraft.MOD_ID)
        ) {
            componentsTextIndex = Math.max(0, i - 2);
            parsedEntry.append(entry.getAsText());
            toAdd.add(parsedEntry);
            break;
        }

        if (entry.getAsText().getStyle().getColor() == TextColor.fromLegacyFormat(ChatFormatting.WHITE)) {
            continue;
        }

        parsedEntry.append(entry.getAsText().copy().withStyle(arch$hereColor));

        toAdd.add(parsedEntry);
    }

    for (int i = 0; i < toAdd.size(); i++) {
        if (i == componentsTextIndex) continue;

        tooltip.add(toAdd.get(i));
    }
*/




































import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.impl.client.gui.widget.TabWidget;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.klayil.veggycraft.fabric.client.DraggyParent;
import net.klayil.veggycraft.recipe.ReiIconTooltipImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
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
    @Final @Unique final ChatFormatting arch$hereColor = ChatFormatting.DARK_GRAY;

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

