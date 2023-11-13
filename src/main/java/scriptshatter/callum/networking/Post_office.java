package scriptshatter.callum.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;

public class Post_office {
    public static final Identifier SCROLL_ID = Callum.identifier("scroll");

    public static void register_shitty_ass_mother_fucking_mail_C2S(){
        ServerPlayNetworking.registerGlobalReceiver(SCROLL_ID, C2S_scroll_packet::register);
    }
}
