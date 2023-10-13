package me.infamous.mob_vote_five.client.renderer.model;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.GrapplingHookEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GrapplingHookModel<T extends GrapplingHookEntity> extends AnimatedGeoModel<T> {

    public static final ResourceLocation ANIMATION_LOCATION = new ResourceLocation(MobVote2023.MODID, "animations/grappling_hook.animation.json");
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(MobVote2023.MODID, "geo/grappling_hook.geo.json");
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MobVote2023.MODID, "textures/entity/grappling_hook/grappling_hook.png");

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
