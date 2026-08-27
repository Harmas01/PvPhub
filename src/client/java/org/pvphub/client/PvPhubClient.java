package org.pvphub.client;

import net.fabricmc.api.ClientModInitializer;
import org.pvphub.client.hud.ArmorHud;

public class PvPhubClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ArmorHud.register();
	}
}
