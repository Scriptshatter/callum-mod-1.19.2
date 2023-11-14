package scriptshatter.callum.armor.client;

import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;
import scriptshatter.callum.armor.Goggles;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

import java.util.List;
import java.util.function.Supplier;

public class Trans_cap_model implements FabricBakedModel, BakedModel{

    //Make a constructor that takes in item stack and generate model based on that

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

    }

    private BakedModel template_model = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("item/callum_goggles_model"));

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModels().getModel(stack);
        if(stack.getItem() instanceof Goggles){
            bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("item/callum_goggles_model"));
        }
        else if (stack.getItem() instanceof Cap_item){
            bakedModel = BakedModelManagerHelper.getModel(MinecraftClient.getInstance().getBakedModelManager(), Callum.identifier("armor/callum_pilot_model"));
        }

        context.getEmitter().spriteColor(0, 0x7fffffff, 0x7fffffff, 0x7fffffff, 0x7fffffff);
        context.getEmitter().emit();
        context.bakedModelConsumer().accept(bakedModel);
        template_model = bakedModel;
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
