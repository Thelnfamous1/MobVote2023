package me.infamous.mob_vote_five.common.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import me.infamous.mob_vote_five.common.registry.MVItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MobVote2023.MODID)
public class DataGenHandler {

    @SubscribeEvent
    static void onDataGen(GatherDataEvent event){
        boolean client = event.includeClient();
        boolean server = event.includeServer();

        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        addVirtualPackContents(existingFileHelper);
        generator.addProvider(client, new LanguageProvider(generator, MobVote2023.MODID, "en_us") {
            @Override
            protected void addTranslations() {
                this.add(MVEntityTypes.ARMADILLO.get(), "Armadillo");
                this.add(MVEntityTypes.CRAB.get(), "Crab");
                this.add(MVEntityTypes.PENGUIN.get(), "Penguin");
                this.add(MVItems.ARMADILLO_SPAWN_EGG.get(), "Armadillo Spawn Egg");
                this.add(MVItems.CRAB_SPAWN_EGG.get(), "Crab Spawn Egg");
                this.add(MVItems.PENGUIN_SPAWN_EGG.get(), "Penguin Spawn Egg");
                this.add(MVItems.ARMADILLO_SCUTE.get(), "Armadillo Scute");
                this.add(MVItems.CRAB_CLAW.get(), "Crab Claw");
            }
        });

        generator.addProvider(client, new ItemModelProvider(generator, MobVote2023.MODID, existingFileHelper) {
            @Override
            protected void registerModels() {
                this.spawnEgg(MVItems.ARMADILLO_SPAWN_EGG.getId().getPath());
                this.spawnEgg(MVItems.CRAB_SPAWN_EGG.getId().getPath());
                this.spawnEgg(MVItems.PENGUIN_SPAWN_EGG.getId().getPath());
                this.basicItem(MVItems.ARMADILLO_SCUTE.get());
            }

            private void spawnEgg(String name) {
                this.withExistingParent(name, "item/template_spawn_egg");
            }
        });
        generator.addProvider(server, new LootTableProvider(generator){
            @Override
            protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
                // do not validate against all registered loot tables
            }

            @Override
            protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
                return ImmutableList.of(Pair.of(() -> new EntityLoot(){

                    @Override
                    protected void addTables() {
                        this.add(MVEntityTypes.ARMADILLO.get(), LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(MVItems.ARMADILLO_SCUTE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
                        this.add(MVEntityTypes.CRAB.get(), LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                        .add(LootItem.lootTableItem(MVItems.CRAB_CLAW.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
                        this.add(MVEntityTypes.PENGUIN.get(), LootTable.lootTable());
                    }

                    @Override
                    protected Iterable<EntityType<?>> getKnownEntities() {
                        return MVEntityTypes.getKnownEntities();
                    }
                }, LootContextParamSets.ENTITY));
            }
        });

        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, MobVote2023.MODID, existingFileHelper){
            @Override
            protected void addTags() {
                this.tag(MVTags.PENGUINS_SPAWNABLE_ON).add(Blocks.GRASS_BLOCK, Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.SAND);
                this.tag(MVTags.CRABS_SPAWNABLE_ON).add(Blocks.GRASS_BLOCK, Blocks.MUD, Blocks.MANGROVE_ROOTS, Blocks.MUDDY_MANGROVE_ROOTS);

            }
        };
        generator.addProvider(server, blockTagsProvider);

        generator.addProvider(server, new ItemTagsProvider(generator, blockTagsProvider, MobVote2023.MODID, existingFileHelper){
            @Override
            protected void addTags() {
                this.tag(MVTags.CRAB_FOOD).add(Blocks.SEAGRASS.asItem());
                this.tag(MVTags.PENGUIN_FOOD).addTag(ItemTags.FISHES);
                this.tag(MVTags.ARMADILLO_FOOD).add(Items.SPIDER_EYE);
            }
        });

        generator.addProvider(server, new EntityTypeTagsProvider(generator, MobVote2023.MODID, existingFileHelper){

            @Override
            protected void addTags() {
                this.tag(MVTags.PENGUIN_EGG_IGNORES_FALLING).add(EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_VILLAGER);
                this.tag(MVTags.PENGUIN_EGG_IGNORES).add(EntityType.BAT);
            }
        });

        generator.addProvider(server, new BiomeTagsProvider(generator, MobVote2023.MODID, existingFileHelper){

            @Override
            protected void addTags() {
            }
        });
    }

    // tell the file helper that these resources exist, even if they don't - good for things like textures that haven't been added yet
    private static void addVirtualPackContents(ExistingFileHelper existingFileHelper) {
        existingFileHelper.trackGenerated(new ResourceLocation(MobVote2023.MODID, "armadillo_scute"), PackType.CLIENT_RESOURCES, ".png", "textures/item");
    }
}
