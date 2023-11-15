package scriptshatter.callum.armor.client;

import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class Trans_cap_model implements FabricBakedModel, BakedModel{

    //Make a constructor that takes in item stack and generate model based on that
    public Trans_cap_model(Identifier model){
        this.template_model = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), model);
    }

    public Trans_cap_model(ItemStack itemStack, LivingEntity entity, @Nullable World world, int seed){
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        this.template_model = itemRenderer.getModel(itemStack, world, entity, seed);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

    }

    public final BakedModel template_model;

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        QuadEmitter quadEmitter = context.getEmitter();
        Random random = randomSupplier.get();
        if(template_model != null){
            for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                final Direction cullFace = ModelHelper.faceFromIndex(i);
                random.setSeed(42L);
                final List<BakedQuad> quads = template_model.getQuads(null, cullFace, random);
                final int count = quads.size();
                if (count != 0) {
                    for (final BakedQuad q : quads) {
                        quadEmitter.fromVanilla(q, IndigoRenderer.MATERIAL_STANDARD, cullFace);
                        quadEmitter.spriteColor(0, 0x80ffffff, 0x80ffffff, 0x80ffffff, 0x80ffffff);
                        quadEmitter.emit();

                    }
                }
            }
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
