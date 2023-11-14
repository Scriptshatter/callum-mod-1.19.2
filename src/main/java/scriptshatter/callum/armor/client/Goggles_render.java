package scriptshatter.callum.armor.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.powers.InvisEquipmentPower;

@Environment(EnvType.CLIENT)
public class Goggles_render implements TrinketRenderer {

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        BakedModel bakedModel;
        bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("item/callum_goggles_model"));
        if (contextModel instanceof BipedEntityModel<? extends LivingEntity> playerEntityModel) {
            matrices.push();
            if(!entity.hasStackEquipped(EquipmentSlot.HEAD)){
                matrices.scale(0.9f, 0.9f, 0.9f);
            }
            Trinket_model_provider.translateToHead(matrices, (BipedEntityModel<LivingEntity>) playerEntityModel, entity, headYaw, headPitch);
            itemRenderer.renderItem(Upgrade_item.make_invis(stack, PowerHolderComponent.hasPower(entity, InvisEquipmentPower.class)), ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light, LivingEntityRenderer.getOverlay(entity, 0), bakedModel);
            matrices.pop();
        }
    }
}
