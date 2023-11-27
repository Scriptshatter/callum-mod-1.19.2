package scriptshatter.callum.armor.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.origin.Origin;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.IndigoQuadHandler;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.ItemRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import scriptshatter.callum.Callum;
import scriptshatter.callum.powers.InvisEquipmentPower;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class Trinket_model_provider implements ExtraModelProvider {

    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
        out.accept(Callum.identifier("item/callum_goggles_model"));
        out.accept(Callum.identifier("armor/callum_pilot_model"));
    }

    public static void translateToHead(MatrixStack matrices, BipedEntityModel<LivingEntity> model,
                                       LivingEntity player, float headYaw, float headPitch) {

        if (player.isInSwimmingPose() || player.isFallFlying()) {
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(model.head.roll));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-45.0F));
        } else {

            if (player.isInSneakingPose() && !model.riding) {
                matrices.translate(0.0F, 0.25F, 0.0F);
            }
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));
        }
        matrices.translate(0.0F, -0.25F, -0.3F);
    }

    public static void render_invis_item(LivingEntity livingEntity,
                                         MatrixStack matrices,
                                         ItemStack stack,
                                         VertexConsumerProvider vertexConsumerProvider,
                                         int light,
                                         @Nullable Identifier model_ID,
                                         boolean lefty,
                                         ModelTransformation.Mode model_mode,
                                         ItemRenderer renderer,
                                         @Nullable World world,
                                         int item_seed){
        Trans_cap_model model = new Trans_cap_model(stack, livingEntity, world, item_seed, 0x80ffffff);
        if(PowerHolderComponent.hasPower(livingEntity, InvisEquipmentPower.class) && !model.isBuiltin()){
            if(model_ID != null){
                model = new Trans_cap_model(model_ID, 0x80ffffff);
            }
            ThreadLocal<ItemRenderContext> fabric_contexts = ThreadLocal.withInitial(() -> new ItemRenderContext(renderer.colors));
            ItemRenderContext.VanillaQuadHandler fabric_vanillaHandler = new IndigoQuadHandler(renderer);
            matrices.push();
            model.getTransformation().getTransformation(model_mode).apply(lefty, matrices);
            matrices.translate(-0.5, -0.5, -0.5);
            fabric_contexts.get().renderModel(stack, model_mode, lefty, matrices, vertexConsumerProvider, light, LivingEntityRenderer.getOverlay(livingEntity, 0), model, fabric_vanillaHandler);
            matrices.pop();
        }
        else {
            BakedModel bakedModel;
            bakedModel = renderer.getModel(stack, world, livingEntity, item_seed);
            if(model_ID != null){
                bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), model_ID);
            }
            renderer.renderItem(stack, model_mode, lefty, matrices, vertexConsumerProvider, light, LivingEntityRenderer.getOverlay(livingEntity, 0), bakedModel);

        }
    }
}
