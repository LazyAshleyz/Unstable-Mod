package com.unstable.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.unstable.item.ModItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class OrbitalCommands {
	private OrbitalCommands() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(Commands.literal("orbital")
						.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
						.then(giveCommand("Nuke", ModItems.NUKE_SHOT, "Nuke Shot"))
						.then(giveCommand("Stab", ModItems.STAB_SHOT, "Stab Shot"))
						.then(giveCommand("Flamewrd", ModItems.FLAMEWRD_ROD, "Flamewrd Rod"))));
	}

	private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> giveCommand(
			String weapon, Item item, String displayName) {
		return Commands.literal(weapon)
				.then(Commands.literal("give")
						.then(Commands.argument("playername", EntityArgument.player())
								.then(Commands.argument("amount", IntegerArgumentType.integer(1))
										.executes(context -> giveItem(
												context.getSource(),
												EntityArgument.getPlayer(context, "playername"),
												item,
												displayName,
												IntegerArgumentType.getInteger(context, "amount"))))));
	}

	private static int giveItem(CommandSourceStack source, ServerPlayer target, Item item, String displayName, int amount) {
		for (int index = 0; index < amount; index++) {
			ItemStack rod = new ItemStack(item);
			if (!target.getInventory().add(rod) && !rod.isEmpty()) {
				target.drop(rod, false);
			}
		}

		source.sendSuccess(
				() -> Component.literal("Gave " + amount + " " + displayName + " to "
						+ target.getGameProfile().name() + "."),
				true);
		return amount;
	}
}
