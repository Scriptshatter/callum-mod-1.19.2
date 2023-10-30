package scriptshatter.callum.armor;

import io.github.apace100.origins.component.OriginComponent;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.badges.ArmorMats;
import scriptshatter.callum.items.ItemRegister;
import scriptshatter.callum.items.badgeJson.UpgradeableItemTemplate;

import java.util.List;
import java.util.Optional;

public class Cap_item extends ArmorItem implements UpgradeableItemTemplate {
    public Cap_item(Settings settings) {
        super(ArmorMats.CALLUM_CLOTHING, EquipmentSlot.HEAD, settings);
    }

    @Override
    public EquipmentSlot itemSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Identifier get_id() {
        return Callum.identifier("callum_pilot");
    }

    @Override
    public int getUpgrade_cap() {
        return 3;
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
