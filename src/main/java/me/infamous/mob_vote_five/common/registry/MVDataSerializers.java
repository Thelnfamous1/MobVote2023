package me.infamous.mob_vote_five.common.registry;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.ai.hider.Hider;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MVDataSerializers {

    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MobVote2023.MODID);

    public static final RegistryObject<EntityDataSerializer<Hider.HideState>> DIG_STATE = DATA_SERIALIZERS.register("hide_state", () -> EntityDataSerializer.simpleEnum(Hider.HideState.class));


    public static void register(IEventBus modEventBus){
        DATA_SERIALIZERS.register(modEventBus);
    }
}
