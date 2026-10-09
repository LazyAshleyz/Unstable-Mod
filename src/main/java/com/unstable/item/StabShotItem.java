package com.unstable.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public final class StabShotItem extends OrbitalRodItem {
	public StabShotItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected void onReelIn(ServerPlayer player) {
	}
}
