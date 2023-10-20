package scriptshatter.callum.armor;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.entity.model.DragonHeadEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;
import software.bernie.example.client.EntityResources;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Cap_model extends AnimatedGeoModel<Cap_item> {

    @Override
    public Identifier getModelResource(Cap_item object) {
        return new Identifier(Callum.MOD_ID, "geo/cap.geo.json");
    }

    @Override
    public Identifier getTextureResource(Cap_item object) {
        return new Identifier(Callum.MOD_ID, "textures/item/cap.png");
    }

    @Override
    public Identifier getAnimationResource(Cap_item animatable) {
        return EntityResources.GECKOARMOR_ANIMATIONS;
    }
}
