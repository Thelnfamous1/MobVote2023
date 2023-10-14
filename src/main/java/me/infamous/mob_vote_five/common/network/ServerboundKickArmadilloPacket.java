package me.infamous.mob_vote_five.common.network;

import me.infamous.mob_vote_five.common.entity.Armadillo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundKickArmadilloPacket {

    private final int id;

    public ServerboundKickArmadilloPacket(Armadillo armadillo){
        this.id = armadillo.getId();
    }

    public ServerboundKickArmadilloPacket(FriendlyByteBuf byteBuf){
        this.id = byteBuf.readVarInt();
    }

    public static void write(ServerboundKickArmadilloPacket packet, FriendlyByteBuf byteBuf){
        byteBuf.writeVarInt(packet.id);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            Entity entity = sender.level.getEntity(this.id);
            if(entity instanceof Armadillo armadillo && !armadillo.isRevealed()){
                armadillo.applyCombatKnockback(sender, 4.0F);
                armadillo.setRot(sender.getYRot(), sender.getXRot());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
