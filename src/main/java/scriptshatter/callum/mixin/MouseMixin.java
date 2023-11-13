package scriptshatter.callum.mixin;

import dev.emi.trinkets.mixin.HandledScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import scriptshatter.callum.items.upgradeableItems.AccessWiden;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onMouseScroll", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseScrolled(DDD)Z"))
    void call_scroll(long window, double horizontal, double vertical, CallbackInfo ci, double d, double e, double f){
        AccessWiden accessWiden = (AccessWiden) this.client.currentScreen;
        if(accessWiden != null){
            accessWiden.scroll_mouse(e, f, d);
        }
    }

}
