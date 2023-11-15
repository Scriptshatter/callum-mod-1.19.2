package scriptshatter.callum.items;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Upgrade_item extends Item{
    //NBT values are: valid_armor, upgrade_group, Powers
    @Nullable
    public final Identifier valid_armor;
    @Nullable
    public final String upgrade_group;
    public final int color;
    public final DefaultedList<Identifier> powers = DefaultedList.of();

    // I had chatGPT make this cause I was feeling really, REALLY lazy that day.
    public static List<String> splitString(String input, int wordsPerChunk) {
        String[] words = input.split("\\s+");
        List<String> result = new ArrayList<>();

        StringBuilder currentChunk = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            if (wordCount + word.length() > wordsPerChunk) {
                result.add(currentChunk.toString());
                currentChunk = new StringBuilder();
                wordCount = 0;
            }

            currentChunk.append(word).append(" ");
            wordCount += word.length() + 1;
        }

        if (currentChunk.length() > 0) {
            result.add(currentChunk.toString().trim());
        }

        return result;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(stack.getItem() instanceof Upgrade_item upgrade_item){
            if(upgrade_item.upgrade_group == null){
                tooltip.add(Text.translatable("item.callum.upgrade.stackable").formatted(Formatting.DARK_GREEN));
            }
            upgrade_item.powers.forEach(powerID -> {
                if(PowerTypeRegistry.contains(powerID)){
                    PowerType<?> powerType = PowerTypeRegistry.get(powerID);
                    if(!powerType.isHidden()){
                        if(I18n.translate(powerType.getOrCreateNameTranslationKey()).contains("isBad")){
                            splitString(I18n.translate(powerType.getOrCreateDescriptionTranslationKey()), 40).forEach(s -> {
                                tooltip.add(
                                        Text.literal(s)
                                                .formatted(Formatting.RED));
                            });
                        }
                        else {
                            splitString(I18n.translate(powerType.getOrCreateDescriptionTranslationKey()), 40).forEach(s -> {
                                tooltip.add(
                                        Text.literal(s)
                                                .formatted(Formatting.LIGHT_PURPLE));
                            });
                        }
                        tooltip.add(Text.literal(" "));
                    }
                }
            });
        }
    }

    public Upgrade_item(Settings settings, int color, @Nullable Identifier valid_armor, @Nullable String upgradeGroup, Identifier... powers) {
        super(settings.maxCount(1));
        this.valid_armor = valid_armor;
        upgrade_group = upgradeGroup;
        this.powers.addAll(Arrays.asList(powers));
        this.color = color;
    }
}
