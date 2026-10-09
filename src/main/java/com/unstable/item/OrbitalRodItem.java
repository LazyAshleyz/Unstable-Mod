package com.unstable.item;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.UUID;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.entity.player.Player;

public abstract class OrbitalRodItem extends FishingRodItem {
	private static final Map<ServerPlayer, ActiveHook> ACTIVE_HOOKS = new WeakHashMap<>();

	protected OrbitalRodItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			return super.use(level, player, hand);
		}

		if (player.fishing != null) {
			if (!(player instanceof ServerPlayer serverPlayer)) {
				return InteractionResult.FAIL;
			}

			FishingHook hook = player.fishing;
			ActiveHook activeHook = ACTIVE_HOOKS.get(serverPlayer);
			if (activeHook == null || !activeHook.hookId().equals(hook.getUUID()) || activeHook.rod() != this) {
				return InteractionResult.FAIL;
			}

			ACTIVE_HOOKS.remove(serverPlayer);
			ItemStack stack = player.getItemInHand(hand);
			hook.retrieve(stack);
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 1.0F);
			stack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
			stack.shrink(1);
			onReelIn(serverPlayer);
			return InteractionResult.SUCCESS;
		}

		InteractionResult result = super.use(level, player, hand);
		if (player instanceof ServerPlayer serverPlayer && serverPlayer.fishing != null) {
			ACTIVE_HOOKS.put(serverPlayer, new ActiveHook(serverPlayer.fishing.getUUID(), this));
		}
		return result;
	}

	protected abstract void onReelIn(ServerPlayer player);

	private record ActiveHook(UUID hookId, OrbitalRodItem rod) {
	}
}
