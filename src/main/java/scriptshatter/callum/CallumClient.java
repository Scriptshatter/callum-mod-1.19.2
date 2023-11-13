package scriptshatter.callum;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemConvertible;
import scriptshatter.callum.armor.Goggles;
import scriptshatter.callum.armor.client.Cap_renderer;
import scriptshatter.callum.armor.client.Goggles_render;
import scriptshatter.callum.armor.client.Trinket_model_provider;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.items.ItemRegister;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

import java.util.concurrent.atomic.AtomicInteger;

public class CallumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArmorRenderer.register(new Cap_renderer(), ItemRegister.CALLUM_PILOT);
        TrinketRendererRegistry.registerRenderer(ItemRegister.CALLUM_GOGGLES, new Goggles_render());
        ModelLoadingRegistry.INSTANCE.registerModelProvider(new Trinket_model_provider());
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
            return 0x999999;
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
                ItemRegister.STAR_PIN);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if(tintIndex != 0){
                        return -1;
                    }
                    if (stack.getItem() instanceof Upgrade_item upgrade){
                        return upgrade.color;
                    }
                    return 0x999999;
                }, ItemRegister.MOON_PIN);
    }
}
