package scriptshatter.callum.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/screen/PlayerScreenHandler$1")
public abstract class PlayerScreenHandlerMixin extends Slot {

    public PlayerScreenHandlerMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(method = "canInsert(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void preventArmorInsertion(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
/*        PlayerEntity player = ((PlayerInventory)inventory).player;
        if(!player.getUuid().equals(UUID.fromString("2f16fc74-cfb4-433a-97e2-b67173dc8dae")) && stack.getItem() instanceof Cap_item){
            info.setReturnValue(false);
        }*/
    }
}
