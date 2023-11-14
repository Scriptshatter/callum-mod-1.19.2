package scriptshatter.callum.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.IndigoQuadHandler;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;
import scriptshatter.callum.armor.client.Trans_cap_model;
import scriptshatter.callum.armor.client.Trinket_model_provider;
import scriptshatter.callum.items.Upgrade_item;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;
import scriptshatter.callum.powers.InvisEquipmentPower;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorRenderMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public ArmorRenderMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
    void render_upgradeable(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if(livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof Cap_item){
            BipedEntityModel<LivingEntity> contextModel = (BipedEntityModel<LivingEntity>) getContextModel();
            ItemStack stack = livingEntity.getEquippedStack(EquipmentSlot.HEAD);
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

            BakedModel bakedModel;

            bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("armor/callum_pilot_model"));
            matrices.push();
            matrices.scale(1.001F, 1.001F, 1.001F);
            Trinket_model_provider.translateToHead(matrices, contextModel, livingEntity, k, l);
            if(PowerHolderComponent.hasPower(livingEntity, InvisEquipmentPower.class)){
                Trans_cap_model model = new Trans_cap_model();
                itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumerProvider, i, LivingEntityRenderer.getOverlay(livingEntity, 0), model);
            }
            else {
                itemRenderer.renderItem(stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumerProvider, i, LivingEntityRenderer.getOverlay(livingEntity, 0), bakedModel);
            }
            // Render objects Itemrenderer -> Vertex consumer there is where you will find the alpha values


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

                    itemRenderer.renderItem(Upgrade_item.make_invis(pinInstance, PowerHolderComponent.hasPower(livingEntity, InvisEquipmentPower.class)), ModelTransformation.Mode.HEAD, false, matrices, vertexConsumerProvider, i, LivingEntityRenderer.getOverlay(livingEntity, 0), pinModel);
                    matrices.pop();

                });
            }
            matrices.pop();
        }
    }
}
