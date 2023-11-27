package scriptshatter.callum.networking;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import scriptshatter.callum.powers.InvisEquipmentPower;

import java.util.UUID;

public class C2S_grab_player {

    public static void register(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        UUID uuid = packetByteBuf.readUuid();

        ServerPlayerEntity serverPlayerEntity1 = minecraftServer.getPlayerManager().getPlayer(uuid);
        if(serverPlayerEntity1 != null){
            packetByteBuf.writeBoolean(PowerHolderComponent.hasPower(serverPlayerEntity1, InvisEquipmentPower.class));
        }
        else {
            packetByteBuf.writeBoolean(false);
        }
    }
}
