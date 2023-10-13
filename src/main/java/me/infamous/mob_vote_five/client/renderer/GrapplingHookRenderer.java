package me.infamous.mob_vote_five.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import me.infamous.mob_vote_five.client.renderer.model.GrapplingHookModel;
import me.infamous.mob_vote_five.common.entity.GrapplingHookEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class GrapplingHookRenderer<T extends GrapplingHookEntity> extends GeoProjectilesRenderer<T> {
    public GrapplingHookRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GrapplingHookModel<>());
    }

    @Override
    public void render(T animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);
        Entity owner = animatable.getOwner();
        if (owner != null) {
            this.renderLeash(animatable, partialTick, poseStack, bufferSource, owner);
        }
    }

    private <E extends Entity> void renderLeash(T entity, float partialTick, PoseStack poseStack,
                                                MultiBufferSource bufferSource, E leashHolder) {
        double lerpBodyAngle = (Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
        Vec3 leashOffset = entity.getLeashOffset();
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = Mth.lerp(partialTick, entity.xo, entity.getX()) + xAngleOffset;
        double lerpOriginY = Mth.lerp(partialTick, entity.yo, entity.getY()) + leashOffset.y;
        double lerpOriginZ = Mth.lerp(partialTick, entity.zo, entity.getZ()) + zAngleOffset;
        Vec3 ropeGripPosition = leashHolder.getRopeHoldPosition(partialTick);
        float xDif = (float)(ropeGripPosition.x - lerpOriginX);
        float yDif = (float)(ropeGripPosition.y - lerpOriginY);
        float zDif = (float)(ropeGripPosition.z - lerpOriginZ);
        float offsetMod = Mth.fastInvSqrt(xDif * xDif + zDif * zDif) * 0.025f / 2f;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        BlockPos entityEyePos = new BlockPos(entity.getEyePosition(partialTick));
        BlockPos holderEyePos = new BlockPos(leashHolder.getEyePosition(partialTick));
        int entityBlockLight = getBlockLightLevel(entity, entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.level.getBrightness(LightLayer.BLOCK, holderEyePos);
        int entitySkyLight = entity.level.getBrightness(LightLayer.SKY, entityEyePos);
        int holderSkyLight = entity.level.getBrightness(LightLayer.SKY, holderEyePos);

        poseStack.pushPose();
        poseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        Matrix4f posMatrix = poseStack.last().pose();

        for (int segment = 0; segment <= 24; ++segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.025f, xOffset, zOffset, segment, false);
        }

        for (int segment = 24; segment >= 0; --segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight,
                    entitySkyLight, holderSkyLight, 0.025f, 0.0f, xOffset, zOffset, segment, true);
        }

        poseStack.popPose();
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif,
                                         float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight,
                                         int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = segment / 24f;
        int lerpBlockLight = (int)Mth.lerp(piecePosPercent, entityBlockLight, holderBlockLight);
        int lerpSkyLight = (int)Mth.lerp(piecePosPercent, entitySkyLight, holderSkyLight);
        int packedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1f;
        float red = 0.5f * knotColourMod;
        float green = 0.4f * knotColourMod;
        float blue = 0.3f * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0f ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0f - piecePosPercent) * (1.0f - piecePosPercent);
        float z = zDif * piecePosPercent;

        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1).uv2(packedLight).endVertex();
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1).uv2(packedLight).endVertex();
    }
}
