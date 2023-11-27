package scriptshatter.callum.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

import java.util.List;
import java.util.Optional;

public class Lens_crucible extends Item implements IUpgradeableItem {
    public Lens_crucible(Settings settings) {
        super(settings);
    }

    @Override
    public EquipmentSlot itemSlot() {
        return null;
    }

    @Override
    public String upgrade_type() {
        return null;
    }

    @Override
    public Identifier get_id() {
        return Callum.identifier("lens_crucible");
    }

    @Override
    public int getUpgrade_cap() {
        return 1;
    }

    @Override
    public void update_powers(ItemStack upgradeable) {

    }

    @Override
    public boolean has_power(ItemStack upgradeable, Identifier powerId) {
        return true;
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
}
