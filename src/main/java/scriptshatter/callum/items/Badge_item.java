package scriptshatter.callum.items;

import dev.emi.trinkets.TrinketSlot;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.apace100.apoli.util.StackPowerUtil.StackPower;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Badge_item extends Item {
    //NBT values are: valid_armor, upgrade_group, Powers
    @Nullable
    public final Identifier valid_armor;
    @Nullable
    public final String upgrade_group;
    public final int color;
    public final DefaultedList<Identifier> powers = DefaultedList.of();

    public Badge_item(Settings settings, int color, @Nullable Identifier valid_armor, @Nullable String upgradeGroup, Identifier... powers) {
        super(settings);
        this.valid_armor = valid_armor;
        upgrade_group = upgradeGroup;
        this.powers.addAll(Arrays.asList(powers));
        this.color = color;
    }
}
