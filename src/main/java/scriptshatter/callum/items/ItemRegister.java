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
    static Identifier LUMBERJACK_SPEED_POWER;
    static Identifier DWARF_PIN_POWER;
    static Identifier DWARF_SPEED_POWER;
    static Identifier STAR_NO_PHASING;
    static Identifier STAR_NO_TRANSPARENT;
    static Identifier STAR_YES_SUN;
    static Identifier MOON_ARMOR;
    static Identifier MOON_TRANS_ARMOR;
    static Identifier DIVING_WATER_BREATHING;
    static Identifier FINS_SWIM_SPEED;
    static Identifier DRAINING_PIN_POWER;
    static Identifier SPEED_PIN_POWER;
    static Identifier CLOUD_PIN_POWER;
    static Identifier SPRING_PIN;
    static Identifier WINTER_PIN_POWER;
    static Identifier BLAST_PIN_POWER;
    static Identifier MAGNET_PIN_POWER;
    static Identifier CACTI_PIN;
    static Identifier SHADOW_LENS_POWER;
    static Identifier TRUTH_LENS_POWER;

    static {
        WEIRD_SKIN = Origins.identifier("arcane_skin");
        BONE_PIN_POWER = Callum.identifier("bone_pin");
        LUMBERJACK_PIN_POWER = Callum.identifier("lumberjack_pin");
        LUMBERJACK_SPEED_POWER = Callum.identifier("lumberjack_speed");
        DWARF_PIN_POWER = Callum.identifier("dwarf_pin");
        DWARF_SPEED_POWER = Callum.identifier("dwarf_speed");
        STAR_NO_PHASING = Callum.identifier("star_no_phasing");
        STAR_NO_TRANSPARENT = Callum.identifier("star_no_transparent");
        STAR_YES_SUN = Callum.identifier("star_yes_sun");
        MOON_ARMOR = Callum.identifier("moon_armor");
        MOON_TRANS_ARMOR = Callum.identifier("moon_replace_power");
        DIVING_WATER_BREATHING = Callum.identifier("diving_water_breathing");
        FINS_SWIM_SPEED = Callum.identifier("fins_swim_speed");
        DRAINING_PIN_POWER = Callum.identifier("draining_pin");
        SPEED_PIN_POWER = Callum.identifier("speed_pin");
        CLOUD_PIN_POWER = Callum.identifier("cloud_double_jump");
        SPRING_PIN = Callum.identifier("spring_wall_jump");
        WINTER_PIN_POWER = Callum.identifier("winter_pin");
        BLAST_PIN_POWER = Callum.identifier("blast_pin");
        MAGNET_PIN_POWER = Callum.identifier("magnet_pin");
        CACTI_PIN = Callum.identifier("cactus_pin");
        SHADOW_LENS_POWER = Callum.identifier("shadow_lens");
        TRUTH_LENS_POWER = Callum.identifier("truth_lens");
    }
    public static final Item CALLUM_ICON = register_item("callum_icon", new Item(new FabricItemSettings()));
    public static final Item PIN_BASE = register_item("pin_base", new Item(new FabricItemSettings()));
    public static final ItemGroup CALLUM = FabricItemGroupBuilder.build(Callum.identifier("callum_itemgroup"), () -> new ItemStack(CALLUM_ICON));
    public static final Item CALLUM_PILOT = register_item("callum_pilot", new Cap_item(new FabricItemSettings().group(CALLUM)));
    public static final Item CALLUM_GOGGLES = register_item("callum_goggles", new Goggles(new FabricItemSettings().group(CALLUM)));
    public static final Item PLACEHOLDER_PIN = register_item("placeholder_pin", new Upgrade_item(new FabricItemSettings(), 0xFF00FF, null, null, WEIRD_SKIN));
    public static final Item FINS_PIN = register_item("fins_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x093659, Callum.identifier("callum_pilot"), "fins", FINS_SWIM_SPEED));
    public static final Item DIVING_PIN = register_item("diving_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x474476, Callum.identifier("callum_pilot"), "diving", DIVING_WATER_BREATHING));
    public static final Item SPEED_PIN = register_item("speed_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x2b4ea5, Callum.identifier("callum_pilot"), "speed", SPEED_PIN_POWER));
    public static final Item CLOUD_PIN = register_item("cloud_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xc8bfd8, Callum.identifier("callum_pilot"), "cloud", CLOUD_PIN_POWER));
    public static final Item KICKOFF_PIN = register_item("kickoff_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xff8e80, Callum.identifier("callum_pilot"), "kickoff", SPRING_PIN));
    public static final Item WINTER_PIN = register_item("winter_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xa7bcc9, Callum.identifier("callum_pilot"), "winter", WINTER_PIN_POWER));
    public static final Item BLAST_PIN = register_item("blast_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xffc96b, Callum.identifier("callum_pilot"), "blast", BLAST_PIN_POWER));
    public static final Item MAGNET_PIN = register_item("magnet_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xe01f3f, Callum.identifier("callum_pilot"), "magnet", MAGNET_PIN_POWER));
    public static final Item PRICKLY_PIN = register_item("prickly_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x40985e, Callum.identifier("callum_pilot"), "prickly", CACTI_PIN));
    public static final Item BONE_PIN = register_item("bone_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xffd4a3, Callum.identifier("callum_pilot"), null, BONE_PIN_POWER));
    //More pins more defence
    public static final Item LUMBERJACK_PIN = register_item("lumberjack_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xec273f, Callum.identifier("callum_pilot"), "lumberjack", LUMBERJACK_PIN_POWER, LUMBERJACK_SPEED_POWER));
    public static final Item DWARF_PIN = register_item("dwarf_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xa097a1, Callum.identifier("callum_pilot"), "dwarf", DWARF_PIN_POWER, DWARF_SPEED_POWER));
    public static final Item STAR_PIN = register_item("star_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xd68fb8, Callum.identifier("callum_pilot"), "time",  STAR_YES_SUN, STAR_NO_TRANSPARENT, STAR_NO_PHASING));
    public static final Item SPACE_PIN = register_item("moon_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x091e4d, Callum.identifier("callum_pilot"), "time", MOON_ARMOR, MOON_TRANS_ARMOR));
    public static final Item TRUTH_LENS = register_item("truth_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x0ce6f2, Callum.identifier("callum_goggles"), "lens", TRUTH_LENS_POWER));
    public static final Item WEALTH_LENS = register_item("wealth_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0xf0d696, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));
    public static final Item VISION_LENS = register_item("vision_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x3c42c4, Callum.identifier("callum_goggles"), "lens", WEIRD_SKIN));
    public static final Item SHADOW_LENS = register_item("shadow_lens", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x1f285d, Callum.identifier("callum_goggles"), "lens", SHADOW_LENS_POWER));
    public static final Item DRAINING_PIN = register_item("draining_pin", new Upgrade_item(new FabricItemSettings().group(CALLUM), 0x1f285d, Callum.identifier("callum_goggles"), "draining", DRAINING_PIN_POWER));


    public static Item register_item(String name, Item item){
        return Registry.register(Registry.ITEM, Callum.identifier(name), item);
    }
}
