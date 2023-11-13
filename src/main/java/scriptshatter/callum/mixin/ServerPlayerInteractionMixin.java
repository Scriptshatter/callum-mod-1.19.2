package scriptshatter.callum.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.registry.ModComponents;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scriptshatter.callum.networking.Post_office;
import scriptshatter.callum.powers.MultiMinePower;
import scriptshatter.callum.powers.SneakingStateSavingManager;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionMixin implements SneakingStateSavingManager {


    @Shadow
    protected ServerWorld world;
    @Final
    @Shadow
    protected ServerPlayerEntity player;

    @Shadow
    public void finishMining(BlockPos pos, int sequence, String reason) {
    }

    @Unique
    private BlockState justMinedBlockState;
    @Unique
    private boolean performingMultiMine = false;
    @Unique
    private boolean wasSneakingWhenStarted = false;

    @Inject(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockBreakStart(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", ordinal = 0))
    private void saveSneakingState(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        wasSneakingWhenStarted = player.isSneaking();
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(!wasSneakingWhenStarted);
        ServerPlayNetworking.send(player, Post_office.MULTI_MINING, data);
    }

    @Inject(method = "finishMining", at = @At("HEAD"))
    private void saveBlockStateForMultiMine(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        justMinedBlockState = world.getBlockState(pos);
    }

    @Inject(method = "finishMining", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;method_41250(Lnet/minecraft/util/math/BlockPos;ZILjava/lang/String;)V"))
    private void multiMinePower(BlockPos pos, int sequence, String reason, CallbackInfo ci) {
        if(!wasSneakingWhenStarted && !performingMultiMine) {
            performingMultiMine = true;
            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if(mmp.isBlockStateAffected(justMinedBlockState)) {
                    ItemStack tool = player.getMainHandStack().copy();
                    for(BlockPos bp : mmp.getAffectedBlocks(justMinedBlockState, pos)) {
                        finishMining(bp, sequence, reason);
                        if(!player.getMainHandStack().isItemEqualIgnoreDamage(tool)) {
                            break;
                        }
                    }
                }
            });
            performingMultiMine = false;
        }
    }
    @Override
    public boolean callum_template_1_19_2$wasSneakingWhenBlockBreakingStarted() {
        return false;
    }
}
