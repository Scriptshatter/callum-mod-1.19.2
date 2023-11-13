package scriptshatter.callum.items.upgradeableItems;

import net.minecraft.screen.slot.Slot;

public interface AccessWiden {
    Slot get_slot(double x, double y);
    void scroll_mouse(double mouseX, double mouseY, double amount);
}
