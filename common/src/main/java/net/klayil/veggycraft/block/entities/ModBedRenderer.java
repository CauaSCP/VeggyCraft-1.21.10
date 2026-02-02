package net.klayil.veggycraft.block.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBedBlock;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModBedRenderer implements BlockEntityRenderer<ModBedEntity, ModBedRenderer.ModBedRenderState> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("veggycraft", "textures/block/straw_bed.png");


    private final ModelPart head;
    private final ModelPart foot;

    public ModBedRenderer(BlockEntityRendererProvider.Context ctx) {
        this.head = ctx.bakeLayer(ModelLayers.BED_HEAD);
        this.foot = ctx.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public @NotNull ModBedRenderState createRenderState() {
        return new ModBedRenderState();
    }

    @Override
    public void extractRenderState(ModBedEntity blockEntity, ModBedRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(blockEntity, renderState, breakProgress);
        renderState.state = blockEntity.getBlockState();
    }

    @Override
    public void submit(ModBedRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
        if (!(state.state.getBlock() instanceof ModBedBlock)) return;

        boolean isHead = state.state.getValue(ModBedBlock.PART) == BedPart.HEAD;
        Direction facing = state.state.getValue(ModBedBlock.FACING);

        ModelPart part = isHead ? head : foot;

        poseStack.pushPose();

        poseStack.translate(0, 0.5625, 0);

        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.translate(0.5, 0.5, 0.5);

        poseStack.mulPose(Axis.ZP.rotationDegrees(180 + facing.toYRot()));

        poseStack.translate(-0.5, -0.5, -0.5);

        collector.submitModelPart(
                part,
                poseStack,
                RenderType.entitySolid(TEXTURE),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                null
        );

        poseStack.popPose();
    }

    public static class ModBedRenderState extends BlockEntityRenderState{
        public BlockState state;
    }
}
