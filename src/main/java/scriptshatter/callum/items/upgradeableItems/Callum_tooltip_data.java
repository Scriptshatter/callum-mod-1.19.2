package scriptshatter.callum.items.upgradeableItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public record Callum_tooltip_data(ItemStack upgradeable, ItemStack hover) implements TooltipData {}