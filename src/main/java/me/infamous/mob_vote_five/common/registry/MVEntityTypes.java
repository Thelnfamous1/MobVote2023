package me.infamous.mob_vote_five.common.registry;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import me.infamous.mob_vote_five.common.entity.Crab;
import me.infamous.mob_vote_five.common.entity.Penguin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class MVEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MobVote2023.MODID);

    public static final RegistryObject<EntityType<Armadillo>> ARMADILLO = register("armadillo",
            EntityType.Builder.of(Armadillo::new, MobCategory.CREATURE)
                    .sized(0.95F, 0.95F)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<Crab>> CRAB = register("crab",
            EntityType.Builder.of(Crab::new, MobCategory.CREATURE)
                    .sized(0.95F, 0.5F)
                    .clientTrackingRange(10));
    public static final RegistryObject<EntityType<Penguin>> PENGUIN = register("penguin",
            EntityType.Builder.of(Penguin::new, MobCategory.CREATURE)
                    .sized(0.7F, 1.2F)
                    .clientTrackingRange(10));

    public static Iterable<EntityType<?>> getKnownEntities(){
        return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String pKey, EntityType.Builder<T> pBuilder) {
        return ENTITY_TYPES.register(pKey, () -> pBuilder.build(new ResourceLocation(MobVote2023.MODID, pKey).toString()));
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
