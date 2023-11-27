package scriptshatter.callum.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import scriptshatter.callum.Callum;

public class CallumSounds {

    public static SoundEvent DOUBLE_JUMP = registerSoundEvent("double_jump");
    public static SoundEvent PIN_OFF = registerSoundEvent("pin_off");
    public static SoundEvent PIN_ON = registerSoundEvent("pin_on");
    public static SoundEvent LENS_OFF = registerSoundEvent("lens_off");
    public static SoundEvent LENS_ON = registerSoundEvent("lens_on");


    private static SoundEvent registerSoundEvent(String name){
        Identifier id = Callum.identifier(name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void veg(){
        Callum.LOGGER.info("Hello there jimmy");
    }
}
