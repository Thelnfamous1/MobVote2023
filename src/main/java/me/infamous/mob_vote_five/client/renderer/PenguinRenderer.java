package me.infamous.mob_vote_five.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.MVModelLayers;
import me.infamous.mob_vote_five.client.renderer.model.PenguinModel;
import me.infamous.mob_vote_five.common.entity.Penguin;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PenguinRenderer extends MobRenderer<Penguin, PenguinModel<Penguin>> {
    public static final ResourceLocation LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/penguin/penguin.png");

    public PenguinRenderer(EntityRendererProvider.Context context) {
        super(context, new PenguinModel<>(context.bakeLayer(MVModelLayers.PENGUIN)), 0.7F);
    }

    @Override
    public void render(Penguin pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            this.shadowRadius *= 0.5F;
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected void scale(Penguin pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        if(pLivingEntity.isBaby()){
            pMatrixStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Penguin pEntity) {
        return LOCATION;
    }
}
