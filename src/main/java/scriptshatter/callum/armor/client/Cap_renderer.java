package scriptshatter.callum.armor.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import scriptshatter.callum.armor.Cap_item;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class Cap_renderer extends GeoArmorRenderer<Cap_item> {

    public Cap_renderer() {
        super(new Cap_model());
        this.headBone = "hat";
        this.bodyBone = null;
        this.rightArmBone = null;
        this.leftArmBone = null;
        this.rightLegBone = null;
        this.leftLegBone = null;
        this.rightBootBone = null;
        this.leftBootBone = null;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        super.render(matrices, vertexConsumers, stack, entity, slot, light, contextModel);
    }
}
