package scriptshatter.callum.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scriptshatter.callum.powers.SprintPower;
@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow protected abstract boolean hasMovementInput();

    @Shadow public Input input;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Redirect(method = "isWalking", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/Input;movementForward:F"))
    float run_run(Input instance){
        if(PowerHolderComponent.hasPower(this, SprintPower.class)){
            return Math.abs(input.getMovementInput().x) + Math.abs(input.getMovementInput().y);
        }
        return input.movementForward;
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    boolean run(Input instance){
        return instance.hasForwardMovement() || (PowerHolderComponent.hasPower(this, SprintPower.class) && this.hasMovementInput());
    }

}
