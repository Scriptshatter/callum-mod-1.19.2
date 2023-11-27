package scriptshatter.callum;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scriptshatter.callum.items.ItemRegister;
import scriptshatter.callum.networking.Post_office;
import scriptshatter.callum.powers.CallumPowerFactory;
import scriptshatter.callum.sound.CallumSounds;

public class Callum implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "callum";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier identifier(String path) {
		return new Identifier(Callum.MOD_ID, path);
	}
	@Override
	public void onInitialize() {
		ItemRegister.register_moditems();
		Post_office.register_shitty_ass_mother_fucking_mail_C2S();
		CallumPowerFactory.register();
		CallumSounds.veg();
		LOGGER.info("Hello Fabric world!");
	}


}