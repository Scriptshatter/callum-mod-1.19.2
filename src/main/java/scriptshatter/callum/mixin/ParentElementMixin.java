package scriptshatter.callum.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import scriptshatter.callum.items.upgradeableItems.AccessWiden;
import scriptshatter.callum.networking.C2S_scroll_packet;
@Environment(EnvType.CLIENT)
@Mixin(ParentElement.class)
public interface ParentElementMixin extends Element, AccessWiden {

    @Override
    default void scroll_mouse(double mouseX, double mouseY, double amount){
        if(this instanceof HandledScreen<?> handledScreen) {
            AccessWiden widener = (AccessWiden)handledScreen;
            Slot slot = widener.get_slot(mouseX, mouseY);
            if (slot != null) {
                C2S_scroll_packet.scroll_client(slot.id, amount > 0 ? 1 : -1);
            }
        }
    }
}
