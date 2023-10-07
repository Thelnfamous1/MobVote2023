package me.infamous.mob_vote_five.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.MVModelLayers;
import me.infamous.mob_vote_five.client.renderer.model.ArmadilloModel;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArmadilloRenderer extends MobRenderer<Armadillo, ArmadilloModel<Armadillo>> {
    public static final ResourceLocation LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/armadillo/armadillo.png");

    public ArmadilloRenderer(EntityRendererProvider.Context context) {
        super(context, new ArmadilloModel<>(context.bakeLayer(MVModelLayers.ARMADILLO)), 0.7F);
    }

    @Override
    public void render(Armadillo pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.shadowRadius *= 0.5F;
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected void scale(Armadillo pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        if(pLivingEntity.isBaby()){
            pMatrixStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Armadillo pEntity) {
        return LOCATION;
    }
}
