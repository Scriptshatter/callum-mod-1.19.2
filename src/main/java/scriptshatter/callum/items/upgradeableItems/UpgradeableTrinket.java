package scriptshatter.callum.items.upgradeableItems;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.api.*;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.LivingEntityMixin;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.networking.Post_office;

import java.util.*;

public abstract class UpgradeableTrinket extends TrinketItem implements IUpgradeableItem {
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
    public void update_powers(ItemStack upgradeable) {
    }

    // This adds and removes powers based on upgrades when you have the trinket on your person.
    @Override
    public void tick(ItemStack itemsStack, SlotReference slot, LivingEntity entity) {
/*        if(being_worn){
            ItemStack stack = slot.inventory().getStack(slot.index());
            PowerHolderComponent holder = PowerHolderComponent.KEY.get(entity);

            IUpgradeableItem.getUpgrades(stack).forEach((upgrade_slot, upgrade) -> {
                if (upgrade.getItem() instanceof Upgrade_item badgeItem) {
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

                IUpgradeableItem.getUpgrades(stack).forEach((upgrade_slot, upgrade) -> {
                    if (upgrade.getItem() instanceof Upgrade_item badgeItem) {
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
        }*/
    }

    // Basically enables the tick function to run, since sometimes it would run one more time after you had taken it off.

    // Removes the powers supplied by the trinkets upgrades when you de-equip it.
    // Also it slaps trinkets in the face and tells it the item was de-equipped, since if you spastically took the trinket on and off before, the server would sometimes think you still had it equipped.


    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        slot.inventory().setStack(slot.index(), ItemStack.EMPTY);
    }
}
