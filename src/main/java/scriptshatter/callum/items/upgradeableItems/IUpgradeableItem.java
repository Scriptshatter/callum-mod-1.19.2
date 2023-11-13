package scriptshatter.callum.items.upgradeableItems;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.apace100.origins.component.OriginComponent;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.ItemRegister;
import scriptshatter.callum.items.Upgrade_item;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public interface IUpgradeableItem {
    //onClicked, onStackClicked, getTooltipData, appendTooltip

    EquipmentSlot itemSlot();
    String upgrade_type();


    //Does not need to be changed
    default boolean onceClicked(ItemStack upgradeable, ItemStack upgrade, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (has_upgrade_in_slot(upgradeable, get_selected_slot(upgradeable)) && can_enter(cursorStackReference.get(), this.get_id(), upgradeable)) {
                remove_upgrade(upgradeable).ifPresent((removedStack) -> {
                    this.playRemoveOneSound(player);
                    addUpgrade(upgradeable, upgrade);
                    cursorStackReference.set(removedStack);
                });
            } else {
                int i = addUpgrade(upgradeable, upgrade);
                if (i > 0) {
                    this.playInsertSound(player);
                    upgrade.decrement(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }



    static boolean can_enter(ItemStack itemStack, Identifier upgrade_item, ItemStack upgradeable){
        AtomicBoolean can_enter = new AtomicBoolean();
        can_enter.set(false);
        HashMap<Integer, ItemStack> upgrades = getUpgrades(upgradeable);
        if (itemStack.getItem() instanceof Upgrade_item upgrade) {
            can_enter.set(upgrade.valid_armor == null || (Objects.equals(upgrade.valid_armor, upgrade_item)));
            if (can_enter.get()) {
                upgrades.forEach((slot, upgradeInstance) -> {
                    if (upgradeInstance.getItem() instanceof Upgrade_item upgradeTemp && can_enter.get() && upgradeable.getItem() instanceof IUpgradeableItem upgradeableItem && !(upgradeableItem.get_selected_slot(upgradeable) == slot)) {
                        can_enter.set(upgrade.upgrade_group == null || !upgrade.upgrade_group.equals(upgradeTemp.upgrade_group));
                    }
                });
            }
        }
        return can_enter.get() || itemStack.isEmpty();
    }

    Identifier get_id();

    default boolean onceStackClicked(ItemStack upgradeable, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT ) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (has_upgrade_in_slot(upgradeable, get_selected_slot(upgradeable)) && can_enter(itemStack, this.get_id(), upgradeable)) {
                this.playRemoveOneSound(player);
                addUpgrade(upgradeable, itemStack);
                remove_upgrade(upgradeable).ifPresent((removedStack) -> {
                    addUpgrade(upgradeable, itemStack);
                    slot.takeStack(itemStack.getCount());
                    slot.insertStack(removedStack);
                });
            } else if (itemStack.getItem().canBeNested() && upgradeable.getItem() instanceof IUpgradeableItem upgradeableThing && can_enter(itemStack, upgradeableThing.get_id(),upgradeable)) {
                int i = (upgradeableThing.getUpgrade_cap() - getUpgradeCount(upgradeable));
                int j = addUpgrade(upgradeable, slot.takeStackRange(1, i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    default int get_selected_slot(ItemStack upgradeable){
        NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
        if(!nbtCompound.contains("SelectedSlot")){
            nbtCompound.put("SelectedSlot", NbtInt.of(1));
        }
        if(nbtCompound.getInt("SelectedSlot") > this.getUpgrade_cap() || nbtCompound.getInt("SelectedSlot") <= 0){
            nbtCompound.putInt("SelectedSlot", 1);
        }
        return nbtCompound.getInt("SelectedSlot");
    }

    default void scroll_selected_slot(ItemStack upgradeable, int value){
        NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
        int old_slot = get_selected_slot(upgradeable);
        if(old_slot + value > this.getUpgrade_cap()){
            nbtCompound.putInt("SelectedSlot", 1);
        }
        else if(old_slot + value <= 0) {
            nbtCompound.putInt("SelectedSlot", this.getUpgrade_cap());
        }
        else {
            nbtCompound.putInt("SelectedSlot", old_slot + value);
        }
    }

    private static int addUpgrade(ItemStack upgradeable, ItemStack upgrade) {
        if (!upgrade.isEmpty() && upgrade.getItem() instanceof Upgrade_item && upgradeable.getItem() instanceof IUpgradeableItem upgradeableItem && can_enter(upgrade, upgradeableItem.get_id(), upgradeable)) {
            NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
            int slot = upgradeableItem.get_selected_slot(upgradeable);
            if (!nbtCompound.contains("Upgrades")) {
                nbtCompound.put("Upgrades", new NbtList());
            }

            int i = getUpgradeCount(upgradeable); // Zingleborp


            if (i >= upgradeableItem.getUpgrade_cap()) {
                return 0;
            }

            else {
                NbtList nbt_upgrades = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
                ItemStack upgrade_storing = upgrade.copy();
                NbtCompound upgrade_pair = new NbtCompound();
                NbtCompound upgrade_compound = new NbtCompound();
                upgrade_storing.writeNbt(upgrade_compound);
                upgrade_pair.putInt("Slot", slot);
                upgrade_pair.put("Upgrade", upgrade_compound);
                nbt_upgrades.add(0, upgrade_pair);
                upgradeableItem.update_powers(upgradeable);

                return 1;
            }


        }

        else {
            return 0;
        }
    }

    static boolean has_upgrade_in_slot(ItemStack upgradeable, int slot){
        if(upgradeable.getItem() instanceof IUpgradeableItem && upgradeable.hasNbt()){
            assert upgradeable.getNbt() != null;
            NbtList upgrades = upgradeable.getNbt().getList("Upgrades", NbtElement.COMPOUND_TYPE);
            AtomicReference<NbtCompound> slot_worth_looking_into = new AtomicReference<>(new NbtCompound());
            upgrades.forEach(nbtElement -> {
                if(nbtElement instanceof NbtCompound nbtCompound && nbtCompound.getInt("Slot") == slot){
                    slot_worth_looking_into.set(nbtCompound);

                }
            });
            return slot_worth_looking_into.get().contains("Slot") && !ItemStack.fromNbt(slot_worth_looking_into.get().getCompound("Upgrade")).isEmpty();
        }
        return false;
    }

    static NbtCompound get_compound_from_slot(ItemStack upgradeable, int slot){
        if(upgradeable.getItem() instanceof IUpgradeableItem && upgradeable.hasNbt()){
            assert upgradeable.getNbt() != null;
            NbtList upgrades = upgradeable.getNbt().getList("Upgrades", NbtElement.COMPOUND_TYPE);
            AtomicReference<NbtCompound> slot_worth_looking_into = new AtomicReference<>(new NbtCompound());
            upgrades.forEach(nbtElement -> {
                if(nbtElement instanceof NbtCompound nbtCompound && nbtCompound.getInt("Slot") == slot){
                    slot_worth_looking_into.set(nbtCompound);
                }
            });
            return has_upgrade_in_slot(upgradeable, slot) ? slot_worth_looking_into.get() : null;
        }
        return null;
    }

    static boolean dropAllUpgrades(ItemStack upgradeable, PlayerEntity player) {
        NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
        if (!nbtCompound.contains("Upgrades")) {
            return false;
        } else {
            if (player instanceof ServerPlayerEntity) {
                NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);

                for(int i = 0; i < nbtList.size(); ++i) {
                    NbtCompound nbtCompound2 = nbtList.getCompound(i);
                    ItemStack itemStack = ItemStack.fromNbt(nbtCompound2.getCompound("Upgrade"));
                    player.dropItem(itemStack, true);
                }
            }

            upgradeable.removeSubNbt("Upgrades");
            ((IUpgradeableItem)upgradeable.getItem()).update_powers(upgradeable);
            return true;
        }
    }

    default boolean has_power(ItemStack upgradeable, Identifier powerId){
        AtomicBoolean has_power = new AtomicBoolean(false);
        StackPowerUtil.getPowers(upgradeable, itemSlot()).forEach(stackPower -> has_power.set(stackPower.powerId.equals(powerId)));
        return has_power.get();
    }

    default void update_powers(ItemStack upgradeable){
        StackPowerUtil.getPowers(upgradeable, this.itemSlot()).forEach(stackPower -> StackPowerUtil.removePower(upgradeable, stackPower.slot, stackPower.powerId));
        getUpgrades(upgradeable).forEach((slot, upgradeItem) -> {
            if(upgradeItem.getItem() instanceof Upgrade_item upgrade_item){
                upgrade_item.powers.forEach(power -> {
                    if(!this.has_power(upgradeable, power)){
                        StackPowerUtil.addPower(upgradeable, itemSlot(), power, true, false);
                    }
                });
            }
        });
    }
    //Gets the max amount of upgrades
    int getUpgrade_cap();
    //removes the first itemstack

    private static Optional<ItemStack> remove_upgrade(ItemStack upgradeable) {
        if(upgradeable.getItem() instanceof IUpgradeableItem upgradeableItem){
            int slot = upgradeableItem.get_selected_slot(upgradeable);
            NbtCompound nbtCompound = upgradeable.getOrCreateNbt();

            if (!nbtCompound.contains("Upgrades") || !has_upgrade_in_slot(upgradeable, slot)) {
                return Optional.empty();
            }
            else {
                NbtList nbt_upgrades = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
                NbtCompound upgrade_pair = get_compound_from_slot(upgradeable, slot);
                assert upgrade_pair != null;
                ItemStack upgrade_retrieved = ItemStack.fromNbt(upgrade_pair.getCompound("Upgrade"));
                nbt_upgrades.remove(upgrade_pair);
                upgradeableItem.update_powers(upgradeable);
                return Optional.of(upgrade_retrieved);
            }
        }
        return Optional.of(ItemStack.EMPTY);
    }
    //returns the amount of stacks within the item
    private static int getUpgradeCount(ItemStack stack) {
        return getUpgrades(stack).size();
    }
    // Gets a list of all the stacks within the item.
    static HashMap<Integer, ItemStack> getUpgrades(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        HashMap<Integer, ItemStack> upgrades = new HashMap<>();
        if (nbtCompound == null) {
            return upgrades;
        } else {
            NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
            nbtList.forEach(nbtElement -> {
                if(nbtElement instanceof NbtCompound upgradeCompound && !ItemStack.fromNbt((NbtCompound) upgradeCompound.get("Upgrade")).isEmpty()){
                    upgrades.put(upgradeCompound.getInt("Slot"), ItemStack.fromNbt((NbtCompound) upgradeCompound.get("Upgrade")));
                }
            });
            Objects.requireNonNull(NbtCompound.class);
            return upgrades;
        }
    }

    default Optional<TooltipData> getTheTooltipData(ItemStack stack) {
        assert MinecraftClient.getInstance().player != null;
        return Optional.of(new Callum_tooltip_data(stack, MinecraftClient.getInstance().player.currentScreenHandler.getCursorStack()));
    }

    default void appendTheTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", getUpgradeCount(stack), getUpgrade_cap()).formatted(Formatting.AQUA));
        if(!IUpgradeableItem.getUpgrades(stack).isEmpty()){
            tooltip.add(Text.translatable("upgradeable.callum." + this.upgrade_type()).formatted(Formatting.GOLD));
            IUpgradeableItem.getUpgrades(stack).forEach((slot, upgrade) -> {
                tooltip.add(
                        Text.literal(" ")
                                .append(upgrade.getItem().getName())
                                .append(":")
                                .formatted(Formatting.GREEN));
                if(upgrade.getItem() instanceof Upgrade_item upgrade_item){
                    upgrade_item.powers.forEach(powerID -> {
                        if(PowerTypeRegistry.contains(powerID)){
                            PowerType<?> powerType = PowerTypeRegistry.get(powerID);
                            if(!powerType.isHidden()){
                                if(I18n.translate(powerType.getOrCreateNameTranslationKey()).contains("isBad")){
                                    Upgrade_item.splitString(I18n.translate(powerType.getOrCreateDescriptionTranslationKey()), 40).forEach(s -> {
                                        tooltip.add(
                                                Text.literal("  ")
                                                        .append(s)
                                                        .formatted(Formatting.RED));
                                    });
                                }
                                else {
                                    Upgrade_item.splitString(I18n.translate(powerType.getOrCreateDescriptionTranslationKey()), 40).forEach(s -> {
                                        tooltip.add(
                                                Text.literal("  ")
                                                        .append(s)
                                                        .formatted(Formatting.LIGHT_PURPLE));
                                    });
                                }

                            }
                        }
                    });
                }
            });
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
