package scriptshatter.callum.items;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.origins.Origins;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;
import scriptshatter.callum.armor.Goggles;

public class ItemRegister {

    public static void register_moditems(){
        Callum.LOGGER.info("This would be a little more complicated in 1.20");
    }

    static Identifier WEIRD_SKIN;
    static {
        WEIRD_SKIN = Origins.identifier("arcane_skin");
    }

    public static final Item CALLUM_PILOT = register_item("callum_pilot", new Cap_item(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CALLUM_GOGGLES = register_item("callum_goggles", new Goggles(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PLACEHOLDER_PIN = register_item("placeholder_pin", new Badge_item(new FabricItemSettings().group(ItemGroup.COMBAT), 0xFF00FF, null, null, WEIRD_SKIN));

    public static Item register_item(String name, Item item){
        return Registry.register(Registry.ITEM, Callum.identifier(name), item);
    }
}
