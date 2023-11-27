package scriptshatter.callum.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;
import scriptshatter.callum.items.upgradeableItems.UpgradeableTrinket;
import scriptshatter.callum.networking.Post_office;

import java.util.*;


@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(at = @At("TAIL"), method = "updateItems")
    private void updateItems(CallbackInfo info) {
        PowerHolderComponent holder = PowerHolderComponent.KEY.get(player);
        HashMap<Identifier, PowerType<?>> dont_remove = new HashMap<>();
        TrinketsApi.getTrinketComponent(player).ifPresent(trinkets ->
                trinkets.forEach((slotReference, itemStack) ->
                {
                    if(itemStack.getItem() instanceof UpgradeableTrinket){
                        IUpgradeableItem.getUpgrades(itemStack).forEach((slot, item) -> {
                            if(item.getItem() instanceof Upgrade_item upgradeItem){
                                String source_string = upgradeItem.upgrade_group == null ? "wildcard" : upgradeItem.upgrade_group;
                                Identifier source = Callum.identifier("badge_type_" + source_string);
                                upgradeItem.powers.forEach(powerID -> {
                                    dont_remove.put(source, PowerTypeRegistry.get(powerID));
                                    if(!holder.hasPower(PowerTypeRegistry.get(powerID))){
                                        holder.addPower(PowerTypeRegistry.get(powerID), source);
                                    }
                                });
                            }
                        });
                    }
                }));


        holder.getPowerTypes(true).forEach(powerType -> holder.getSources(powerType).forEach(identifier -> {
            if (Objects.equals(identifier.getNamespace(), "callum") && identifier.getPath().contains("badge_type_") && !dont_remove.containsKey(identifier) && !dont_remove.containsValue(powerType)){
                holder.removePower(powerType, identifier);
            }
        }));


    }
}
