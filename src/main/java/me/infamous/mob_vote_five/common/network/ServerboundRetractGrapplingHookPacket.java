package me.infamous.mob_vote_five.common.network;

import me.infamous.mob_vote_five.common.duck.Grappler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundRetractGrapplingHookPacket {

    public ServerboundRetractGrapplingHookPacket(){
    }

    public ServerboundRetractGrapplingHookPacket(FriendlyByteBuf byteBuf){
    }

    public static void write(ServerboundRetractGrapplingHookPacket packet, FriendlyByteBuf byteBuf){
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            ((Grappler)sender).setGHook(null);
        });
        ctx.get().setPacketHandled(true);
    }
}
