package scriptshatter.callum.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import scriptshatter.callum.armor.badges.ArmorItemTemplate;
import scriptshatter.callum.armor.badges.ArmorMats;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Cap_item extends ArmorItemTemplate {
    public Cap_item(Settings settings) {
        super(EquipmentSlot.HEAD, settings, 3);
    }
}
