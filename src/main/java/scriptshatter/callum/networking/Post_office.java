package scriptshatter.callum.networking;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;

public class Post_office {
    public static final Identifier SCROLL_ID = Callum.identifier("scroll");
    public static final Identifier MULTI_MINING = Callum.identifier("classes_multi_mining");
    public static final Identifier GET_PLAYER = Callum.identifier("get_player");

    public static void register_shitty_ass_mother_fucking_mail_C2S(){
        ServerPlayNetworking.registerGlobalReceiver(SCROLL_ID, C2S_scroll_packet::register);
        ServerPlayNetworking.registerGlobalReceiver(GET_PLAYER, C2S_grab_player::register);
        ServerPlayNetworking.registerGlobalReceiver(Callum.identifier("sync_trinkets"), (server, player, handler, buf, responseSender) -> {
            TrinketsApi.getTrinketComponent(player).ifPresent(TrinketComponent::update);
        });
    }

    public static void sync_trinkets(){
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        ClientPlayNetworking.send(Callum.identifier("sync_trinkets"), packetByteBuf);
    }

    public static void S2C(){
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(MULTI_MINING, Post_office::receiveMultiMine);
        }));
        ClientPlayNetworking.registerGlobalReceiver(Callum.identifier("sync_pls"), (client, handler, buf, responseSender) -> {
            TrinketsApi.getTrinketComponent(client.player).ifPresent(TrinketComponent::update);
        });
    }


    public static boolean isMultiMining;

    @Environment(EnvType.CLIENT)
    private static void receiveMultiMine(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean isMulti = packetByteBuf.readBoolean();
        minecraftClient.execute(() -> isMultiMining = isMulti);
    }
}
