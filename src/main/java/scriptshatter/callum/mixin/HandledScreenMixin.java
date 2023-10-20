package scriptshatter.callum.mixin;


import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scriptshatter.callum.items.ItemRegister;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {
    @Shadow
    protected Slot focusedSlot;
    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"))
    private void render_tooltip(MatrixStack matrices, int x, int y, CallbackInfo ci){
        if (this.focusedSlot != null && this.focusedSlot.getStack().getItem().getDefaultStack().isItemEqual(ItemRegister.CALLUM_PILOT.getDefaultStack())) {
            this.renderTooltip(matrices, this.focusedSlot.getStack(), x, y);
        }
    }

}
