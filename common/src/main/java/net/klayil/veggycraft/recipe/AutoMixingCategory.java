package net.klayil.veggycraft.recipe;


import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
//import me.shedaniel.rei.plugin.common.displays.crafting.CraftingDisplay;
import net.klayil.veggycraft.VeggyCraft;
//import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.item.Items;
//import net.minecraft.text.Text;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AutoMixingCategory implements DisplayCategory<BasicDisplay> {
    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return PISTON_SMASH;
    }

    public static final CategoryIdentifier<BasicDisplay> PISTON_SMASH =
            CategoryIdentifier.of(VeggyCraft.MOD_ID, "piston_smash");

    @Override
    public Component getTitle() {
        return Component.translatable("klay_api.smash.predicate").append(Component.translatable("block.minecraft.piston")).append(Component.translatable("klay_api.smash.wan"));
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.PISTON.asItem().getDefaultInstance());
    }


    public record MixingBasinRenderState(Matrix3x2f pose, int x1, int y1, ScreenRectangle bounds) implements GuiElementRenderState {
        public MixingBasinRenderState(Matrix3x2f pose, int x, int y) {
            this(pose, x, y, new ScreenRectangle(x, y, 30, 80).transformMaxBounds(pose));
        }

        public int x2() {
            return x1 + 30;
        }

        public int y2() {
            return y1 + 80;
        }

        public float scale() {
            return 23;
        }

        @Override
        public void buildVertices(VertexConsumer consumer) {

        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipelines.GUI;
        }

        @Override
        public @NotNull TextureSetup textureSetup() {
            return TextureSetup.noTexture();
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return null;
        }
    }

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID,"textures/gui/recipe/arro.png");

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

//        int xv = 86;
//        int yv = 35;
        Widget texturedWidget = Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 176, 82));

//        VeggyCraft.LOGGER.info("#TEXTURE: %s | %s".formatted(TEXTURE.toDebugFileName(), texturedWidget.toString()));

        widgets.add(texturedWidget);

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54-6, startPoint.y + 33))
                .entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54-6+64, startPoint.y + 33))
                .entries(display.getOutputEntries().get(0)).markOutput());

        return widgets;
    }


    public static List<EntryIngredient> condenseIngredients(List<EntryIngredient> ingredients) {
        List<ItemStack> cache = new ArrayList<>();
        List<EntryIngredient> result = new ArrayList<>();
        Find:
        for (EntryIngredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }
            int size = ingredient.size();
            if (size != 1) {
                result.add(ingredient);
                continue;
            }
            EntryStack<?> entryStack = ingredient.getFirst();
            if (!(entryStack.getValue() instanceof ItemStack stack)) {
                result.add(ingredient);
                continue;
            }
            for (ItemStack target : cache) {
                if (ItemStack.isSameItem(stack, target)) {
                    target.grow(stack.getCount());
                    continue Find;
                }
            }
            stack = stack.copy();
            cache.add(stack);
            result.add(EntryIngredients.of(stack));
        }
        return result;
    }
//    @Override
//    public void addWidgets(List<Widget> widgets, CraftingDisplay display, Rectangle bounds) {
//        List<EntryIngredient> ingredients = condenseIngredients(display.getInputEntries());
//        List<Point> points = new ArrayList<>();
//        for (int i = 0, size = ingredients.size(), xOffset = size < 3 ? (3 - size) * 19 / 2 : 0; i < size; i++) {
//            points.add(new Point(bounds.x + 17 + xOffset + (i % 3) * 19, bounds.y + 56 - (i / 3) * 19));
//        }
//        Point output = new Point(bounds.x + 147, bounds.y + 56);
//        widgets.add(Widgets.createDrawableWidget(( GuiGraphics graphics, int mouseX, int mouseY, float delta) -> {
////            drawSlotBackground(graphics, points, output);
////            AllGuiTextures.JEI_DOWN_ARROW.render(graphics, bounds.x + 141, bounds.y + 37);
////            AllGuiTextures.JEI_SHADOW.render(graphics, bounds.x + 86, bounds.y + 73);
////            graphics.
//
//            DrawContext context = new DrawContext(Minecraft.getInstance(), graphics.pose(), new GuiRenderState());
//
//                context.addSpecialElement(new MixingBasinRenderState(new Matrix3x2f(graphics.pose()), 91, -8));
//
////            graphics.state.addSpecialElement(
//            new MixingBasinRenderState(new Matrix3x2f(graphics.pose()), bounds.x + 96, bounds.y
////            )
//            );
//        }));
//        for (int i = 0, size = points.size(); i < size; i++) {
//
//
//            widgets.add(Widgets.createSlot(points.get(i)).markInput().disableBackground().entries(ingredients.get(i)));
//        }
//        widgets.add( (Widgets.createSlot(output).markOutput().disableBackground()).entries(display.getOutputEntries().getFirst()));
//    }

    @Override
    public int getDisplayHeight() {
        return 95;
    }


}
