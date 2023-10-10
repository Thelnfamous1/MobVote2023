package me.infamous.mob_vote_five.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.infamous.mob_vote_five.client.MVModelLayers;
import me.infamous.mob_vote_five.common.item.WolfArmorItem;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class WolfArmorLayer<T extends Wolf> extends RenderLayer<T, WolfModel<T>> {
   private final WolfModel<T> model;

   public WolfArmorLayer(RenderLayerParent<T, WolfModel<T>> pRenderer, EntityModelSet modelSet) {
      super(pRenderer);
      this.model = new WolfModel<>(modelSet.bakeLayer(MVModelLayers.WOLF_ARMOR));
   }

   public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
      ItemStack armor = WolfArmorItem.getArmor(pLivingEntity);
      if (armor.getItem() instanceof WolfArmorItem armorItem) {
         this.getParentModel().copyPropertiesTo(this.model);
         this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
         this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
         float red;
         float green;
         float blue;
         if (armorItem instanceof DyeableLeatherItem) {
            int i = ((DyeableLeatherItem)armorItem).getColor(armor);
            red = (float)(i >> 16 & 255) / 255.0F;
            green = (float)(i >> 8 & 255) / 255.0F;
            blue = (float)(i & 255) / 255.0F;
         } else {
            red = 1.0F;
            green = 1.0F;
            blue = 1.0F;
         }

         VertexConsumer armorBuffer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(armorItem.getTexture()));
         this.model.renderToBuffer(pMatrixStack, armorBuffer, pPackedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
      }
   }

   public static MeshDefinition createBodyMesh(CubeDeformation pCubeDeformation) {
      MeshDefinition meshDefinition = new MeshDefinition();
      PartDefinition root = meshDefinition.getRoot();
      float f = 13.5F;
      PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.5F, -7.0F));
      head.addOrReplaceChild("real_head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, pCubeDeformation).texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, pCubeDeformation).texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, pCubeDeformation).texOffs(0, 10).addBox(-0.5F, -0.001F, -5.0F, 3.0F, 3.0F, 4.0F, pCubeDeformation), PartPose.ZERO);
      root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, pCubeDeformation), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
      root.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, pCubeDeformation), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
      CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, pCubeDeformation);
      root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-2.5F, 16.0F, 7.0F));
      root.addOrReplaceChild("left_hind_leg", leg, PartPose.offset(0.5F, 16.0F, 7.0F));
      root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-2.5F, 16.0F, -4.0F));
      root.addOrReplaceChild("left_front_leg", leg, PartPose.offset(0.5F, 16.0F, -4.0F));
      PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 12.0F, 8.0F, ((float)Math.PI / 5F), 0.0F, 0.0F));
      tail.addOrReplaceChild("real_tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, pCubeDeformation), PartPose.ZERO);
      return meshDefinition;
   }
}