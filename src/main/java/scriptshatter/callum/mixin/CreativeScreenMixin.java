package scriptshatter.callum.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scriptshatter.callum.items.upgradeableItems.AccessWiden;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;
import scriptshatter.callum.networking.C2S_scroll_packet;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> implements AccessWiden {

    public CreativeScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Shadow protected abstract boolean isCreativeInventorySlot(@Nullable Slot slot);

    @Shadow private static int selectedTab;

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    void cancel_if_upgradeable(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir){
        AccessWiden widener = (AccessWiden)this;
        Slot slot = widener.get_slot(mouseX, mouseY);
        if (slot != null && slot.getStack().getItem() instanceof IUpgradeableItem){
            cir.setReturnValue(false);
        }
    }

    @Override
    public void scroll_mouse(double mouseX, double mouseY, double amount){
        AccessWiden widener = (AccessWiden)this;
        Slot slot = widener.get_slot(mouseX, mouseY);
        if (slot != null && !isCreativeInventorySlot(slot)) {
            if(selectedTab == ItemGroup.INVENTORY.getIndex()){
                C2S_scroll_packet.scroll_client(((CreativeInventoryScreen.CreativeSlot)slot).slot.id, amount > 0 ? 1 : -1);
            }
            else{
                C2S_scroll_packet.scroll_client(slot.id - ((CreativeInventoryScreen.CreativeScreenHandler)this.handler).slots.size() + 9 + 36, amount > 0 ? 1 : -1);
            }
        }
    }
}
