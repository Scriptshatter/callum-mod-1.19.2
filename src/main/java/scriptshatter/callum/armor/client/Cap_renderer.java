package scriptshatter.callum.armor.client;

import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
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
/*        MinecraftClient instance = MinecraftClient.getInstance();

        float tickDelta = instance.currentScreen == null ? instance.getTickDelta() : 1;
        float h = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevHeadYaw, livingEntity.headYaw);
        float k = j - h;

        float m = MathHelper.lerp(tickDelta, livingEntity.prevPitch, livingEntity.getPitch());
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        BakedModel bakedModel;

        bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("armor/callum_pilot_model"));
        matrices.push();
        matrices.scale(1.001F, 1.001F, 1.001F);
        Trinket_model_provider.translateToHead(matrices, contextModel, livingEntity, k, m);

        // Render objects
        itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(livingEntity, 0), bakedModel);

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
                itemRenderer.renderItem(pinInstance, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(livingEntity, 0), pinModel);
                matrices.pop();

            });
        }
        matrices.pop();*/


    }
}
