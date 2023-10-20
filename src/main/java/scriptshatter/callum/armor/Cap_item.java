package scriptshatter.callum.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import scriptshatter.callum.armor.badges.ArmorMats;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Cap_item extends ArmorItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public Cap_item(Settings settings) {
        super(ArmorMats.CALLUM_CLOTHING, EquipmentSlot.HEAD, settings);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 20, horse -> PlayState.STOP));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
