package scriptshatter.callum.mixin;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scriptshatter.callum.items.upgradeableItems.Callum_tooltip_component;
import scriptshatter.callum.items.upgradeableItems.Callum_tooltip_data;

@Mixin(TooltipComponent.class)
public interface ExampleMixin {
	@Inject(method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;", at = @At("HEAD"), cancellable = true)
	private static void render_tooltip(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir){
		if (data instanceof Callum_tooltip_data) {
			cir.setReturnValue(new Callum_tooltip_component((Callum_tooltip_data)data));
		}
	}
}