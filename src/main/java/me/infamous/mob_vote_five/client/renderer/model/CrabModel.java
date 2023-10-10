package me.infamous.mob_vote_five.client.renderer.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Crab;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrabModel<T extends Crab> extends AnimatedGeoModel<T> {

	public static final ResourceLocation ANIMATION_LOCATION = new ResourceLocation(MobVote2023.MODID, "animations/crab.animation.json");
	public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(MobVote2023.MODID, "geo/crab.geo.json");
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/crab/crab.png");

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
}