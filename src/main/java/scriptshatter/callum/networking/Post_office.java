package scriptshatter.callum.networking;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

public class Post_office {
    public static final Identifier SCROLL_ID = Callum.identifier("scroll");
    public static final Identifier MULTI_MINING = Callum.identifier("classes_multi_mining");
    public static final Identifier GET_PLAYER = Callum.identifier("get_player");

    public static void register_shitty_ass_mother_fucking_mail_C2S(){
        ServerPlayNetworking.registerGlobalReceiver(SCROLL_ID, C2S_scroll_packet::register);
        ServerPlayNetworking.registerGlobalReceiver(GET_PLAYER, C2S_grab_player::register);
        ServerPlayNetworking.registerGlobalReceiver(Callum.identifier("sync_trinkets"), (server, player, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            TrinketsApi.TRINKET_COMPONENT.get(player).getAllEquipped().forEach(pair -> {
                if(pair.getRight().isItemEqual(stack)){
                    SlotReference slot = pair.getLeft();
                    pair.getLeft().inventory().setStack(slot.index(), ItemStack.EMPTY);
                    PowerHolderComponent holder = PowerHolderComponent.KEY.get(player);
                    IUpgradeableItem.getUpgrades(stack).forEach((slotNum, item) -> {
                        if(item.getItem() instanceof Upgrade_item upgradeItem){
                            String source_string = upgradeItem.upgrade_group == null ? "wildcard" : upgradeItem.upgrade_group;
                            Identifier source = Callum.identifier("badge_type_" + source_string);
                            upgradeItem.powers.forEach(powerID -> {
                                if(holder.hasPower(PowerTypeRegistry.get(powerID))){
                                    holder.removePower(PowerTypeRegistry.get(powerID), source);
                                }
                            });
                        }
                    });
                    slot.inventory().removeStack(slot.index());
                }
            });
        });
    }

    public static void sync_trinkets(ItemStack stack){
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        packetByteBuf.writeItemStack(stack);

        ClientPlayNetworking.send(Callum.identifier("sync_trinkets"), packetByteBuf);
    }

    public static void S2C(){
        ClientPlayConnectionEvents.INIT.register(((clientPlayNetworkHandler, minecraftClient) -> ClientPlayNetworking.registerReceiver(MULTI_MINING, Post_office::receiveMultiMine)));
    }


    public static boolean isMultiMining;

    @Environment(EnvType.CLIENT)
    private static void receiveMultiMine(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean isMulti = packetByteBuf.readBoolean();
        minecraftClient.execute(() -> isMultiMining = isMulti);
    }
}
