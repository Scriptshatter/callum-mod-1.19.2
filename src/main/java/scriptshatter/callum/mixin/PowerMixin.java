package scriptshatter.callum.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.InvisibilityPower;
import io.github.apace100.apoli.power.ModelColorPower;
import io.github.apace100.apoli.power.Power;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scriptshatter.callum.powers.NoInvisPower;

@Environment(value= EnvType.CLIENT)
@Mixin(targets = "io/github/apace100/apoli/power/Power", remap = false)
public abstract class PowerMixin {
    @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
    void icanseeyou(CallbackInfoReturnable<Boolean> cir){
        //You dont know what you're talking about, InteliJ.
        if(!((Power)(Object)this instanceof NoInvisPower) && PowerHolderComponent.hasPower(MinecraftClient.getInstance().player, NoInvisPower.class) && ((Power)(Object)this instanceof InvisibilityPower || ((Power)(Object)this instanceof ModelColorPower modelColorPower && modelColorPower.getAlpha() < 0.5))){
            cir.setReturnValue(false);
        }
    }
}
