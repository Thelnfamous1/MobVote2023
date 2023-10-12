package me.infamous.mob_vote_five.common.registry;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.effect.MVMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MVMobEffects {

    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MobVote2023.MODID);


    public static final RegistryObject<MobEffect> PENGUINS_GRACE = MOB_EFFECTS.register("penguins_grace", () -> new MVMobEffect(MobEffectCategory.BENEFICIAL, 8954814));

    public static void register(IEventBus modEventBus){
        MOB_EFFECTS.register(modEventBus);
    }
}
