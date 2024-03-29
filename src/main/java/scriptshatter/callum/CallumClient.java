package scriptshatter.callum;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import scriptshatter.callum.armor.Goggles;
import scriptshatter.callum.armor.client.Cap_renderer;
import scriptshatter.callum.armor.client.Goggles_render;
import scriptshatter.callum.armor.client.Trinket_model_provider;
import scriptshatter.callum.items.ItemRegister;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;
import scriptshatter.callum.networking.Post_office;
import scriptshatter.callum.sound.CallumSounds;

import java.util.concurrent.atomic.AtomicInteger;

public class CallumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ArmorRenderer.register(new Cap_renderer(), ItemRegister.CALLUM_PILOT);
        TrinketRendererRegistry.registerRenderer(ItemRegister.CALLUM_GOGGLES, new Goggles_render());

        ModelLoadingRegistry.INSTANCE.registerModelProvider(new Trinket_model_provider());
        Post_office.S2C();
        CallumSounds.veg();
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> {
            if(tintIndex != 1){
                return -1;
            }
            if (stack.getItem() instanceof Goggles && !IUpgradeableItem.getUpgrades(stack).isEmpty()){
                AtomicInteger current_color = new AtomicInteger();
                IUpgradeableItem.getUpgrades(stack).forEach((upgrade_slot, badge) -> {
                    if (badge.getItem() instanceof Upgrade_item badgeItem){
                        current_color.set(badgeItem.color);
                    }
                });
                return current_color.get();
            }
            return 0x00999999;
        }), ItemRegister.CALLUM_GOGGLES);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if(tintIndex != 0){
                return -1;
            }
            if (stack.getItem() instanceof Upgrade_item upgrade){
                return upgrade.color;
            }
            return 0x999999;
        }, ItemRegister.PLACEHOLDER_PIN,
                ItemRegister.SPACE_PIN,
                ItemRegister.FINS_PIN,
                ItemRegister.DIVING_PIN,
                ItemRegister.SPEED_PIN,
                ItemRegister.CLOUD_PIN,
                ItemRegister.KICKOFF_PIN,
                ItemRegister.WINTER_PIN,
                ItemRegister.BLAST_PIN,
                ItemRegister.MAGNET_PIN,
                ItemRegister.PRICKLY_PIN,
                ItemRegister.BONE_PIN,
                ItemRegister.LUMBERJACK_PIN,
                ItemRegister.DWARF_PIN,
                ItemRegister.STAR_PIN,
                ItemRegister.TRUTH_LENS,
                ItemRegister.WEALTH_LENS,
                ItemRegister.VISION_LENS,
                ItemRegister.SHADOW_LENS);
    }
}
