package me.infamous.mob_vote_five.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.renderer.model.ArmadilloModel;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ArmadilloRenderer<T extends Armadillo> extends GeoEntityRenderer<T> {
    public static final ResourceLocation LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/armadillo/armadillo.png");

    public ArmadilloRenderer(EntityRendererProvider.Context context) {
        super(context, new ArmadilloModel<>());
        this.shadowRadius =  0.7F;
    }

    @Override
    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.shadowRadius *= 0.5F;
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public float getWidthScale(T animatable) {
        return super.getWidthScale(animatable) * (animatable.isBaby() ? 0.5F : 1.0F);
    }

    @Override
    public float getHeightScale(T entity) {
        return super.getHeightScale(entity) * (entity.isBaby() ? 0.5F : 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return LOCATION;
    }
}
