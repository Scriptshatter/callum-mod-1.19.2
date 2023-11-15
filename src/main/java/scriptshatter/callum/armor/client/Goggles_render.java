package scriptshatter.callum.armor.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import scriptshatter.callum.Callum;

@Environment(EnvType.CLIENT)
public class Goggles_render implements TrinketRenderer {

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (contextModel instanceof BipedEntityModel<? extends LivingEntity> playerEntityModel) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            if(!entity.hasStackEquipped(EquipmentSlot.HEAD)){
                matrices.scale(0.9f, 0.9f, 0.9f);
            }
            Trinket_model_provider.translateToHead(matrices, (BipedEntityModel<LivingEntity>) playerEntityModel, entity, headYaw, headPitch);
            Trinket_model_provider.render_invis_item(entity, matrices, stack, vertexConsumers, light, Callum.identifier("item/callum_goggles_model"), false, ModelTransformation.Mode.HEAD, itemRenderer, entity.world, 0);
            matrices.pop();
        }
    }
}
