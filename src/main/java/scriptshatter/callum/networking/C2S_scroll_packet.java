package scriptshatter.callum.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

public class C2S_scroll_packet {

    public static void register(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        int slotIndex = packetByteBuf.readInt();
        int direction = packetByteBuf.readInt();
        Slot slot = serverPlayerEntity.currentScreenHandler.getSlot(slotIndex);
        ItemStack stack = slot.getStack();
        if(stack.getItem() instanceof IUpgradeableItem upgradeableItem){
            upgradeableItem.scroll_selected_slot(stack, -direction);
        }
    }

    public static void scroll_client(int slot_index, int direction){
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        packetByteBuf.writeInt(slot_index);
        packetByteBuf.writeInt(direction);

        ClientPlayNetworking.send(Post_office.SCROLL_ID, packetByteBuf);
    }
}
