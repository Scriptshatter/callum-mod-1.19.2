package scriptshatter.callum.armor.client;

import net.fabricmc.fabric.api.client.model.ExtraModelProvider;
import net.minecraft.client.render.model.BuiltinBakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import scriptshatter.callum.Callum;

import java.util.function.Consumer;

public class Trinket_model_provider implements ExtraModelProvider {

    @Override
    public void provideExtraModels(ResourceManager manager, Consumer<Identifier> out) {
        out.accept(Callum.identifier("item/callum_goggles_model"));
    }
}
