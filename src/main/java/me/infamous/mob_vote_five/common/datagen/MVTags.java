package me.infamous.mob_vote_five.common.datagen;

import me.infamous.mob_vote_five.MobVote2023;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MVTags {

    private static TagKey<Block> create(String pName) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MobVote2023.MODID, pName));
    }

}
