package scriptshatter.callum.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import scriptshatter.callum.items.ItemRegister;

import java.util.Locale;

public class BadgeModelGenerator extends FabricModelProvider {
    public BadgeModelGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        registerBadge(ItemRegister.UPGRADE, itemModelGenerator);
    }

    public final void registerBadge(Item badge, ItemModelGenerator itemModelGenerator) {
        for(int i = 1; i < 64; ++i) {
            itemModelGenerator.register(badge, String.format(Locale.ROOT, "_%s", "placeholder"), Models.GENERATED);
        }
    }
}
