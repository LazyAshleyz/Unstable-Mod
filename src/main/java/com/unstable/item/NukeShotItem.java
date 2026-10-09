package com.unstable.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public final class NukeShotItem extends OrbitalRodItem {
	public NukeShotItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected void onReelIn(ServerPlayer player) {
	}
}
