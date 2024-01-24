package scriptshatter.callum.armor.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

@Environment(EnvType.CLIENT)
public class Cap_renderer implements ArmorRenderer {


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity livingEntity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.push();
        matrices.scale(1.001F, 1.001F, 1.001F);
        Trinket_model_provider.translateToHead(matrices, contextModel, livingEntity, (float) (contextModel.head.yaw/(Math.PI/180)), (float) (contextModel.head.pitch/(Math.PI/180)));

        // Render objects
        Trinket_model_provider.render_invis_item(livingEntity, matrices, stack, vertexConsumers, light, Callum.identifier("armor/callum_pilot_model"), false, ModelTransformation.Mode.HEAD, itemRenderer, livingEntity.world, 0);

       if(stack.getItem() instanceof Cap_item){
            IUpgradeableItem.getUpgrades(stack).forEach((pin_slot, pinInstance) -> {
                matrices.push();
                BakedModel pinModel;
                pinModel = itemRenderer.getModel(pinInstance, null, livingEntity, 0);
                matrices.scale(0.1F, 0.1F, 0.1F);
                switch (pin_slot) {
                    case 1 -> {
                        matrices.translate(3.6, -1, 2);
                        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
                        matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(75));
                    }
                    case 2 -> {
                        matrices.translate(-1.7, -1.3, -0.5);
                        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                        matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(74.8F));
                    }
                    case 3 -> {
                        matrices.translate(-3.6, 1.3, 5);
                        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90));
                        matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(120));
                    }
                }
                Trinket_model_provider.render_invis_item(livingEntity, matrices, pinInstance, vertexConsumers, light, null, false, ModelTransformation.Mode.HEAD, itemRenderer, livingEntity.world, 0);
                matrices.pop();

            });
        }
        matrices.pop();


    }
}
