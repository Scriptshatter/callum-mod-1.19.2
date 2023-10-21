package scriptshatter.callum.armor.badges;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class Callum_tooltip_data implements TooltipData {
    private final int upgrade_cap;
    private final ItemStack hover;
    private final DefaultedList<ItemStack> upgrades;

    public Callum_tooltip_data(int upgradeCap, DefaultedList<ItemStack> upgrades, ItemStack hover) {
        this.upgrade_cap = upgradeCap;
        this.upgrades = upgrades;
        this.hover = hover;
    }

    public DefaultedList<ItemStack> getUpgrades() {
        return this.upgrades;
    }

    public int getUpgrade_cap() {
        return this.upgrade_cap;
    }

    public ItemStack getHover(){
        return this.hover;
    }
}
