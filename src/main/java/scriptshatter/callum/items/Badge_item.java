package scriptshatter.callum.items;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class Badge_item extends Item {
    //NBT values are: valid_armor, upgrade_group, Powers
    @Nullable
    public final Identifier valid_armor;
    @Nullable
    public final String upgrade_group;
    public final PowerType<?>[] powers;

    public Badge_item(Settings settings, @Nullable Identifier valid_armor, @Nullable String upgradeGroup, PowerType<?>[] powers) {
        super(settings);
        this.valid_armor = valid_armor;
        upgrade_group = upgradeGroup;
        this.powers = powers;
    }




}
