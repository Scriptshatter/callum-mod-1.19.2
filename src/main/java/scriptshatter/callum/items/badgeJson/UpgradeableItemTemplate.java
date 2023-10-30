package scriptshatter.callum.items.badgeJson;

import io.github.apace100.apoli.util.StackPowerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public interface UpgradeableItemTemplate extends IAnimatable {
    //onClicked, onStackClicked, getTooltipData, appendTooltip

    @Override
    default void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 20, horse -> PlayState.STOP));
    }
    @Override
    default AnimationFactory getFactory() {
        return GeckoLibUtil.createFactory(this);
    }

    EquipmentSlot itemSlot();

    default boolean onceClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isEmpty()) {
                removeFirstStack(stack).ifPresent((itemStack) -> {
                    this.playRemoveOneSound(player);
                    cursorStackReference.set(itemStack);
                });
            } else {
                int i = addUpgrade(stack, otherStack);
                if (i > 0) {
                    this.playInsertSound(player);
                    otherStack.decrement(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    static boolean can_enter(ItemStack itemStack, Identifier upgrade_item, DefaultedList<ItemStack> upgrades){
        AtomicBoolean can_enter = new AtomicBoolean();
        can_enter.set(itemStack.getNbt() != null && (Objects.equals(Identifier.tryParse(itemStack.getNbt().getString("valid_armor")), upgrade_item)));
        if(can_enter.get()) {
            upgrades.forEach(upgrade -> {
                if (upgrade.getNbt() != null && Objects.equals(itemStack.getNbt().getString("upgrade_group"), upgrade.getNbt().getString("upgrade_group"))) {
                    can_enter.set(itemStack.getNbt().getString("upgrade_group").equals(""));
                }
            });
        }
        return can_enter.get() || itemStack.isEmpty();
    }

    Identifier get_id();

    default boolean onceStackClicked(ItemStack upgradeable, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT ) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeFirstStack(upgradeable).ifPresent((removedStack) -> addUpgrade(upgradeable, slot.insertStack(removedStack)));
            } else if (itemStack.getItem().canBeNested() && upgradeable.getItem() instanceof UpgradeableItemTemplate upgradeableThing && can_enter(itemStack, upgradeableThing.get_id(), upgradeableThing.get_items_defaulted_list(upgradeable))) {
                int i = (upgradeableThing.getUpgrade_cap() - getUpgradeCount(upgradeable));
                int j = addUpgrade(upgradeable, slot.takeStackRange(1, i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    private static int addUpgrade(ItemStack upgradeable, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canBeNested() && upgradeable.getItem() instanceof UpgradeableItemTemplate upgradeableItem && can_enter(stack, upgradeableItem.get_id(), upgradeableItem.get_items_defaulted_list(upgradeable))) {
            NbtCompound nbtCompound = upgradeable.getOrCreateNbt();
            if (!nbtCompound.contains("Upgrades")) {
                nbtCompound.put("Upgrades", new NbtList());
            }

            int i = getUpgradeCount(upgradeable);
            if (i >= upgradeableItem.getUpgrade_cap()) {
                return 0;
            } else {
                NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
                ItemStack itemStack2 = stack.copy();
                itemStack2.setCount(1);
                NbtCompound nbtCompound3 = new NbtCompound();
                itemStack2.writeNbt(nbtCompound3);
                nbtList.add(0, (NbtElement)nbtCompound3);
                StackPowerUtil.getPowers(stack, upgradeableItem.itemSlot()).forEach(stackPower -> StackPowerUtil.addPower(upgradeable, stackPower));

                return 1;
            }
        } else {
            return 0;
        }
    }
    //Gets the max amount of upgrades
    int getUpgrade_cap();
    //removes the first itemstack
    private static Optional<ItemStack> removeFirstStack(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (!nbtCompound.contains("Upgrades")) {
            return Optional.empty();
        } else {
            NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
            if (nbtList.isEmpty()) {
                return Optional.empty();
            } else {
                boolean i = false;
                NbtCompound nbtCompound2 = nbtList.getCompound(0);
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                nbtList.remove(0);
                if (nbtList.isEmpty()) {
                    stack.removeSubNbt("Upgrades");
                }
                if(itemStack.getNbt() != null && stack.getItem() instanceof UpgradeableItemTemplate upgradeable){
                    StackPowerUtil.getPowers(stack, upgradeable.itemSlot()).forEach(stackPower -> StackPowerUtil.removePower(stack, stackPower.slot, stackPower.powerId));
                }
                return Optional.of(itemStack);
            }
        }
    }
    //returns the amount of stacks within the item
    private static int getUpgradeCount(ItemStack stack) {
        return (int) getUpgrades(stack).count();
    }
    // Gets a list of all the stacks within the item.
    private static Stream<ItemStack> getUpgrades(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return Stream.empty();
        } else {
            NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
            Stream<NbtElement> var10000 = nbtList.stream();
            Objects.requireNonNull(NbtCompound.class);
            return var10000.map(NbtCompound.class::cast).map(ItemStack::fromNbt);
        }
    }

    default DefaultedList<ItemStack> get_items_defaulted_list(ItemStack itemStack){
        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
        Stream<ItemStack> var10000 = getUpgrades(itemStack);
        Objects.requireNonNull(defaultedList);
        var10000.forEach(defaultedList::add);
        return defaultedList;
    }

    default Optional<TooltipData> getTheTooltipData(ItemStack stack) {
        assert MinecraftClient.getInstance().player != null;
        return Optional.of(new Callum_tooltip_data(getUpgrade_cap(), get_items_defaulted_list(stack), MinecraftClient.getInstance().player.currentScreenHandler.getCursorStack(), this.get_id()));
    }

    default void appendTheTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", getUpgradeCount(stack), getUpgrade_cap()).formatted(Formatting.GRAY));
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
