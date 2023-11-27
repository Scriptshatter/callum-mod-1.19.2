package scriptshatter.callum.items.upgradeableItems;

import com.google.common.collect.Multimap;
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
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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


    //Removes powers. This will also remove the item on unequip, since it was only running it on the client side at times.
    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.world.isClient()){
            Post_office.sync_trinkets(stack);
        }
        else{
            PowerHolderComponent holder = PowerHolderComponent.KEY.get(entity);
            IUpgradeableItem.getUpgrades(stack).forEach((slotNum, item) -> {
                if(item.getItem() instanceof Upgrade_item upgradeItem){
                    String source_string = upgradeItem.upgrade_group == null ? "wildcard" : upgradeItem.upgrade_group;
                    Identifier source = Callum.identifier("badge_type_" + source_string);
                    upgradeItem.powers.forEach(powerID -> {
                        if(holder.hasPower(PowerTypeRegistry.get(powerID))){
                            holder.removePower(PowerTypeRegistry.get(powerID), source);
                        }
                    });
                }
            });
            slot.inventory().removeStack(slot.index());
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        PowerHolderComponent holder = PowerHolderComponent.KEY.get(entity);
        IUpgradeableItem.getUpgrades(stack).forEach((slotNum, item) -> {
            if(item.getItem() instanceof Upgrade_item upgradeItem){
                String source_string = upgradeItem.upgrade_group == null ? "wildcard" : upgradeItem.upgrade_group;
                Identifier source = Callum.identifier("badge_type_" + source_string);
                upgradeItem.powers.forEach(powerID -> {
                    if(!holder.hasPower(PowerTypeRegistry.get(powerID))){
                        holder.addPower(PowerTypeRegistry.get(powerID), source);
                    }
                });
            }
        });
        return super.getModifiers(stack, slot, entity, uuid);
    }
}
