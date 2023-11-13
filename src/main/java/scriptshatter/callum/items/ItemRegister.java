package scriptshatter.callum.items;

import io.github.apace100.origins.Origins;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
    static Identifier BONE_PIN_POWER;
    static Identifier LUMBERJACK_PIN_POWER;
    static {
        WEIRD_SKIN = Origins.identifier("arcane_skin");
        BONE_PIN_POWER = Callum.identifier("bone_pin");
        LUMBERJACK_PIN_POWER = Callum.identifier("lumberjack_pin");
    }
    public static final Item CALLUM_ICON = register_item("callum_icon", new Item(new FabricItemSettings()));
    public static final ItemGroup CALLUM = FabricItemGroupBuilder.build(Callum.identifier("callum_itemgroup"), () -> new ItemStack(CALLUM_ICON));
    public static final Item CALLUM_PILOT = register_item("callum_pilot", new Cap_item(new FabricItemSettings().group(CALLUM)));
    public static final Item CALLUM_GOGGLES = register_item("callum_goggles", new Goggles(new FabricItemSettings().group(CALLUM)));
    public static final Item PLACEHOLDER_PIN = register_item("placeholder_pin", new Upgrade_item(new FabricItemSettings(), 0xFF00FF, null, null, WEIRD_SKIN));
    public static final Item FINS_PIN = register_item("fins_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x093659, Callum.identifier("callum_pilot"), "fins", WEIRD_SKIN));
    public static final Item DIVING_PIN = register_item("diving_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x474476, Callum.identifier("callum_pilot"), "diving", WEIRD_SKIN));
    public static final Item SPEED_PIN = register_item("speed_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x2b4ea5, Callum.identifier("callum_pilot"), "speed", WEIRD_SKIN));
    public static final Item CLOUD_PIN = register_item("cloud_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xc8bfd8, Callum.identifier("callum_pilot"), "cloud", WEIRD_SKIN));
    public static final Item KICKOFF_PIN = register_item("kickoff_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xff8e80, Callum.identifier("callum_pilot"), "kickoff", WEIRD_SKIN));
    public static final Item WINTER_PIN = register_item("winter_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xa7bcc9, Callum.identifier("callum_pilot"), "winter", WEIRD_SKIN));
    public static final Item BLAST_PIN = register_item("blast_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xffc96b, Callum.identifier("callum_pilot"), "blast", WEIRD_SKIN));
    public static final Item MAGNET_PIN = register_item("magnet_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xe01f3f, Callum.identifier("callum_pilot"), "magnet", WEIRD_SKIN));
    public static final Item PRICKLY_PIN = register_item("prickly_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x40985e, Callum.identifier("callum_pilot"), "prickly", WEIRD_SKIN));
    public static final Item BONE_PIN = register_item("bone_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xffd4a3, Callum.identifier("callum_pilot"), null, BONE_PIN_POWER));
    //More pins more defence
    public static final Item LUMBERJACK_PIN = register_item("lumberjack_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xec273f, Callum.identifier("callum_pilot"), "lumberjack", LUMBERJACK_PIN_POWER));
    public static final Item DWARF_PIN = register_item("dwarf_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xa097a1, Callum.identifier("callum_pilot"), "dwarf", WEIRD_SKIN));
    public static final Item STAR_PIN = register_item("star_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xd68fb8, Callum.identifier("callum_pilot"), "time", WEIRD_SKIN));
    public static final Item SPACE_PIN = register_item("moon_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x091e4d, Callum.identifier("callum_pilot"), "space", WEIRD_SKIN));
    public static final Item TRUTH_LENS = register_item("truth_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x0ce6f2, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));
    public static final Item WEALTH_LENS = register_item("wealth_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xf0d696, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));
    public static final Item VISION_LENS = register_item("vision_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x3c42c4, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));
    public static final Item SHADOW_LENS = register_item("shadow_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x1f285d, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));


    public static Item register_item(String name, Item item){
        return Registry.register(Registry.ITEM, Callum.identifier(name), item);
    }
}
