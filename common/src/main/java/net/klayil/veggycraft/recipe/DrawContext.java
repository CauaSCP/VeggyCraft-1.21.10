package net.klayil.veggycraft.recipe;


import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.ColoredRectangleRenderState;
import net.minecraft.client.gui.render.state.*;
import net.minecraft.client.gui.render.state.pip.GuiBannerResultRenderState;
import net.minecraft.client.gui.render.state.pip.GuiBookModelRenderState;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.gui.render.state.pip.GuiProfilerChartRenderState;
import net.minecraft.client.gui.render.state.pip.GuiSignRenderState;
import net.minecraft.client.gui.render.state.pip.GuiSkinRenderState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.AtlasIds;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ResultField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import net.minecraft.client.gui.render.state.GuiElementRenderState;


import net.minecraft.client.gui.navigation.ScreenRectangle;

//import static com.sun.tools.javac.jvm.ByteCodes.invokedynamic;

public class DrawContext extends GuiGraphics {
    private static final int EXTRA_SPACE_AFTER_FIRST_TOOLTIP_LINE = 2;
    private final Minecraft minecraft;
    public Matrix3x2fStack pose;
    public Matrix3x2fStack getMatrices;
    private final ScissorStack scissorStack;
    private final MaterialSet materials;
    private final TextureAtlas guiSprites;
    public GuiRenderState guiRenderState;
    public GuiRenderState State;
    private CursorType pendingCursor;
    @Nullable
    private Runnable deferredTooltip;
    private final List<OutlineBox> deferredOutlines;
    private Layer currentLayer;
    private ArrayList<Object> rootLayers;
    ScreenRectangle currentLayerBounds;


    public static class Layer {
        @Nullable
        public final Layer parent;
        @Nullable
        public Layer up;
        @Nullable
        public List<GuiElementRenderState> simpleElementRenderStates;
        @Nullable
        public List<GuiElementRenderState> preparedTextElementRenderStates;
        @Nullable
        public List<GuiElementRenderState> itemElementRenderStates;
        @Nullable
        public List<GuiElementRenderState> textElementRenderStates;
        @Nullable
        public List<GuiElementRenderState> specialElementRenderStates;

        Layer(@Nullable Layer parent) {
            this.parent = parent;
        }

        public void addItem(GuiElementRenderState state) {
            if (this.itemElementRenderStates == null) {
                this.itemElementRenderStates = new ArrayList();
            }

            this.itemElementRenderStates.add(state);
        }

        public void addText(GuiElementRenderState state) {
            if (this.textElementRenderStates == null) {
                this.textElementRenderStates = new ArrayList();
            }

            this.textElementRenderStates.add(state);
        }

        public void addSpecialElement(GuiElementRenderState state) {
            if (this.specialElementRenderStates == null) {
                this.specialElementRenderStates = new ArrayList();
            }

            this.specialElementRenderStates.add(state);
        }

        public void addSimpleElement(GuiElementRenderState state) {
            if (this.simpleElementRenderStates == null) {
                this.simpleElementRenderStates = new ArrayList();
            }

            this.simpleElementRenderStates.add(state);
        }

        public void addPreparedText(GuiElementRenderState state) {
            if (this.preparedTextElementRenderStates == null) {
                this.preparedTextElementRenderStates = new ArrayList();
            }

            this.preparedTextElementRenderStates.add(state);
        }
    }


    boolean findAndGoToLayerToAdd(GuiElementRenderState state) {
        ScreenRectangle screenRect = state.bounds();
        if (screenRect == null) {
            return false;
        } else {
            if (this.currentLayerBounds != null && this.currentLayerBounds.containsPoint(screenRect.position().x(), screenRect.position().y())) {
                this.goUpLayer();
            } else {
                this.findAndGoToLayerIntersecting(screenRect);
            }

            this.currentLayerBounds = screenRect;
            return true;
        }
    }

    private boolean anyIntersect(ScreenRectangle bounds, @Nullable List<? extends GuiElementRenderState> elementRenderStates) {
        if (elementRenderStates != null) {
            for(GuiElementRenderState guiElementRenderState : elementRenderStates) {
                ScreenRectangle screenRect = guiElementRenderState.bounds();
                if (screenRect != null && screenRect.intersects(bounds)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void findAndGoToLayerIntersecting(ScreenRectangle bounds) {
        Layer layer;

//        this.currentLayer = new Layer((Layer)null);
        this.rootLayers.add(this.currentLayer);

        for(layer = (Layer)this.rootLayers.getLast(); layer.up != null; layer = layer.up) {
        }

        boolean bl = false;

        while(!bl) {
            bl = this.anyIntersect(bounds, layer.simpleElementRenderStates) || this.anyIntersect(bounds, layer.itemElementRenderStates) || this.anyIntersect(bounds, layer.textElementRenderStates) || this.anyIntersect(bounds, layer.specialElementRenderStates);
            if (layer.parent == null) {
                break;
            }

            if (!bl) {
                layer = layer.parent;
            }
        }

        this.currentLayer = layer;
        if (bl) {
            this.goUpLayer();
        }

    }


    void addSpecialElement(GuiElementRenderState state) {
        guiRenderState = (GuiRenderState) state;

        if (this.findAndGoToLayerToAdd(state)) {
            this.currentLayer.addSimpleElement(state);
            this.onElementAdded(state.bounds());
        }

    }

    private void onElementAdded(@Nullable ScreenRectangle bounds) {
        if (SharedConstants.DEBUG_RENDER_UI_LAYERING_RECTANGLES && bounds != null) {
            this.goUpLayer();
            this.currentLayer.addSimpleElement(
                new ColoredRectangleRenderState(RenderPipelines.GUI, TextureSetup.noTexture(), new Matrix3x2f(), 0, 0, 10_000, 10_000, 2000962815, 2000962815, bounds)
            );
        }
    }

    private void goUpLayer() {
        if (this.currentLayer.up == null) {
            this.currentLayer.up = new Layer(this.currentLayer);
        }

        this.currentLayer = this.currentLayer.up;
    }

    public DrawContext(Minecraft minecraft, Matrix3x2fStack pose, GuiRenderState guiRenderState) {
        super(minecraft, guiRenderState);
        this.pose = pose;
        this.getMatrices = this.pose;
        this.minecraft = minecraft;
        this.guiRenderState = guiRenderState;
        this.State = guiRenderState;

        this.scissorStack = new ScissorStack();
        this.pendingCursor = CursorType.DEFAULT;
        this.deferredOutlines = new ArrayList();
//        this.minecraft = minecraft;
        AtlasManager atlasManager = minecraft.getAtlasManager();
        this.materials = atlasManager;
        this.guiSprites = atlasManager.getAtlasOrThrow(AtlasIds.GUI);
//        this.guiRenderState = guiRenderState;
    }

    public void requestCursor(CursorType cursor) {
        this.pendingCursor = cursor;
    }

    public void applyCursor(Window window) {
        window.selectCursor(this.pendingCursor);
    }

    public int guiWidth() {
        return this.minecraft.getWindow().getGuiScaledWidth();
    }

    public int guiHeight() {
        return this.minecraft.getWindow().getGuiScaledHeight();
    }

    public void nextStratum() {
        this.guiRenderState.nextStratum();
    }

    public void blurBeforeThisStratum() {
        this.guiRenderState.blurBeforeThisStratum();
    }

    public Matrix3x2fStack pose() {
        return this.pose;
    }

    public void hLine(int minX, int maxX, int y, int color) {
        if (maxX < minX) {
            int i = minX;
            minX = maxX;
            maxX = i;
        }

        this.fill(minX, y, maxX + 1, y + 1, color);
    }

    public void vLine(int x, int minY, int maxY, int color) {
        if (maxY < minY) {
            int i = minY;
            minY = maxY;
            maxY = i;
        }

        this.fill(x, minY + 1, x + 1, maxY, color);
    }

    public void enableScissor(int minX, int minY, int maxX, int maxY) {
        ScreenRectangle screenRectangle = (new ScreenRectangle(minX, minY, maxX - minX, maxY - minY)).transformAxisAligned(this.pose);
        this.scissorStack.push(screenRectangle);
    }

    public void disableScissor() {
        this.scissorStack.pop();
    }

    public boolean containsPointInScissor(int x, int y) {
        return this.scissorStack.containsPoint(x, y);
    }

    public void fill(int minX, int minY, int maxX, int maxY, int color) {
        this.fill(RenderPipelines.GUI, minX, minY, maxX, maxY, color);
    }

    public void fill(RenderPipeline pipeline, int minX, int minY, int maxX, int maxY, int color) {
        if (minX < maxX) {
            int i = minX;
            minX = maxX;
            maxX = i;
        }

        if (minY < maxY) {
            int i = minY;
            minY = maxY;
            maxY = i;
        }

        this.submitColoredRectangle(pipeline, TextureSetup.noTexture(), minX, minY, maxX, maxY, color, (Integer)null);
    }

    public void fillGradient(int minX, int minY, int maxX, int maxY, int colorFrom, int colorTo) {
        this.submitColoredRectangle(RenderPipelines.GUI, TextureSetup.noTexture(), minX, minY, maxX, maxY, colorFrom, colorTo);
    }

    public void fill(RenderPipeline pipeline, TextureSetup textureSetup, int minX, int minY, int maxX, int maxY) {
        this.submitColoredRectangle(pipeline, textureSetup, minX, minY, maxX, maxY, -1, (Integer)null);
    }

    private void submitColoredRectangle(RenderPipeline pipeline, TextureSetup textureSetup, int minX, int minY, int maxX, int maxY, int colorFrom, @Nullable Integer colorTo) {
        this.guiRenderState.submitGuiElement(new ColoredRectangleRenderState(pipeline, textureSetup, new Matrix3x2f(this.pose), minX, minY, maxX, maxY, colorFrom, colorTo != null ? colorTo : colorFrom, this.scissorStack.peek()));
    }

    public void textHighlight(int minX, int minY, int maxX, int maxY) {
        this.fill(RenderPipelines.GUI_INVERT, minX, minY, maxX, maxY, -1);
        this.fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, minX, minY, maxX, maxY, -16776961);
    }

    public void drawCenteredString(Font font, String text, int x, int y, int color) {
        this.drawString(font, text, x - font.width(text) / 2, y, color);
    }

    public void drawCenteredString(Font font, Component text, int x, int y, int color) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        this.drawString(font, formattedCharSequence, x - font.width(formattedCharSequence) / 2, y, color);
    }

    public void drawCenteredString(Font font, FormattedCharSequence text, int x, int y, int color) {
        this.drawString(font, text, x - font.width(text) / 2, y, color);
    }

    public void drawString(Font font, @Nullable String text, int x, int y, int color) {
        this.drawString(font, text, x, y, color, true);
    }

    public void drawString(Font font, @Nullable String text, int x, int y, int color, boolean drawShadow) {
        if (text != null) {
            this.drawString(font, Language.getInstance().getVisualOrder(FormattedText.of(text)), x, y, color, drawShadow);
        }
    }

    public void drawString(Font font, FormattedCharSequence text, int x, int y, int color) {
        this.drawString(font, text, x, y, color, true);
    }

    public void drawString(Font font, FormattedCharSequence text, int x, int y, int color, boolean drawShadow) {
        if (ARGB.alpha(color) != 0) {
            this.guiRenderState.submitText(new GuiTextRenderState(font, text, new Matrix3x2f(this.pose), x, y, color, 0, drawShadow, this.scissorStack.peek()));
        }
    }

    public void drawString(Font font, Component text, int x, int y, int color) {
        this.drawString(font, text, x, y, color, true);
    }

    public void drawString(Font font, Component text, int x, int y, int color, boolean drawShadow) {
        this.drawString(font, text.getVisualOrderText(), x, y, color, drawShadow);
    }

    public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color) {
        this.drawWordWrap(font, text, x, y, lineWidth, color, true);
    }

    public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color, boolean dropShadow) {
        for(FormattedCharSequence formattedCharSequence : font.split(text, lineWidth)) {
            this.drawString(font, formattedCharSequence, x, y, color, dropShadow);
            Objects.requireNonNull(font);
            y += 9;
        }

    }

    public void drawStringWithBackdrop(Font font, Component text, int x, int y, int width, int color) {
        int i = this.minecraft.options.getBackgroundColor(0.0F);
        if (i != 0) {
            int j = 2;
            int var10001 = x - 2;
            int var10002 = y - 2;
            int var10003 = x + width + 2;
            Objects.requireNonNull(font);
            this.fill(var10001, var10002, var10003, y + 9 + 2, ARGB.multiply(i, color));
        }

        this.drawString(font, text, x, y, color, true);
    }

    public void submitOutline(int x, int y, int width, int height, int color) {
        boolean add = this.deferredOutlines.add(new OutlineBox(x, y, width, height, color));
    }


//    public static class ScissorStack {
//        private final Deque<ScreenRectangle> stack = new ArrayDeque();
//
//        ScissorStack() {
//        }
//
//        public ScreenRectangle push(ScreenRectangle scissor) {
//            ScreenRectangle screenRectangle = (ScreenRectangle)this.stack.peekLast();
//            if (screenRectangle != null) {
//                ScreenRectangle screenRectangle2 = (ScreenRectangle)Objects.requireNonNullElse(scissor.intersection(screenRectangle), ScreenRectangle.empty());
//                this.stack.addLast(screenRectangle2);
//                return screenRectangle2;
//            } else {
//                this.stack.addLast(scissor);
//                return scissor;
//            }
//        }
//
//        @Nullable
//        public ScreenRectangle pop() {
//            if (this.stack.isEmpty()) {
//                throw new IllegalStateException("Scissor stack underflow");
//            } else {
//                this.stack.removeLast();
//                return (ScreenRectangle)this.stack.peekLast();
//            }
//        }
//
//        @Nullable
//        public ScreenRectangle peek() {
//            return (ScreenRectangle)this.stack.peekLast();
//        }
//
//        public boolean containsPoint(int x, int y) {
//            return this.stack.isEmpty() ? true : ((ScreenRectangle)this.stack.peek()).containsPoint(x, y);
//        }
//    }
//

    record OutlineBox(int x, int y, int width, int height, int color) {
        OutlineBox(int x, int y, int width, int height, int color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }

        public void render(GuiGraphics guiGraphics) {
            guiGraphics.fill(this.x, this.y, this.x + this.width, this.y + 1, this.color);
            guiGraphics.fill(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, this.color);
            guiGraphics.fill(this.x, this.y + 1, this.x + 1, this.y + this.height - 1, this.color);
            guiGraphics.fill(this.x + this.width - 1, this.y + 1, this.x + this.width, this.y + this.height - 1, this.color);
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public int color() {
            return this.color;
        }
    }

    public void blitSprite(RenderPipeline pipeline, ResourceLocation sprite, int x, int y, int width, int height) {
        this.blitSprite(pipeline, (ResourceLocation)sprite, x, y, width, height, -1);
    }

    public void blitSprite(RenderPipeline pipeline, ResourceLocation sprite, int x, int y, int width, int height, float fade) {
        this.blitSprite(pipeline, sprite, x, y, width, height, ARGB.color(fade, -1));
    }

    private static GuiSpriteScaling getSpriteScaling(TextureAtlasSprite sprite) {
        return ((GuiMetadataSection)sprite.contents().getAdditionalMetadata(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT)).scaling();
    }


//    public void blitSprite(RenderPipeline pipeline, ResourceLocation sprite, int x, int y, int width, int height, int color) {
//        TextureAtlasSprite textureAtlasSprite = this.guiSprites.getSprite(sprite);
//        GuiSpriteScaling guiSpriteScaling = getSpriteScaling(textureAtlasSprite);
//        Objects.requireNonNull(guiSpriteScaling);
//        byte var11 = 0;
//        //$FF: var11->value
//        //0->net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch
//        //1->net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile
//        //2->net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice
//        switch (guiSpriteScaling.typeSwitch<invokedynamic>(guiSpriteScaling, var11)) {
//            case 0:
//                GuiSpriteScaling.Stretch stretch = (GuiSpriteScaling.Stretch)guiSpriteScaling;
//                this.blitSprite(pipeline, textureAtlasSprite, x, y, width, height, color);
//                break;
//            case 1:
//                GuiSpriteScaling.Tile tile = (GuiSpriteScaling.Tile)guiSpriteScaling;
//                this.blitTiledSprite(pipeline, textureAtlasSprite, x, y, width, height, 0, 0, tile.width(), tile.height(), tile.width(), tile.height(), color);
//                break;
//            case 2:
//                GuiSpriteScaling.NineSlice nineSlice = (GuiSpriteScaling.NineSlice)guiSpriteScaling;
//                this.blitNineSlicedSprite(pipeline, textureAtlasSprite, nineSlice, x, y, width, height, color);
//        }
//
//    }

    public void blitSprite(RenderPipeline pipeline, ResourceLocation sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height) {
        this.blitSprite(pipeline, (ResourceLocation)sprite, textureWidth, textureHeight, u, v, x, y, width, height, -1);
    }

    public void blitSprite(RenderPipeline pipeline, ResourceLocation sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height, int color) {
        TextureAtlasSprite textureAtlasSprite = this.guiSprites.getSprite(sprite);
        GuiSpriteScaling guiSpriteScaling = getSpriteScaling(textureAtlasSprite);
        if (guiSpriteScaling instanceof GuiSpriteScaling.Stretch) {
            this.blitSprite(pipeline, textureAtlasSprite, textureWidth, textureHeight, u, v, x, y, width, height, color);
        } else {
            this.enableScissor(x, y, x + width, y + height);
            this.blitSprite(pipeline, sprite, x - u, y - v, textureWidth, textureHeight, color);
            this.disableScissor();
        }

    }

    public void blitSprite(RenderPipeline pipeline, TextureAtlasSprite sprite, int x, int width, int y, int height) {
        this.blitSprite(pipeline, (TextureAtlasSprite)sprite, x, width, y, height, -1);
    }

    public void blitSprite(RenderPipeline pipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int color) {
        if (width != 0 && height != 0) {
            this.innerBlit(pipeline, sprite.atlasLocation(), x, x + width, y, y + height, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), color);
        }
    }

    private void blitSprite(RenderPipeline pipeline, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height, int color) {
        if (width != 0 && height != 0) {
            this.innerBlit(pipeline, sprite.atlasLocation(), x, x + width, y, y + height, sprite.getU((float)u / (float)textureWidth), sprite.getU((float)(u + width) / (float)textureWidth), sprite.getV((float)v / (float)textureHeight), sprite.getV((float)(v + height) / (float)textureHeight), color);
        }
    }

    private void blitNineSlicedSprite(RenderPipeline pipeline, TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int width, int height, int color) {
        GuiSpriteScaling.NineSlice.Border border = nineSlice.border();
        int i = Math.min(border.left(), width / 2);
        int j = Math.min(border.right(), width / 2);
        int k = Math.min(border.top(), height / 2);
        int l = Math.min(border.bottom(), height / 2);
        if (width == nineSlice.width() && height == nineSlice.height()) {
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, height, color);
        } else if (height == nineSlice.height()) {
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, height, color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x + i, y, width - j - i, height, i, 0, nineSlice.width() - j - i, nineSlice.height(), nineSlice.width(), nineSlice.height(), color);
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, height, color);
        } else if (width == nineSlice.width()) {
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, k, color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x, y + k, width, height - l - k, 0, k, nineSlice.width(), nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, width, l, color);
        } else {
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, k, color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x + i, y, width - j - i, k, i, 0, nineSlice.width() - j - i, k, nineSlice.width(), nineSlice.height(), color);
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, k, color);
            this.blitSprite(pipeline, (TextureAtlasSprite)sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, i, l, color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x + i, y + height - l, width - j - i, l, i, nineSlice.height() - l, nineSlice.width() - j - i, l, nineSlice.width(), nineSlice.height(), color);
            this.blitSprite(pipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, nineSlice.height() - l, x + width - j, y + height - l, j, l, color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x, y + k, i, height - l - k, 0, k, i, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x + i, y + k, width - j - i, height - l - k, i, k, nineSlice.width() - j - i, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
            this.blitNineSliceInnerSegment(pipeline, nineSlice, sprite, x + width - j, y + k, j, height - l - k, nineSlice.width() - j, k, j, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), color);
        }
    }

    private void blitNineSliceInnerSegment(RenderPipeline pipeline, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite sprite, int borderMinX, int borderMinY, int borderMaxX, int borderMaxY, int u, int v, int spriteWidth, int spriteHeight, int textureWidth, int textureHeight, int color) {
        if (borderMaxX > 0 && borderMaxY > 0) {
            if (nineSlice.stretchInner()) {
                this.innerBlit(pipeline, sprite.atlasLocation(), borderMinX, borderMinX + borderMaxX, borderMinY, borderMinY + borderMaxY, sprite.getU((float)u / (float)textureWidth), sprite.getU((float)(u + spriteWidth) / (float)textureWidth), sprite.getV((float)v / (float)textureHeight), sprite.getV((float)(v + spriteHeight) / (float)textureHeight), color);
            } else {
                this.blitTiledSprite(pipeline, sprite, borderMinX, borderMinY, borderMaxX, borderMaxY, u, v, spriteWidth, spriteHeight, textureWidth, textureHeight, color);
            }

        }
    }

    private void blitTiledSprite(RenderPipeline pipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int u, int v, int spriteWidth, int spriteHeight, int textureWidth, int textureHeight, int color) {
        if (width > 0 && height > 0) {
            if (spriteWidth > 0 && spriteHeight > 0) {
                GpuTextureView gpuTextureView = this.minecraft.getTextureManager().getTexture(sprite.atlasLocation()).getTextureView();
                this.submitTiledBlit(pipeline, gpuTextureView, spriteWidth, spriteHeight, x, y, x + width, y + height, sprite.getU((float)u / (float)textureWidth), sprite.getU((float)(u + spriteWidth) / (float)textureWidth), sprite.getV((float)v / (float)textureHeight), sprite.getV((float)(v + spriteHeight) / (float)textureHeight), color);
            } else {
                throw new IllegalArgumentException("Tile size must be positive, got " + spriteWidth + "x" + spriteHeight);
            }
        }
    }

    public void blit(RenderPipeline pipeline, ResourceLocation atlas, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
        this.blit(pipeline, atlas, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color);
    }

    public void blit(RenderPipeline pipeline, ResourceLocation atlas, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        this.blit(pipeline, atlas, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
    }

    public void blit(RenderPipeline pipeline, ResourceLocation atlas, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.blit(pipeline, atlas, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight, -1);
    }

    public void blit(RenderPipeline pipeline, ResourceLocation atlas, int x, int y, float u, float v, int width, int height, int uWidth, int vHeight, int textureWidth, int textureHeight, int color) {
        this.innerBlit(pipeline, atlas, x, x + width, y, y + height, (u + 0.0F) / (float)textureWidth, (u + (float)uWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)vHeight) / (float)textureHeight, color);
    }

    public void blit(ResourceLocation atlas, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
        this.innerBlit(RenderPipelines.GUI_TEXTURED, atlas, x0, x1, y0, y1, u0, u1, v0, v1, -1);
    }

    private void innerBlit(RenderPipeline pipeline, ResourceLocation atlas, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
        GpuTextureView gpuTextureView = this.minecraft.getTextureManager().getTexture(atlas).getTextureView();
        this.submitBlit(pipeline, gpuTextureView, x0, y0, x1, y1, u0, u1, v0, v1, color);
    }

    private void submitBlit(RenderPipeline pipeline, GpuTextureView atlasTexture, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
        this.guiRenderState.submitGuiElement(new BlitRenderState(pipeline, TextureSetup.singleTexture(atlasTexture), new Matrix3x2f(this.pose), x0, y0, x1, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
    }

    private void submitTiledBlit(RenderPipeline pipeline, GpuTextureView atlasTexture, int tileWidth, int tileHeight, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
        this.guiRenderState.submitGuiElement(new TiledBlitRenderState(pipeline, TextureSetup.singleTexture(atlasTexture), new Matrix3x2f(this.pose), tileWidth, tileHeight, x0, x1, y0, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
    }

    public void renderItem(ItemStack stack, int x, int y) {
        this.renderItem(this.minecraft.player, this.minecraft.level, stack, x, y, 0);
    }

    public void renderItem(ItemStack stack, int x, int y, int seed) {
        this.renderItem(this.minecraft.player, this.minecraft.level, stack, x, y, seed);
    }

    public void renderFakeItem(ItemStack stack, int x, int y) {
        this.renderFakeItem(stack, x, y, 0);
    }

    public void renderFakeItem(ItemStack stack, int x, int y, int seed) {
        this.renderItem((LivingEntity)null, this.minecraft.level, stack, x, y, seed);
    }

    public void renderItem(LivingEntity entity, ItemStack stack, int x, int y, int seed) {
        this.renderItem(entity, entity.level(), stack, x, y, seed);
    }

    private void renderItem(@Nullable LivingEntity entity, @Nullable Level level, ItemStack stack, int x, int y, int seed) {
        if (!stack.isEmpty()) {
            TrackingItemStackRenderState trackingItemStackRenderState = new TrackingItemStackRenderState();
            this.minecraft.getItemModelResolver().updateForTopItem(trackingItemStackRenderState, stack, ItemDisplayContext.GUI, level, entity, seed);

            try {
                this.guiRenderState.submitItem(new GuiItemRenderState(stack.getItem().getName().toString(), new Matrix3x2f(this.pose), trackingItemStackRenderState, x, y, this.scissorStack.peek()));
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashReportCategory = crashReport.addCategory("Item being rendered");
                crashReportCategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportCategory.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportCategory.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                throw new ReportedException(crashReport);
            }
        }
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y) {
        this.renderItemDecorations(font, stack, x, y, (String)null);
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
        if (!stack.isEmpty()) {
            this.pose.pushMatrix();
            this.renderItemBar(stack, x, y);
            this.renderItemCooldown(stack, x, y);
            this.renderItemCount(font, stack, x, y, text);
            this.pose.popMatrix();
        }
    }

    public void setTooltipForNextFrame(Component text, int x, int y) {
        this.setTooltipForNextFrame(List.of(text.getVisualOrderText()), x, y);
    }

    public void setTooltipForNextFrame(List<FormattedCharSequence> lines, int x, int y) {
        this.setTooltipForNextFrame(this.minecraft.font, lines, DefaultTooltipPositioner.INSTANCE, x, y, false);
    }

    public void setTooltipForNextFrame(Font font, ItemStack stack, int x, int y) {
        this.setTooltipForNextFrame(font, Screen.getTooltipFromItem(this.minecraft, stack), stack.getTooltipImage(), x, y, (ResourceLocation)stack.get(DataComponents.TOOLTIP_STYLE));
    }

    public void setTooltipForNextFrame(Font font, List<Component> lines, Optional<TooltipComponent> tooltipImage, int x, int y) {
        this.setTooltipForNextFrame(font, lines, tooltipImage, x, y, (ResourceLocation)null);
    }

    public void setTooltipForNextFrame(Font font, List<Component> lines, Optional<TooltipComponent> tooltipImage, int x, int y, @Nullable ResourceLocation background) {
        List<ClientTooltipComponent> list = (List)lines.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Util.toMutableList());
        tooltipImage.ifPresent((tooltipComponent) -> list.add(list.isEmpty() ? 0 : 1, ClientTooltipComponent.create(tooltipComponent)));
        this.setTooltipForNextFrameInternal(font, list, x, y, DefaultTooltipPositioner.INSTANCE, background, false);
    }

    public void setTooltipForNextFrame(Font font, Component text, int x, int y) {
        this.setTooltipForNextFrame(font, text, x, y, (ResourceLocation)null);
    }

    public void setTooltipForNextFrame(Font font, Component text, int x, int y, @Nullable ResourceLocation background) {
        this.setTooltipForNextFrame(font, List.of(text.getVisualOrderText()), x, y, background);
    }

    public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int x, int y) {
        this.setComponentTooltipForNextFrame(font, lines, x, y, (ResourceLocation)null);
    }

    public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int x, int y, @Nullable ResourceLocation background) {
        this.setTooltipForNextFrameInternal(font, lines.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList(), x, y, DefaultTooltipPositioner.INSTANCE, background, false);
    }

    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int x, int y) {
        this.setTooltipForNextFrame(font, (List)lines, x, y, (ResourceLocation)null);
    }

    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int x, int y, @Nullable ResourceLocation background) {
        this.setTooltipForNextFrameInternal(font, (List)lines.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, DefaultTooltipPositioner.INSTANCE, background, false);
    }

    public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> lines, ClientTooltipPositioner positioner, int x, int y, boolean focused) {
        this.setTooltipForNextFrameInternal(font, (List)lines.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, positioner, (ResourceLocation)null, focused);
    }

    private void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation background, boolean focused) {
        if (!components.isEmpty()) {
            if (this.deferredTooltip == null || focused) {
                this.deferredTooltip = () -> this.renderTooltip(font, components, x, y, positioner, background);
            }

        }
    }

//    public void renderTooltip(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation background) {
//        int i = 0;
//        int j = components.size() == 1 ? -2 : 0;
//
//        for(ClientTooltipComponent clientTooltipComponent : components) {
//            int k = clientTooltipComponent.getWidth(font);
//            if (k > i) {
//                i = k;
//            }
//
//            j += clientTooltipComponent.getHeight(font);
//        }
//
//        int l = i;
//        int m = j;
//        Vector2ic vector2ic = positioner.positionTooltip(this.guiWidth(), this.guiHeight(), x, y, i, j);
//        int n = vector2ic.x();
//        int o = vector2ic.y();
//        this.pose.pushMatrix();


    public void renderTooltip(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation background) {
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;

        for(ClientTooltipComponent clientTooltipComponent : components) {
            int k = clientTooltipComponent.getWidth(font);
            if (k > i) {
                i = k;
            }

            j += clientTooltipComponent.getHeight(font);
        }

        int l = i;
        int m = j;
        Vector2ic vector2ic = positioner.positionTooltip(this.guiWidth(), this.guiHeight(), x, y, i, j);
        int n = vector2ic.x();
        int o = vector2ic.y();
        this.pose.pushMatrix();
        TooltipRenderUtil.renderTooltipBackground(this, n, o, i, j, background);
        int p = o;

        for(int q = 0; q < components.size(); ++q) {
            ClientTooltipComponent clientTooltipComponent2 = (ClientTooltipComponent)components.get(q);
            clientTooltipComponent2.renderText(this, font, n, p);
            p += clientTooltipComponent2.getHeight(font) + (q == 0 ? 2 : 0);
        }

        p = o;

        for(int q = 0; q < components.size(); ++q) {
            ClientTooltipComponent clientTooltipComponent2 = (ClientTooltipComponent)components.get(q);
            clientTooltipComponent2.renderImage(font, n, p, l, m, this);
            p += clientTooltipComponent2.getHeight(font) + (q == 0 ? 2 : 0);
        }

        this.pose.popMatrix();
    }

//    public void renderDeferredElements() {
//        if (!this.deferredOutlines.isEmpty()) {
//            this.nextStratum();
//
//            this.deferredOutlines.forEach(outlineBox -> outlineBox.render(this));
//
//            this.deferredOutlines.clear();
//        }
//
//        if (this.deferredTooltip != null) {
//            this.nextStratum();
//            this.deferredTooltip.run();
//            this.deferredTooltip = null;
//        }
//
//    }

    private void renderItemBar(ItemStack stack, int x, int y) {
        if (stack.isBarVisible()) {
            int i = x + 2;
            int j = y + 13;
            this.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
            this.fill(RenderPipelines.GUI, i, j, i + stack.getBarWidth(), j + 1, ARGB.opaque(stack.getBarColor()));
        }

    }

    private void renderItemCount(Font font, ItemStack stack, int x, int y, @Nullable String text) {
        if (stack.getCount() != 1 || text != null) {
            String string = text == null ? String.valueOf(stack.getCount()) : text;
            this.drawString(font, (String)string, x + 19 - 2 - font.width(string), y + 6 + 3, -1, true);
        }

    }

    private void renderItemCooldown(ItemStack stack, int x, int y) {
        LocalPlayer localPlayer = this.minecraft.player;
        float f = localPlayer == null ? 0.0F : localPlayer.getCooldowns().getCooldownPercent(stack, this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true));
        if (f > 0.0F) {
            int i = y + Mth.floor(16.0F * (1.0F - f));
            int j = i + Mth.ceil(16.0F * f);
            this.fill(RenderPipelines.GUI, x, i, x + 16, j, Integer.MAX_VALUE);
        }

    }
//
//    public void renderComponentHoverEffect(Font font, @Nullable Style style, int mouseX, int mouseY) {
//        if (style != null) {
//            if (style.getClickEvent() != null) {
//                this.requestCursor(CursorTypes.POINTING_HAND);
//            }
//
//            if (style.getHoverEvent() != null) {
//                HoverEvent var10000 = style.getHoverEvent();
//                Objects.requireNonNull(var10000);
//                HoverEvent var5 = var10000;
//                byte var6 = 0;
//                //$FF: var6->value
//                //0->net/minecraft/network/chat/HoverEvent$ShowItem
//                //1->net/minecraft/network/chat/HoverEvent$ShowEntity
//                //2->net/minecraft/network/chat/HoverEvent$ShowText
//                switch (var5.typeSwitch<invokedynamic>(var5, var6)) {
//                    case 0:
//                        HoverEvent.ShowItem var7 = (HoverEvent.ShowItem)var5;
//                        HoverEvent.ShowItem var23 = var7;
//
//                        try {
//                            var24 = var23.item();
//                        } catch (Throwable var16) {
//                            throw new MatchException(var16.toString(), var16);
//                        }
//
//                        ItemStack itemStack = var24;
//                        this.setTooltipForNextFrame(font, itemStack, mouseX, mouseY);
//                        break;
//                    case 1:
//                        HoverEvent.ShowEntity itemStack = (HoverEvent.ShowEntity)var5;
//                        HoverEvent.ShowEntity var21 = itemStack;
//
//                        try {
//                            var22 = var21.entity();
//                        } catch (Throwable var15) {
//                            throw new MatchException(var15.toString(), var15);
//                        }
//
//                        HoverEvent.EntityTooltipInfo entityTooltipInfo = var22;
//                        if (this.minecraft.options.advancedItemTooltips) {
//                            this.setComponentTooltipForNextFrame(font, entityTooltipInfo.getTooltipLines(), mouseX, mouseY);
//                        }
//                        break;
//                    case 2:
//                        HoverEvent.ShowText entityTooltipInfo = (HoverEvent.ShowText)var5;
//                        HoverEvent.ShowText var19 = entityTooltipInfo;
//
//                        try {
//                            var20 = var19.value();
//                        } catch (Throwable var14) {
//                            throw new MatchException(var14.toString(), var14);
//                        }
//
//                        Component component = var20;
//                        this.setTooltipForNextFrame(font, font.split(component, Math.max(this.guiWidth() / 2, 200)), mouseX, mouseY);
//                }
//            }
//
//        }
//    }

    public void submitMapRenderState(MapRenderState renderState) {
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        GpuTextureView gpuTextureView = textureManager.getTexture(renderState.texture).getTextureView();
        this.submitBlit(RenderPipelines.GUI_TEXTURED, gpuTextureView, 0, 0, 128, 128, 0.0F, 1.0F, 0.0F, 1.0F, -1);

        for(MapRenderState.MapDecorationRenderState mapDecorationRenderState : renderState.decorations) {
            if (mapDecorationRenderState.renderOnFrame) {
                this.pose.pushMatrix();
                this.pose.translate((float)mapDecorationRenderState.x / 2.0F + 64.0F, (float)mapDecorationRenderState.y / 2.0F + 64.0F);
                this.pose.rotate(((float)Math.PI / 180F) * (float)mapDecorationRenderState.rot * 360.0F / 16.0F);
                this.pose.scale(4.0F, 4.0F);
                this.pose.translate(-0.125F, 0.125F);
                TextureAtlasSprite textureAtlasSprite = mapDecorationRenderState.atlasSprite;
                if (textureAtlasSprite != null) {
                    GpuTextureView gpuTextureView2 = textureManager.getTexture(textureAtlasSprite.atlasLocation()).getTextureView();
                    this.submitBlit(RenderPipelines.GUI_TEXTURED, gpuTextureView2, -1, -1, 1, 1, textureAtlasSprite.getU0(), textureAtlasSprite.getU1(), textureAtlasSprite.getV1(), textureAtlasSprite.getV0(), -1);
                }

                this.pose.popMatrix();
                if (mapDecorationRenderState.name != null) {
                    Font font = minecraft.font;
                    float f = (float)font.width(mapDecorationRenderState.name);
                    float var10000 = 25.0F / f;
                    Objects.requireNonNull(font);
                    float g = Mth.clamp(var10000, 0.0F, 6.0F / 9.0F);
                    this.pose.pushMatrix();
                    this.pose.translate((float)mapDecorationRenderState.x / 2.0F + 64.0F - f * g / 2.0F, (float)mapDecorationRenderState.y / 2.0F + 64.0F + 4.0F);
                    this.pose.scale(g, g);
                    this.guiRenderState.submitText(new GuiTextRenderState(font, mapDecorationRenderState.name.getVisualOrderText(), new Matrix3x2f(this.pose), 0, 0, -1, Integer.MIN_VALUE, false, this.scissorStack.peek()));
                    this.pose.popMatrix();
                }
            }
        }

    }

    public void submitEntityRenderState(EntityRenderState renderState, float scale, Vector3f translation, Quaternionf rotation, @Nullable Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiEntityRenderState(renderState, translation, rotation, overrideCameraAngle, x0, y0, x1, y1, scale, this.scissorStack.peek()));
    }

    public void submitSkinRenderState(PlayerModel playerModel, ResourceLocation texture, float scale, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiSkinRenderState(playerModel, texture, rotationX, rotationY, pivotY, x0, y0, x1, y1, scale, this.scissorStack.peek()));
    }

    public void submitBookModelRenderState(BookModel bookModel, ResourceLocation texture, float scale, float open, float flip, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiBookModelRenderState(bookModel, texture, open, flip, x0, y0, x1, y1, scale, this.scissorStack.peek()));
    }

    public void submitBannerPatternRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiBannerResultRenderState(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, this.scissorStack.peek()));
    }

    public void submitSignRenderState(Model.Simple signModel, float scale, WoodType woodType, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiSignRenderState(signModel, woodType, x0, y0, x1, y1, scale, this.scissorStack.peek()));
    }

    public void submitProfilerChartRenderState(List<ResultField> chartData, int x0, int y0, int x1, int y1) {
        this.guiRenderState.submitPicturesInPictureState(new GuiProfilerChartRenderState(chartData, x0, y0, x1, y1, this.scissorStack.peek()));
    }

    public TextureAtlasSprite getSprite(Material material) {
        return this.materials.get(material);
    }

    public static class ScissorStack {
        private final Deque<ScreenRectangle> stack = new ArrayDeque();

        ScissorStack() {
        }

        public ScreenRectangle push(ScreenRectangle scissor) {
            ScreenRectangle screenRectangle = (ScreenRectangle)this.stack.peekLast();
            if (screenRectangle != null) {
                ScreenRectangle screenRectangle2 = (ScreenRectangle)Objects.requireNonNullElse(scissor.intersection(screenRectangle), ScreenRectangle.empty());
                this.stack.addLast(screenRectangle2);
                return screenRectangle2;
            } else {
                this.stack.addLast(scissor);
                return scissor;
            }
        }

        @Nullable
        public ScreenRectangle pop() {
            if (this.stack.isEmpty()) {
                throw new IllegalStateException("Scissor stack underflow");
            } else {
                this.stack.removeLast();
                return (ScreenRectangle)this.stack.peekLast();
            }
        }

        @Nullable
        public ScreenRectangle peek() {
            return (ScreenRectangle)this.stack.peekLast();
        }

        public boolean containsPoint(int x, int y) {
            return this.stack.isEmpty() ? true : ((ScreenRectangle)this.stack.peek()).containsPoint(x, y);
        }
    }
//
//    static record OutlineBox(int x, int y, int width, int height, int color) {
//        OutlineBox(int i, int j, int k, int l, int m) {
//            this.x = i;
//            this.y = j;
//            this.width = k;
//            this.height = l;
//            this.color = m;
//        }
//
//        public void render(GuiGraphics guiGraphics) {
//            guiGraphics.fill(this.x, this.y, this.x + this.width, this.y + 1, this.color);
//            guiGraphics.fill(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, this.color);
//            guiGraphics.fill(this.x, this.y + 1, this.x + 1, this.y + this.height - 1, this.color);
//            guiGraphics.fill(this.x + this.width - 1, this.y + 1, this.x + this.width, this.y + this.height - 1, this.color);
//        }
//
//        public int x() {
//            return this.x;
//        }
//
//        public int y() {
//            return this.y;
//        }
//
//        public int width() {
//            return this.width;
//        }
//
//        public int height() {
//            return this.height;
//        }
//
//        public int color() {
//            return this.color;
//        }
//    }
}
