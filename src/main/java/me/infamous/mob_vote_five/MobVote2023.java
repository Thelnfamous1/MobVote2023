package me.infamous.mob_vote_five;

import com.mojang.logging.LogUtils;
import me.infamous.mob_vote_five.common.registry.MVDataSerializers;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import me.infamous.mob_vote_five.common.registry.MVItems;
import me.infamous.mob_vote_five.common.registry.MVMobEffects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MobVote2023.MODID)
public class MobVote2023 {
    public static final String MODID = "mob_vote_five";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MobVote2023() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MVEntityTypes.register(modEventBus);
        MVItems.register(modEventBus);
        MVDataSerializers.register(modEventBus);
        MVMobEffects.register(modEventBus);
    }
}
