package net.klayil.veggycraft.recipe;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackProviderWidget;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Objects;

public class ReiIconTooltipImpl {
    public static class ArchApiMaster {
        public final ChatFormatting hereColor;
        public final Minecraft inst;

        public ArchApiMaster(ChatFormatting col, Minecraft mineInst) {
            hereColor = col;
            inst = mineInst;
        };
    }

    public static class WidMaster {
        DraggableStackProviderWidget draggableHolder;
        WidgetWithBounds boundsWidgetHolder;
        
        final public Rectangle bounds;
        
        public WidMaster(WidgetWithBounds bounded, DraggableStackProviderWidget draggable) {
            draggableHolder = draggable;
            boundsWidgetHolder = bounded;
            
            bounds = boundsWidgetHolder.getBounds();
        }

        public boolean isMouseOver(Number mouseX, Number mouseY) {
            return boundsWidgetHolder.isMouseOver(mouseX.doubleValue(), mouseY.doubleValue());
        }
        
        public DraggableStack getHoveredStack(DraggingContext<Screen> context, double mouseX, double mouseY) {
            return draggableHolder.getHoveredStack(context, mouseX, mouseY);
        }
    }
    
    static public <Arch extends ArchApiMaster, S extends Screen> void run(Tooltip tooltip, DisplayCategory<?> category, Arch arch, DraggingContext<S> screenContext, WidMaster wid) {


//        if (().getEntries().getFirst().getValue() instanceof ItemStack stack) {
//            VeggyCraft.LOGGER.warn("#Item Icon ID: %s", BuiltInRegistries.ITEM.getKey(stack.getItem()));
//        } else {
//            throw new AssertionError();
//        }


        ItemStack stack = null;

        if (wid.isMouseOver(wid.bounds.x, wid.bounds.y)) {

            EntryStack<?> entry = Objects.requireNonNull(wid.getHoveredStack(screenContext.cast(), wid.bounds.x, wid.bounds.y)).get();
            if (entry.getType() == VanillaEntryTypes.ITEM) {
                stack = entry.castValue();

                stack.getTooltipLines(Item.TooltipContext.of(arch.inst.level), arch.inst.player, TooltipFlag.NORMAL)
                        .forEach(component -> {
                            if (component.getStyle().getColor() != TextColor.fromLegacyFormat(ChatFormatting.WHITE))
                                tooltip.add(component);
                        });

//                Component comp = ((ItemMixinImpl) stack.getItem()).get$HoverCases().get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
//                tooltip.add(comp.copy().withStyle(arch.hereColor));
            }

            assert stack != null;
        }

/* *      ItemMixin hoverCasesHolder = new ItemMixin();
//        Map<ResourceLocation, Component> hoverCases = hoverCasesHolder.parsed$hoverCases.get();
//
//        ConsumersMore.Master tooltipMethod = hoverCasesHolder.methods.get("appendHoverText");
//
//        Minecraft inst = Minecraft.getInstance();
//
//        Supplier<?> v;
//
//        try {
//            v = tooltipMethod.acceptApplySupplierParser(tooltipMethod, Item.TooltipContext.of(inst.level), inst.player, TooltipFlag.ADVANCED);
//        } catch (NoSuchMethodException e) {
//            throw new NoSuchMethodException(e.getMessage());
//        }
//
//        VeggyCraft.LOGGER.warn("#Gotten: %s", v);
*/

        if (arch.inst.options.advancedItemTooltips) {
            tooltip.add(Component.literal(category.getIdentifier().toString()).withStyle(arch.hereColor));
        }

        if (ConfigObject.getInstance().shouldAppendModNames()) {
            tooltip.add(ClientHelper.getInstance().getFormattedModFromIdentifier(category.getIdentifier()));
        }

        tooltip.queue();
    }
}
