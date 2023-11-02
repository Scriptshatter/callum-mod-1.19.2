package scriptshatter.callum.items.badgeJson;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.LivingEntityMixin;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Badge_item;
import scriptshatter.callum.items.ItemRegister;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class UpgradeableTrinket extends TrinketItem implements UpgradeableItemTemplate {
    public UpgradeableTrinket(Settings settings) {
        super(settings);
    }

    private LivingEntity current_wearer;

    private ItemStack current_equipped;

    public UpgradeableTrinket(FabricItemSettings settings){
        super(settings);
        current_wearer = null;
        current_equipped = null;
    }

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

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        this.current_wearer = entity;
        PowerHolderComponent holder = PowerHolderComponent.KEY.get(current_wearer);


        this.get_items_defaulted_list(stack).forEach(upgrade -> {
            if (upgrade.getItem() instanceof Badge_item badgeItem) {
                String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                badgeItem.powers.forEach(powerID -> holder.removePower(PowerTypeRegistry.get(powerID), Callum.identifier("badge_type_" + upgradeGroup)));
            }
        });


        holder.getPowerTypes(true).forEach(powerType -> this.get_items_defaulted_list(stack).forEach(upgrade -> {
            if (upgrade.getItem() instanceof Badge_item badgeItem) {
                String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                AtomicBoolean marked = new AtomicBoolean();
                marked.set(true);
                badgeItem.powers.forEach(powerID -> {
                    if(powerType.getIdentifier().equals(powerID)){
                        marked.set(false);
                    }
                });
                if (marked.get()){
                    holder.removePower(powerType, Callum.identifier("badge_type_" + upgradeGroup));
                }
            }
        }));
        holder.sync();
    }

    @Override
    public void remove_upgrades(ItemStack upgrade, ItemStack upgradeable) {
    }

/*    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        this.current_wearer = entity;
        this.current_equipped = stack;
        if(stack.getItem() instanceof UpgradeableItemTemplate upgradeableItem){
            upgradeableItem.get_items_defaulted_list(stack).forEach(upgrade -> {
                if (upgrade.getItem() instanceof Badge_item badgeItem) {
                    String upgradeGroup = badgeItem.upgrade_group != null ? badgeItem.upgrade_group : "wildcard";
                    PowerHolderComponent holder = PowerHolderComponent.KEY.get(current_wearer);
                    badgeItem.powers.forEach(powerID -> holder.addPower(PowerTypeRegistry.get(powerID), Callum.identifier("badge_type_" + upgradeGroup)));
                    holder.sync();
                }
            });
        }
    }*/

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        this.current_wearer = null;
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
    }
}
