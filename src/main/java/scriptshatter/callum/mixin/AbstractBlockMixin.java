package scriptshatter.callum.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scriptshatter.callum.networking.Post_office;
import scriptshatter.callum.powers.MultiMinePower;
import scriptshatter.callum.powers.SneakingStateSavingManager;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(method = "calcBlockBreakingDelta", at = @At("RETURN"), cancellable = true)
    private void modifyMultiMinedBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        boolean processMultimine;
        if(player instanceof ServerPlayerEntity) {
            SneakingStateSavingManager sneakingState = (SneakingStateSavingManager) ((ServerPlayerEntity)player).interactionManager;
            processMultimine = sneakingState.callum_template_1_19_2$wasSneakingWhenBlockBreakingStarted();
        } else {
            processMultimine = Post_office.isMultiMining;
        }
        if(processMultimine) {
            ItemStack tool = player.getEquippedStack(EquipmentSlot.MAINHAND);
            int toolDurability = 128;
            if(!tool.isEmpty()) {
                toolDurability = tool.getMaxDamage() - tool.getDamage();
            }
            int finalToolDurability = toolDurability;
            PowerHolderComponent.KEY.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if(mmp.isBlockStateAffected(state)) {
                    int affectBlockCount = mmp.getAffectedBlocks(state, pos).size();
                    if(affectBlockCount > 0) {
                        int multiplier = Math.min(affectBlockCount, finalToolDurability - 1);
                        multiplier = (int)Math.ceil((float)multiplier * 0.75F);
                        cir.setReturnValue(cir.getReturnValueF() / multiplier);
                    }
                }
            });
        }
    }
}