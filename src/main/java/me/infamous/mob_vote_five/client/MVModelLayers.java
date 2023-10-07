package me.infamous.mob_vote_five.client;

import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class MVModelLayers {
    public static final ModelLayerLocation ARMADILLO = createLayerLocation(MVEntityTypes.ARMADILLO.getId());
    public static final ModelLayerLocation CRAB = createLayerLocation(MVEntityTypes.CRAB.getId());
    public static final ModelLayerLocation PENGUIN = createLayerLocation(MVEntityTypes.PENGUIN.getId());

    private static ModelLayerLocation createLayerLocation(ResourceLocation id) {
        return new ModelLayerLocation(id, "main");
    }
}
