package me.infamous.mob_vote_five.client.renderer.model;


import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ArmadilloModel<T extends Armadillo> extends AnimatedGeoModel<T> {

	public static final ResourceLocation ANIMATION_LOCATION = new ResourceLocation(MobVote2023.MODID, "animations/armadillo.animation.json");
	public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(MobVote2023.MODID, "geo/armadillo.geo.json");
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/armadillo/armadillo.png");

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
		}
	}
}