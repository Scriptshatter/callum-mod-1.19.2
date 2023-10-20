package scriptshatter.callum.armor.badges;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ArmorItemTemplate extends ArmorItem implements IAnimatable {
    public final int upgrade_cap;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public ArmorItemTemplate(EquipmentSlot slot, Settings settings, int upgradeCap) {
        super(ArmorMats.CALLUM_CLOTHING, slot, settings);
        upgrade_cap = upgradeCap;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 20, horse -> PlayState.STOP));
    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }




    public boolean onStackClicked(ItemStack armor, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                this.playRemoveOneSound(player);
                removeFirstStack(armor).ifPresent((removedStack) -> {
                    addUpgrade(armor, slot.insertStack(removedStack));
                });
            } else if (itemStack.getItem().canBeNested()) {
                int i = (getUpgrade_cap(armor) - getUpgradeCount(armor));
                int j = addUpgrade(armor, slot.takeStackRange(1, i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    private static int addUpgrade(ItemStack armor, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canBeNested()) {
            NbtCompound nbtCompound = armor.getOrCreateNbt();
            if (!nbtCompound.contains("Upgrades")) {
                nbtCompound.put("Upgrades", new NbtList());
            }

            int i = getUpgradeCount(armor);
            if (i >= getUpgrade_cap(armor)) {
                return 0;
            } else {
                NbtList nbtList = nbtCompound.getList("Upgrades", NbtElement.COMPOUND_TYPE);
                ItemStack itemStack2 = stack.copy();
                itemStack2.setCount(1);
                NbtCompound nbtCompound3 = new NbtCompound();
                itemStack2.writeNbt(nbtCompound3);
                nbtList.add(0, (NbtElement)nbtCompound3);

                return 1;
            }
        } else {
            return 0;
        }
    }
    //Gets the max amount of upgrades
    private static int getUpgrade_cap(ItemStack stack) {
        if(stack.getItem() instanceof ArmorItemTemplate armorItemTemplate){
            return armorItemTemplate.upgrade_cap;
        }
        return 0;
    }
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
