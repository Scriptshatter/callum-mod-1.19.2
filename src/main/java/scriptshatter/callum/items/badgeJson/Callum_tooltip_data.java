package scriptshatter.callum.items.badgeJson;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public record Callum_tooltip_data(int upgradeCap, DefaultedList<ItemStack> upgrades, ItemStack hover, Identifier armor_id) implements TooltipData {}