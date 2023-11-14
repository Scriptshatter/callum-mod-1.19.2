package scriptshatter.callum.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
    }

    public static void S2C(){
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> {
            ClientPlayNetworking.registerReceiver(MULTI_MINING, Post_office::receiveMultiMine);
        }));
    }


    public static boolean isMultiMining;

    @Environment(EnvType.CLIENT)
    private static void receiveMultiMine(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean isMulti = packetByteBuf.readBoolean();
        minecraftClient.execute(() -> isMultiMining = isMulti);
    }
}
