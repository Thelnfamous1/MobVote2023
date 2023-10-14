package me.infamous.mob_vote_five.common.network;

import me.infamous.mob_vote_five.MobVote2023;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class MVNetwork {
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(MobVote2023.MODID, "sync_channel");
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.newSimpleChannel(
            CHANNEL_NAME, () -> "1.0",
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int INDEX = 0;

    public static void init() {
        SYNC_CHANNEL.registerMessage(INDEX++, ServerboundKickArmadilloPacket.class, ServerboundKickArmadilloPacket::write, ServerboundKickArmadilloPacket::new, ServerboundKickArmadilloPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        SYNC_CHANNEL.registerMessage(INDEX++, ServerboundRetractGrapplingHookPacket.class, ServerboundRetractGrapplingHookPacket::write, ServerboundRetractGrapplingHookPacket::new, ServerboundRetractGrapplingHookPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}