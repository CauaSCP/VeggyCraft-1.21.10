package net.klayil.veggycraft.recipe;

import net.klayil.veggycraft.item.ModItems;
import net.minecraft.Util;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.Identifiable;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import me.shedaniel.rei.api.common.util.EntryStacks;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.klayil.veggycraft.VeggyCraft.comment;

public class MixedCategoriesRei implements DisplayCategory<BasicDisplay> {
    final private CategoryIdentifier<BasicDisplay> ctg;

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ctg;
    }

    public static final CategoryIdentifier<BasicDisplay> PISTON_SMASH =
            CategoryIdentifier.of(VeggyCraft.MOD_ID, "piston_smash");

    public static final CategoryIdentifier<BasicDisplay> HAVE_TO_WAIT =
            CategoryIdentifier.of(VeggyCraft.MOD_ID, "have_to_wait");

    @Override
    public Component getTitle() {
        if (ctg == PISTON_SMASH) return Component.translatable("klay_api.smash.predicate").append(Component.translatable("block.minecraft.piston")).append(Component.translatable("klay_api.smash.wan"));
        else if (ctg == HAVE_TO_WAIT) return Component.translatable("klay_api.have.to.wait");

        throw new IllegalArgumentException("identified category of /%s:%s\\ does not exist".formatted(ctg.getNamespace(), ctg.getPath()));
    }

    @Override
    public Renderer getIcon() {
        if (ctg == HAVE_TO_WAIT) {
//            TabWidget

//            EntryStack<ItemStack> modClockStack = EntryStacks.of(ModItems.THIS_MOD_CLOCK.get()).copy().setting(EntryStack.Settings.RENDERER, stack -> new CustomClockReiIconEntry()); // EntryStacks.of(new ItemStack(ModItems.THIS_MOD_CLOCK));
//            assert MolassesToBrownSugar.parsedTexts != null;
//            modClockStack = modClockStack.tooltip(new ArrayList<>(MolassesToBrownSugar.parsedTexts));
//
//            return modClockStack;

            return EntryStacks.of(ModItems.THIS_MOD_CLOCK.get().getDefaultInstance());
        }

        assert ctg == PISTON_SMASH : "identified category of /%s:%s\\ does not exist".formatted(ctg.getNamespace(), ctg.getPath());
        return EntryStacks.of(Blocks.PISTON.asItem().getDefaultInstance());
    }

    static final ResourceLocation TEXTURE = PistonSmashRecipe.TEXTURE;

    Rectangle bg_rect = null;

    Point startPoint;
    List<Widget> setupDisplayMaster(BasicDisplay display, Rectangle bounds) {
        startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        bg_rect = new Rectangle(bounds.getCenterX() - (22 / 2), startPoint.y + 36, 22, 15);

        Widget background = Widgets.createTexturedWidget(TEXTURE, bg_rect, 90, 35); // v: 35
        widgets.add(background);

        comment("""
                /*
                        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
                        List<Widget> widgets = new LinkedList<>();
                
                        bg_rect = new Rectangle(bounds.getCenterX(), startPoint.y+35, 22, 15);
                
                        Widget background = Widgets.createTexturedWidget(TEXTURE, bg_rect, 90, 35); // v: 35
                         */
                
                        // Widget background_plus = Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x+119-11, startPoint.y+30, 26, 26), 146, 30);

                //        widgets.add(background_plus);
                
                //        int[] xs = {28-11, 44-11, 64-11};
                //        int[] ys = {16, 33, 44};
                //        for (int x : xs) {
                //            for (int y : ys) {
                //                widgets.add(
                //                        Widgets.createTexturedWidget(
                //                                TEXTURE,
                //                                new Rectangle(startPoint.x+x, startPoint.y+y, 26, 26),
                //                                146, 30
                //                        )
                //                );
                //            }
                //        }
""");

        Util.make(Widgets.createSlot(new Point(bounds.getCenterX() - 43, startPoint.y + 36))
            .entries(display.getInputEntries().getFirst()).markInput(),
        slot -> {
                craftSlotBounds = slot.getBounds();

                widgets.add(slot);
            }
        );

        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() + 27, startPoint.y + 36))
                .entries(display.getOutputEntries().getFirst()).markOutput());

        return widgets;
    }

    private Rectangle craftSlotBounds;
    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        List<Widget> widgetsRes = setupDisplayMaster(display, bounds);
        assert startPoint != null;

        if(ctg == HAVE_TO_WAIT) {
            int enMoreVert = 0;

            assert parsedTextsPrivate != null;

            boolean first = true;

            for (Component parsedText : parsedTextsPrivate) {
                Label label = Widgets.createLabel(
                        new Point(
                                bounds.getCenterX(),
                                craftSlotBounds.y + craftSlotBounds.height + 6 + enMoreVert
                        ),

                        parsedText.copy()
                );

                label.centered();
                label.color(0xFFFFFFFF);

                if (first) {
                    VeggyCraft.LOGGER.warn("#firstPos: %d", label.getY());

                    first = false;
                }

                widgetsRes.add(label);

                enMoreVert += 10;
            }
        }

        return widgetsRes;
    }

    @Override
    public int getDisplayHeight() {
        return 112;
    }

    public MixedCategoriesRei(CategoryIdentifier<BasicDisplay> ctgParam) {
        assert ctgParam == PISTON_SMASH;
        ctg = ctgParam;
    }


    static List<MutableComponent> parsedTextsPrivate;

    public MixedCategoriesRei(@Nullable Identifiable ignore, List<MutableComponent> textList) {
        ctg = HAVE_TO_WAIT;

        parsedTextsPrivate = textList.stream().toList();
    }
}
