package scriptshatter.callum.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.upgradeableItems.UpgradeableTrinket;
import scriptshatter.callum.sound.CallumSounds;

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

    public void playRemoveOneSound(Entity entity) {
        entity.playSound(CallumSounds.LENS_OFF, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public void playInsertSound(Entity entity) {
        entity.playSound(CallumSounds.LENS_ON, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }


}
