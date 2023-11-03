package scriptshatter.callum.items.badgeJson;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.LivingEntityMixin;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Badge_item;
import scriptshatter.callum.items.ItemRegister;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UpgradeableTrinket extends TrinketItem implements UpgradeableItemTemplate {
    public UpgradeableTrinket(Settings settings) {
        super(settings.maxCount(1));
        being_worn = false;
    }

    private boolean being_worn;


    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        return this.onceStackClicked(stack, slot, clickType, player);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return this.onceClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        this.appendTheTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return this.getTheTooltipData(stack);
    }

    @Override
    public void add_upgrades(ItemStack upgrade, ItemStack upgradeable) {
    }

    // This adds and removes powers based on upgrades when you have the trinket on your person.
    @Override
    public void tick(ItemStack itemsStack, SlotReference slot, LivingEntity entity) {
        if(being_worn){
            ItemStack stack = slot.inventory().getStack(slot.index());
            PowerHolderComponent holder = PowerHolderComponent.KEY.get(entity);

            this.get_items_defaulted_list(stack).forEach(upgrade -> {
                if (upgrade.getItem() instanceof Badge_item badgeItem) {
                    String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                    badgeItem.powers.forEach(powerID -> holder.addPower(PowerTypeRegistry.get(powerID), Callum.identifier("badge_type_" + upgradeGroup)));
                }
            });

            HashMap<PowerType<?>, List<Identifier>> marked_powers = new HashMap<>();

            holder.getPowerTypes(true).forEach(powerType -> {
                List<Identifier> marked_sources = new LinkedList<>();
                holder.getSources(powerType).forEach(identifier -> {
                    if (Objects.equals(identifier.getNamespace(), "callum") && identifier.getPath().contains("badge_type_")){
                        marked_sources.add(identifier);
                    }
                });

                this.get_items_defaulted_list(stack).forEach(upgrade -> {
                    if (upgrade.getItem() instanceof Badge_item badgeItem) {
                        String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                        marked_sources.remove(Callum.identifier("badge_type_" + upgradeGroup));
                    }
                });

                if(!marked_sources.isEmpty()){
                    marked_powers.put(powerType, marked_sources);
                }
            });

            marked_powers.forEach((powerType, identifiers) -> {
                identifiers.forEach(identifier -> holder.removePower(powerType, identifier));
            });
            holder.sync();
        }
    }

    @Override
    public void remove_upgrades(ItemStack upgrade, ItemStack upgradeable) {
    }

    // Basically enables the tick function to run, since sometimes it would run one more time after you had taken it off.
    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        being_worn = true;
    }

    // Removes the powers supplied by the trinkets upgrades when you de-equip it.
    // Also it slaps trinkets in the face and tells it the item was de-equipped, since if you spastically took the trinket on and off before, the server would sometimes think you still had it equipped.
    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        being_worn = false;
        if(stack.getItem() instanceof UpgradeableItemTemplate upgradeableItem){
            upgradeableItem.get_items_defaulted_list(stack).forEach(upgrade -> {
                if (upgrade.getItem() instanceof Badge_item badgeItem) {
                    String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                    PowerHolderComponent holder = PowerHolderComponent.KEY.get(entity);
                    badgeItem.powers.forEach(powerID -> holder.removePower(PowerTypeRegistry.get(powerID), Callum.identifier("badge_type_" + upgradeGroup)));
                    holder.sync();
                }
            });
        }
        slot.inventory().removeStack(slot.index());
    }


}
