package scriptshatter.callum.armor.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import scriptshatter.callum.Callum;

import java.lang.reflect.Field;
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
}
