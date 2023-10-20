package scriptshatter.callum.armor.badges;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum ArmorMats implements ArmorMaterial {
    CALLUM_CLOTHING("Callum Clothing", 70, new int[] {0, 0, 2, 3, 2, 1}, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0, 0, () -> Ingredient.ofItems(Items.LEATHER));

    private final String name;
    private final int durability;
    private final int[] protamt;
    private final SoundEvent eqpsnd;
    private final float toughness;
    private final float kbresist;
    private final Supplier<Ingredient> repair_ing;

    private static final int[] BASE_DURA = {0, 0, 11, 16, 15, 13};

    ArmorMats(String name, int durability, int[] protamt, SoundEvent eqpsnd, float toughness, float kbresist, Supplier<Ingredient> repairIng) {
        this.name = name;
        this.durability = durability;
        this.protamt = protamt;
        this.eqpsnd = eqpsnd;
        this.toughness = toughness;
        this.kbresist = kbresist;
        repair_ing = repairIng;
    }


    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURA[slot.ordinal()] * durability;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return protamt[slot.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return eqpsnd;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repair_ing.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return kbresist;
    }
}
