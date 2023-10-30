package scriptshatter.callum.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import scriptshatter.callum.Callum;
import scriptshatter.callum.armor.Cap_item;

public class ItemRegister {
    public static final Item CALLUM_PILOT = register_item("callum_pilot", new Cap_item(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item UPGRADE = register_item("upgrade", new Badge_item(new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static Item register_item(String name, Item item){
        return Registry.register(Registry.ITEM, Callum.identifier(name), item);
    }

    public static void register_moditems(){
        Callum.LOGGER.info("This would be a little more complicated in 1.20");
    }
}
