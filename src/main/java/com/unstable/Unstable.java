package com.unstable;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import com.unstable.command.OrbitalCommands;
import com.unstable.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unstable implements ModInitializer {
	public static final String MOD_ID = "unstable";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		OrbitalCommands.register();
		LOGGER.info("Registered orbital rods and commands.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
