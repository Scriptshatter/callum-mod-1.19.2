package scriptshatter.callum;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import scriptshatter.callum.armor.Goggles;
import scriptshatter.callum.armor.client.Cap_renderer;
import scriptshatter.callum.armor.client.Goggles_render;
import scriptshatter.callum.armor.client.Trinket_model_provider;
import scriptshatter.callum.items.Badge_item;
import scriptshatter.callum.items.ItemRegister;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.concurrent.atomic.AtomicInteger;

public class CallumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GeoArmorRenderer.registerArmorRenderer(new Cap_renderer(), ItemRegister.CALLUM_PILOT);
        TrinketRendererRegistry.registerRenderer(ItemRegister.CALLUM_GOGGLES, new Goggles_render());
        ModelLoadingRegistry.INSTANCE.registerModelProvider(new Trinket_model_provider());
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> {
            if(tintIndex != 1){
                return -1;
            }
            if (stack.getItem() instanceof Goggles upgradeable && !upgradeable.get_items_defaulted_list(stack).isEmpty()){
                AtomicInteger current_color = new AtomicInteger();
                upgradeable.get_items_defaulted_list(stack).forEach(badge -> {
                    if (badge.getItem() instanceof Badge_item badgeItem){
                        current_color.set(badgeItem.color);
                    }
                });
                return current_color.get();
            }
            return 0x999999;
        }), ItemRegister.CALLUM_GOGGLES);
    }
}
