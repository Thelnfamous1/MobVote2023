package me.infamous.mob_vote_five.common.datagen;

import me.infamous.mob_vote_five.MobVote2023;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class MVTags {

    public static final TagKey<Item> CRAB_FOOD = createItems("crab_food");
    public static final TagKey<Block> PENGUINS_SPAWNABLE_ON = createBlocks("penguins_spawnable_on");
    public static final TagKey<Item> PENGUIN_FOOD = createItems("penguin_food");
    public static final TagKey<Item> ARMADILLO_FOOD = createItems("armadillo_food");
    public static final TagKey<Block> CRABS_SPAWNABLE_ON = createBlocks("crabs_spawnable_on");
    public static final TagKey<EntityType<?>> PENGUIN_EGG_IGNORES_FALLING = createEntityTypes("penguin_egg_ignores_falling");
    public static final TagKey<EntityType<?>> PENGUIN_EGG_IGNORES = createEntityTypes("penguin_egg_ignores");

    private static TagKey<Block> createBlocks(String pName) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MobVote2023.MODID, pName));
    }

    private static TagKey<Biome> createBiomes(String pName) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MobVote2023.MODID, pName));
    }

    private static TagKey<Item> createItems(String pName) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MobVote2023.MODID, pName));
    }

    private static TagKey<EntityType<?>> createEntityTypes(String pName) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(MobVote2023.MODID, pName));
    }

}
