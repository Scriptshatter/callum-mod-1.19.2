package scriptshatter.callum.powers;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import scriptshatter.callum.Callum;

import java.util.*;

public class CallumPowerFactory {
    public static void register(){
        register(Pin_attribute::createFactory);
        register(DisablePowerPower::factory);
        register(new PowerFactory<>(Callum.identifier("lumberjack_classes"),
                new SerializableData(),
                data ->
                        (type, entity) ->

                                new MultiMinePower(type, entity, (pl, bs, bp) -> {
                            Set<BlockPos> affected = new HashSet<>();
                            Queue<BlockPos> queue = new LinkedList<>();
                            queue.add(bp);
                            boolean foundOneWithLeaves = false;
                            BlockPos.Mutable pos = bp.mutableCopy();
                            BlockPos.Mutable newPos = bp.mutableCopy();
                            while(!queue.isEmpty()) {
                                pos.set(queue.remove());
                                for(int dx = -1; dx <= 1; dx++) {
                                    for(int dy = 0; dy <= 1; dy++) {
                                        for(int dz = -1; dz <= 1; dz++) {
                                            if(dx == 0 & dy == 0 && dz == 0) {
                                                continue;
                                            }
                                            newPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                                            BlockState state = pl.world.getBlockState(newPos);
                                            if(state.isOf(bs.getBlock()) && !affected.contains(newPos)) {
                                                BlockPos savedNewPos = newPos.toImmutable();
                                                affected.add(savedNewPos);
                                                queue.add(savedNewPos);
                                                if(affected.size() > 255) {
                                                    if(!foundOneWithLeaves) {
                                                        return new ArrayList<>();
                                                    }
                                                    return new ArrayList<>(affected);
                                                }
                                            } else
                                            if((state.isIn(BlockTags.LEAVES) || state.getBlock() instanceof LeavesBlock) && !state.get(LeavesBlock.PERSISTENT)) {
                                                foundOneWithLeaves = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if(!foundOneWithLeaves) {
                                affected.clear();
                            }
                            return new ArrayList<>(affected);
                        }, state -> state.isIn(BlockTags.LOGS)).addCondition(e -> e instanceof LivingEntity l && l.getMainHandStack().getItem() instanceof AxeItem)
        ));

        register(new PowerFactory<>(Callum.identifier("dwarf"),
                new SerializableData(),
                data ->
                        (type, entity) ->

                                new MultiMinePower(type, entity, (livingEntity, blockState, blockPos) -> {
                                    Set<BlockPos> affected = new HashSet<>();
                                    Queue<BlockPos> queue = new LinkedList<>();
                                    queue.add(blockPos);
                                    BlockPos.Mutable pos = blockPos.mutableCopy();
                                    BlockPos.Mutable newPos = blockPos.mutableCopy();
                                    while(!queue.isEmpty()) {
                                        pos.set(queue.remove());
                                        for(int dx = -1; dx <= 1; dx++) {
                                            for(int dy = 0; dy <= 1; dy++) {
                                                for(int dz = -1; dz <= 1; dz++) {
                                                    if(dx == 0 & dy == 0 && dz == 0) {
                                                        continue;
                                                    }
                                                    newPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                                                    BlockState state = livingEntity.world.getBlockState(newPos);
                                                    if(state.isOf(blockState.getBlock()) && !affected.contains(newPos)) {
                                                        BlockPos savedNewPos = newPos.toImmutable();
                                                        affected.add(savedNewPos);
                                                        queue.add(savedNewPos);
                                                        if(affected.size() > 255) {
                                                            return new ArrayList<>(affected);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return new ArrayList<>(affected);
                                }, state -> state.isIn(TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "ores")))).addCondition(e -> e instanceof LivingEntity l && l.getMainHandStack().getItem() instanceof PickaxeItem)
        ));
    }

    private static void register(io.github.apace100.apoli.power.factory.PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
