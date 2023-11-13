package scriptshatter.callum.powers;

import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import scriptshatter.callum.Callum;
import scriptshatter.callum.items.upgradeableItems.IUpgradeableItem;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Pin_attribute extends Power {
    private final List<AttributedEntityAttributeModifier> modifiers = new LinkedList<AttributedEntityAttributeModifier>();
    private final boolean updateHealth;
    private final ItemStack upgradeItem;

    public Pin_attribute(PowerType<?> type, LivingEntity entity, boolean updateHealth, ItemStack upgradeItem) {
        super(type, entity);
        this.updateHealth = updateHealth;
        this.upgradeItem = upgradeItem;
    }

    public Pin_attribute(PowerType<?> type, LivingEntity entity, boolean updateHealth, EntityAttribute attribute, EntityAttributeModifier modifier, ItemStack upgradeItem) {
        this(type, entity, updateHealth, upgradeItem);
        addModifier(attribute, modifier, entity, upgradeItem);
    }

    public void addModifier(EntityAttribute attribute, EntityAttributeModifier modifier, LivingEntity livingEntity, ItemStack upgrade) {
        List<ItemStack> items_equipped = new LinkedList<>();
        double modVal = modifier.getValue();
        AtomicReference<Double> newVal = new AtomicReference<>((double) 0);
        livingEntity.getItemsEquipped().forEach(items_equipped::add);
        if(livingEntity instanceof PlayerEntity player){
            TrinketsApi.TRINKET_COMPONENT.get(player).forEach((slot, item) -> items_equipped.add(item));
        }
        items_equipped.forEach(item -> {
            if(item.getItem() instanceof IUpgradeableItem){
                IUpgradeableItem.getUpgrades(item).forEach((slot, upgradeThing) -> {
                    if(upgrade.isItemEqual(upgradeThing)){
                        newVal.updateAndGet(v -> v + modVal);
                    }
                });
            }
        });
        EntityAttributeModifier newModif = new EntityAttributeModifier(modifier.getId(), modifier.getName(), newVal.get(), modifier.getOperation());
        AttributedEntityAttributeModifier newMod = new AttributedEntityAttributeModifier(attribute, newModif);
        this.modifiers.add(newMod);
    }

    public void addModifier(AttributedEntityAttributeModifier modifier, LivingEntity livingEntity, ItemStack upgrade) {
        List<ItemStack> items_equipped = new LinkedList<>();
        double modVal = modifier.getModifier().getValue();
        AtomicReference<Double> newVal = new AtomicReference<>((double) 0);
        livingEntity.getItemsEquipped().forEach(items_equipped::add);
        if(livingEntity instanceof PlayerEntity player){
            TrinketsApi.TRINKET_COMPONENT.get(player).forEach((slot, item) -> items_equipped.add(item));
        }
        items_equipped.forEach(item -> {
            if(item.getItem() instanceof IUpgradeableItem){
                IUpgradeableItem.getUpgrades(item).forEach((slot, upgradeThing) -> {
                    if(upgrade.isItemEqual(upgradeThing)){
                        newVal.updateAndGet(v -> v + modVal);
                    }
                });
            }
        });
        EntityAttributeModifier newModif = new EntityAttributeModifier(modifier.getModifier().getId(), modifier.getModifier().getName(), newVal.get(), modifier.getModifier().getOperation());
        AttributedEntityAttributeModifier newMod = new AttributedEntityAttributeModifier(modifier.getAttribute(), newModif);
        this.modifiers.add(newMod);
    }

    @Override
    public void onAdded() {
        if(!entity.world.isClient) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            modifiers.forEach(mod -> {
                if(entity.getAttributes().hasAttribute(mod.getAttribute())) {
                    Objects.requireNonNull(entity.getAttributeInstance(mod.getAttribute())).addTemporaryModifier(mod.getModifier());
                }
            });
            float afterMaxHealth = entity.getMaxHealth();
            if(updateHealth && afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }

    @Override
    public void onRemoved() {
        if(!entity.world.isClient) {
            float previousMaxHealth = entity.getMaxHealth();
            float previousHealthPercent = entity.getHealth() / previousMaxHealth;
            modifiers.forEach(mod -> {
                if (entity.getAttributes().hasAttribute(mod.getAttribute())) {
                    Objects.requireNonNull(entity.getAttributeInstance(mod.getAttribute())).removeModifier(mod.getModifier());
                }
            });
            float afterMaxHealth = entity.getMaxHealth();
            if(updateHealth && afterMaxHealth != previousMaxHealth) {
                entity.setHealth(afterMaxHealth * previousHealthPercent);
            }
        }
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(Callum.identifier("upgrade_attribute"),
                new SerializableData()
                        .add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, null)
                        .add("update_health", SerializableDataTypes.BOOLEAN, true)
                        .add("upgrade", SerializableDataTypes.ITEM_STACK),
                data ->
                        (type, entity) -> {
                            Pin_attribute ap = new Pin_attribute(type, entity, data.getBoolean("update_health"), data.get("upgrade"));
                            if(data.isPresent("modifier")) {
                                ap.addModifier(data.get("modifier"), entity, data.get("upgrade"));
                            }
                            if(data.isPresent("modifiers")) {
                                List<AttributedEntityAttributeModifier> modifierList = data.get("modifiers");
                                modifierList.forEach(mod -> ap.addModifier(mod, entity, data.get("upgrade")));
                            }

                            return ap;
                        });
    }
}
