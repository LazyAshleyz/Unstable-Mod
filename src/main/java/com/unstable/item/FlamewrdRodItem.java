package com.unstable.item;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class FlamewrdRodItem extends OrbitalRodItem {
	private static final String[] SUMMON_TARGETS = {"Flamewrd", "_______________"};
	private static final double[] BEHIND_DISTANCES = {1.5, 2.5, 1.0};
	private static final double[] SIDE_OFFSETS = {0.0, 0.75, -0.75};
	private static final int[] VERTICAL_OFFSETS = {0, 1, -1, 2, -2, 3, -3};

	public FlamewrdRodItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected void onReelIn(ServerPlayer user) {
		ServerPlayer target = null;
		for (String username : SUMMON_TARGETS) {
			ServerPlayer candidate = findOnlinePlayer(user, username);
			if (candidate != null && candidate != user) {
				target = candidate;
				break;
			}
		}

		if (target == null) {
			user.sendSystemMessage(Component.literal(
					"Neither Flamewrd nor _______________ is online as a different player; the rod was consumed."));
			return;
		}

		ServerLevel destinationLevel = user.level();
		Vec3 destination = findSafePosition(destinationLevel, user, target);
		if (destination == null) {
			user.sendSystemMessage(Component.literal(
					"No safe position was found behind you; the rod was consumed."));
			return;
		}

		boolean teleported = target.teleportTo(destinationLevel,
				destination.x,
				destination.y,
				destination.z,
				Set.of(),
				target.getYRot(),
				target.getXRot(),
				false);
		if (!teleported) {
			user.sendSystemMessage(Component.literal(target.getGameProfile().name()
					+ " could not be teleported; the rod was consumed."));
		}
	}

	private static ServerPlayer findOnlinePlayer(ServerPlayer user, String username) {
		for (ServerPlayer onlinePlayer : user.level().getServer().getPlayerList().getPlayers()) {
			if (onlinePlayer.getGameProfile().name().equals(username)) {
				return onlinePlayer;
			}
		}
		return null;
	}

	private static Vec3 findSafePosition(ServerLevel level, ServerPlayer user, ServerPlayer target) {
		double yaw = Math.toRadians(user.getYRot());
		double behindX = Math.sin(yaw);
		double behindZ = -Math.cos(yaw);
		double sideX = Math.cos(yaw);
		double sideZ = Math.sin(yaw);
		int baseY = (int) Math.floor(user.getY());

		for (double distance : BEHIND_DISTANCES) {
			for (double sideOffset : SIDE_OFFSETS) {
				double x = user.getX() + behindX * distance + sideX * sideOffset;
				double z = user.getZ() + behindZ * distance + sideZ * sideOffset;

				for (int verticalOffset : VERTICAL_OFFSETS) {
					BlockPos feet = BlockPos.containing(x, baseY + verticalOffset, z);
					BlockPos ground = feet.below();
					if (!level.getWorldBorder().isWithinBounds(feet)) {
						continue;
					}

					BlockState floor = level.getBlockState(ground);
					BlockState feetState = level.getBlockState(feet);
					BlockState headState = level.getBlockState(feet.above());
					if (!floor.isFaceSturdy(level, ground, Direction.UP)
							|| isHazardous(floor)
							|| isHazardous(feetState)
							|| isHazardous(headState)
							|| !feetState.getFluidState().isEmpty()
							|| !headState.getFluidState().isEmpty()) {
						continue;
					}

					double halfWidth = target.getBbWidth() / 2.0;
					AABB bounds = new AABB(
							x - halfWidth, feet.getY(), z - halfWidth,
							x + halfWidth, feet.getY() + target.getBbHeight(), z + halfWidth);
					if (level.getWorldBorder().isWithinBounds(bounds) && level.noCollision(target, bounds)) {
						return new Vec3(x, feet.getY(), z);
					}
				}
			}
		}

		return null;
	}

	private static boolean isHazardous(BlockState state) {
		return state.is(Blocks.LAVA)
				|| state.is(Blocks.FIRE)
				|| state.is(Blocks.SOUL_FIRE)
				|| state.is(Blocks.CACTUS)
				|| state.is(Blocks.MAGMA_BLOCK)
				|| state.is(Blocks.CAMPFIRE)
				|| state.is(Blocks.SOUL_CAMPFIRE)
				|| state.is(Blocks.POWDER_SNOW)
				|| state.is(Blocks.SWEET_BERRY_BUSH)
				|| state.is(Blocks.POINTED_DRIPSTONE);
	}
}
