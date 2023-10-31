package scriptshatter.callum.items;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.origins.Origins;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;

public class ItemRegister {

    public static void register_moditems(){
        Callum.LOGGER.info("This would be a little more complicated in 1.20");
    }

    static PowerType<?> WEIRD_SKIN;
    static {
        WEIRD_SKIN = new PowerTypeReference<>(Origins.identifier("arcane_skin"));
    }
    private static final PowerType<?>[] PLACEHOLDER_PIN_POWERS = new PowerType<?>[]{
            WEIRD_SKIN
    };

    public static final Item CALLUM_PILOT = register_item("callum_pilot", new Cap_item(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PLACEHOLDER_PIN = register_item("placeholder_pin", new Badge_item(new FabricItemSettings().group(ItemGroup.COMBAT), Callum.identifier("callum_pilot"), null, PLACEHOLDER_PIN_POWERS));

    public static Item register_item(String name, Item item){
        return Registry.register(Registry.ITEM, Callum.identifier(name), item);
    }
}
