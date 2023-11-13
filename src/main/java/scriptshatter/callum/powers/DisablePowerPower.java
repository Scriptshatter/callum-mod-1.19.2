package scriptshatter.callum.powers;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModelColorPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;

public class DisablePowerPower extends Power {
    private final PowerType<?> disabled_power;
    private final Identifier source;
    public DisablePowerPower(PowerType<?> type, LivingEntity entity, PowerType<?> disabledPower, @Nullable Identifier source) {
        super(type, entity);
        disabled_power = disabledPower;
        this.source = source;
    }

    @Override
    public void onAdded() {
        if(source != null){
            PowerHolderComponent.KEY.get(entity).removePower(disabled_power, source);
        }
        super.onAdded();
    }

    @Override
    public void onRemoved() {
        if(source != null){
            PowerHolderComponent.KEY.get(entity).addPower(disabled_power, source);
        }
        super.onRemoved();
    }

    public static PowerFactory<Power> factory(){
        return new PowerFactory<>(Callum.identifier("disable_power"), new SerializableData()
                .add("power", SerializableDataTypes.IDENTIFIER)
                .add("source", SerializableDataTypes.IDENTIFIER),
                (data) -> ((powerPowerType, livingEntity) -> {
                    PowerHolderComponent holder = PowerHolderComponent.KEY.get(livingEntity);
                    Identifier id = null;
                    if (holder.hasPower(PowerTypeRegistry.get(data.getId("power")), data.getId("source"))){
                        id = data.getId("source");
                    }
                    return new DisablePowerPower(powerPowerType, livingEntity, PowerTypeRegistry.get(data.getId("power")), id);
                }));
    }
}
