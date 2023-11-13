package scriptshatter.callum.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.upgradeableItems.UpgradeableTrinket;

public class Goggles extends UpgradeableTrinket {
    public Goggles(Settings settings) {
        super(settings);
    }

    @Override
    public EquipmentSlot itemSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public String upgrade_type() {
        return "lens";
    }

    @Override
    public Identifier get_id() {
        return Callum.identifier("callum_goggles");
    }

    @Override
    public int getUpgrade_cap() {
        return 1;
    }


}
