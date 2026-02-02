package net.klayil.veggycraft.recipe;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.Identifiable;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.klayil.veggycraft.recipe.wait_recipe.AnimatedClockRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import me.shedaniel.rei.api.common.util.EntryStacks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MixedCategories implements DisplayCategory<BasicDisplay> {
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
        if (ctg == HAVE_TO_WAIT) return new AnimatedClockRenderer();

        assert ctg == PISTON_SMASH;
        return EntryStacks.of(Blocks.PISTON.asItem().getDefaultInstance());
    }

    static final ResourceLocation TEXTURE = PistonSmashRecipe.TEXTURE;

    Rectangle bg_rect = null;

    List<Widget> setupDisplayMaster(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        bg_rect = new Rectangle(startPoint.x+4-11, startPoint.y+3, 169, 80);

        Widget background = Widgets.createTexturedWidget(TEXTURE, bg_rect, 4, 3);
        Widget background_plus = Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x+119-11, startPoint.y+30, 26, 26), 146, 30);

        widgets.add(background);

        widgets.add(background_plus);

        int[] xs = {28-11, 44-11, 64-11};
        int[] ys = {16, 33, 44};
        for (int x : xs) {
            for (int y : ys) {
                widgets.add(
                        Widgets.createTexturedWidget(
                                TEXTURE,
                                new Rectangle(startPoint.x+x, startPoint.y+y, 26, 26),
                                146, 30
                        )
                );
            }
        }

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54-6, startPoint.y + 33+3))
                .entries(display.getInputEntries().getFirst()).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54-6+64+4, startPoint.y + 33+3))
                .entries(display.getOutputEntries().getFirst()).markOutput());

        return widgets;
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        if(ctg == HAVE_TO_WAIT) {
            List<Widget> widgetsRes = setupDisplayMaster(display, bounds);

            int enMoreVert = 0;

            assert parsedTexts != null;
            for (Component parsedText : parsedTexts) {
                Label label = Widgets.createLabel(
                        new Point(bounds.getCenterX(), bg_rect.getY() + bg_rect.getHeight() - 27 + enMoreVert),

                        parsedText.copy()
                );

                label.centered();
                label.color(0xFFFFFFFF);

                widgetsRes.add(label);

                enMoreVert += 10;
            }

            return widgetsRes;
        }

        return setupDisplayMaster(display, bounds);
    }

    @Override
    public int getDisplayHeight() {
        return 95;
    }

    public MixedCategories(CategoryIdentifier<BasicDisplay> ctgParam) {
        assert ctgParam == PISTON_SMASH;
        ctg = ctgParam;
    }

    private static List<MutableComponent> parsedTexts = null;
    private boolean firstForSpace = true;

    public MixedCategories(@Nullable Identifiable ignore, @NotNull Map<TimeUnit, Integer> waitMapTime) {
        ctg = HAVE_TO_WAIT;

        parsedTexts = new ArrayList<>(List.of(Component.translatable("klay_api.wait.for").append(" ")));

        for (TimeUnit keyForTextForLabel : List.of(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)) {
            if (!waitMapTime.containsKey(keyForTextForLabel)) continue;

            if (!firstForSpace) parsedTexts.add(Component.literal("& "));
            firstForSpace = false;

            String parsedNumber = "%d".formatted(waitMapTime.get(keyForTextForLabel));
            switch (keyForTextForLabel) {
                case HOURS -> parsedTexts.getLast().append(Component.translatable("klay_api.hours.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.hours.suffix")));

                case MINUTES -> parsedTexts.getLast().append(Component.translatable("klay_api.minutes.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.minutes.suffix")));

                case SECONDS -> parsedTexts.getLast().append(Component.translatable("klay_api.seconds.prefix").append(parsedNumber)
                        .append(Component.translatable("klay_api.seconds.suffix")));
            }
        }

        parsedTexts.add(Component.translatable("master.inFloor"));
    }
}
