package scriptshatter.callum.mixin;


import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scriptshatter.callum.items.upgradeableItems.AccessWiden;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>, AccessWiden {


    @Shadow
    protected Slot focusedSlot;
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private Slot getSlotAt(double x, double y) {
        return null;
    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"))
    private void render_tooltip(MatrixStack matrices, int x, int y, CallbackInfo ci){
        if (this.focusedSlot != null && this.focusedSlot.getStack().getItem() instanceof IUpgradeableItem) {
            this.renderTooltip(matrices, this.focusedSlot.getStack(), x, y);
        }
    }

    @Override
    public Slot get_slot(double x, double y) {
        return getSlotAt(x, y);
    }
}
