package me.infamous.mob_vote_five.client.renderer.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Penguin;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

public class PenguinModel<T extends Penguin> extends AnimatedGeoModel<T> {

	public static final ResourceLocation ANIMATION_LOCATION = new ResourceLocation(MobVote2023.MODID, "animations/penguin.animation.json");
	public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(MobVote2023.MODID, "geo/penguin.geo.json");
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/penguin/penguin.png");

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return ANIMATION_LOCATION;
	}

	@Override
	public ResourceLocation getModelResource(T object) {
		return MODEL_LOCATION;
	}

	@Override
	public ResourceLocation getTextureResource(T object) {
		return TEXTURE_LOCATION;
	}

	@Override
	public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
			head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
			head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));

			if(animatable.isInWater()){
				List<IBone> modelRendererList = this.getAnimationProcessor().getModelRendererList();
				for(IBone bone : modelRendererList){
					if(bone != head) bone.setRotationX(bone.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
				}
			}
		}
	}
}