package me.armar.plugins.autorank.playerchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import me.armar.plugins.autorank.Autorank;
import me.armar.plugins.autorank.AutorankTools;

/*
 * PlayerCheckerTrigger stores what players it recently checked for a 
 * promotion and makes PlayerChecker check a new player every 1-5
 * minutes (depending on the amount of online players).
 * 
 */

public class PlayerCheckerTrigger implements Runnable {

	private Autorank plugin;
	private List<Player> playersToBeChecked = new ArrayList<Player>();
	private PlayerChecker checker;

	public PlayerCheckerTrigger(Autorank plugin, PlayerChecker checker) {
		this.plugin = plugin;
		this.checker = checker;
		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, this, 200);
	}

	public void run() {
		Player[] players = plugin.getServer().getOnlinePlayers();

		if (playersToBeChecked.size() == 0)
			playersToBeChecked.addAll(Arrays.asList(players));

		if (playersToBeChecked.size() != 0) {
			Player player = playersToBeChecked.get(0);

			// TODO: Player would not be ranked up if one of the requirement is not auto complete
			if (!AutorankTools.isExcluded(player))
				checker.checkPlayer(player);

			playersToBeChecked.remove(player);
		}

		// Check every 5 minutes
		int nextCheck = 6000;
		if (players.length > 0) {
			nextCheck = nextCheck / players.length;
			
			// When check time is lower than 1 minute, change it to 1 minute
			// Decreases load on server
			if (nextCheck < 1200) {
				nextCheck = 1200;
			}
		}

		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, this, nextCheck);
	}

}
