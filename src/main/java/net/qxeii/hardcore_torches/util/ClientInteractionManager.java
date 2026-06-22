package net.qxeii.hardcore_torches.util;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.qxeii.hardcore_torches.Mod;

public class ClientInteractionManager {

	// Configuration

	private static final long INTERACTION_PREVENTION_COOLDOWN_TICKS = 50;

	// State

	public long lastPreventingInteractionTick = 0;

	// Init

	public ClientInteractionManager() {
		initializeInteractionTracker();
	}

	public void initializeInteractionTracker() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			var stack = player.getStackInHand(hand);
			var item = stack.getItem();

			if (item.isFood()) {
				Mod.LOGGER.info(
						"Tracking used consumable item '" + item.getTranslationKey() + "' for interaction prevention.");
				lastPreventingInteractionTick = world.getTime();
			}

			return TypedActionResult.pass(stack);
		});
	}

	// Check

	public boolean didConsumeItemWithinTimeout(World world) {
		var tick = world.getTime();
		return tick < lastPreventingInteractionTick + INTERACTION_PREVENTION_COOLDOWN_TICKS;
	}

}
