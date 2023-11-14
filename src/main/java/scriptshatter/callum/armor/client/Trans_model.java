package scriptshatter.callum.armor.client;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Trans_model implements FabricBakedModel, BakedModel {
    private final BakedModel template_model;

    public Trans_model(BakedModel templateModel){
        template_model = templateModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        QuadEmitter emitter = context.getEmitter();
        for (int p = 0; p < 4; p++) {
            emitter.spriteColor(p, 0, 0x7FFFFFFF);
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return template_model.getQuads(state, face, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return template_model.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return template_model.hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return template_model.isSideLit();
    }

    @Override
    public boolean isBuiltin() {
        return template_model.isBuiltin();
    }

    @Override
    public Sprite getParticleSprite() {
        return template_model.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return template_model.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return template_model.getOverrides();
    }
}
