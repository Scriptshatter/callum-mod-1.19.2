package scriptshatter.callum.powers;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import scriptshatter.callum.Callum;

public class DisablePowerPower extends Power {
    @Nullable private final PowerType<?> disabled_power;
    private final Identifier source;
    private boolean has_power;
    public DisablePowerPower(PowerType<?> type, LivingEntity entity, @Nullable PowerType<?> disabledPower, @Nullable Identifier source) {
        super(type, entity);
        disabled_power = disabledPower;
        this.source = source;
        this.setTicking();
    }

    @Override
    public NbtElement toTag() {
        return NbtByte.of(has_power);
    }

    @Override
    public void fromTag(NbtElement tag) {
        has_power = ((NbtByte)tag).byteValue() > 0;
    }

    @Override
    public void tick() {
        PowerHolderComponent.KEY.get(entity).removePower(disabled_power, source);
        PowerHolderComponent.sync(entity);
        super.tick();
    }

    @Override
    public void onLost() {
        if(has_power){
            PowerHolderComponent.KEY.get(entity).addPower(disabled_power, source);
            PowerHolderComponent.sync(entity);
        }
        super.onLost();
    }

    @Override
    public void onGained() {
        has_power = disabled_power != null && PowerHolderComponent.KEY.get(entity).hasPower(disabled_power, source);
        super.onGained();
    }

    public static PowerFactory<Power> factory(){
        return new PowerFactory<>(Callum.identifier("disable_power"), new SerializableData()
                .add("power", SerializableDataTypes.IDENTIFIER)
                .add("source", SerializableDataTypes.IDENTIFIER),
                (data) -> ((powerPowerType, livingEntity) -> {
                    if(PowerTypeRegistry.contains(data.getId("power"))){
                        return new DisablePowerPower(powerPowerType, livingEntity, PowerTypeRegistry.get(data.getId("power")), data.getId("source"));
                    }
                    return new DisablePowerPower(powerPowerType, livingEntity, null, data.getId("source"));
                }));
    }
}
