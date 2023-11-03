package scriptshatter.callum.armor.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
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
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;
import scriptshatter.callum.items.Badge_item;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class Cap_renderer implements ArmorRenderer {


    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity livingEntity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        MinecraftClient instance = MinecraftClient.getInstance();
        float tickDelta = instance.getTickDelta();
        if(instance.currentScreen != null){
            tickDelta = 1;
        }
        float h = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevHeadYaw, livingEntity.headYaw);
        float k = j - h;
        float m = MathHelper.lerp(tickDelta, livingEntity.prevPitch, livingEntity.getPitch());
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        BakedModel bakedModel;
        BakedModel pinModel;
        pinModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("item/pin_model"));
        bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("armor/callum_pilot_model"));
        if (contextModel instanceof PlayerEntityModel<? extends LivingEntity> playerEntityModel) {
            matrices.push();
            TrinketRenderer.translateToFace(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) playerEntityModel, (ClientPlayerEntity)livingEntity, k, m);

            // Render objects
            itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(livingEntity, 0), bakedModel);
            matrices.pop();

            if(stack.getItem() instanceof Cap_item capItem){
                capItem.get_items_defaulted_list(stack).forEach(pinInstance -> {
                    matrices.push();
                    matrices.translate(0, 4, 5);
                    TrinketRenderer.translateToFace(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) playerEntityModel, (ClientPlayerEntity)livingEntity, k, m);
                    itemRenderer.renderItem(pinInstance, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(livingEntity, 0), pinModel);
                    matrices.pop();
                });
            }




        }
    }
}
