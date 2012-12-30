package me.armar.plugins.autorank.playtimes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.earth2me.essentials.Essentials;

public class PlaytimesUpdate implements Runnable {

    @SuppressWarnings("unused")
	private Essentials ess;
    private Playtimes playtimes;

    public PlaytimesUpdate(Playtimes playtimes) {
	this.playtimes = playtimes;

	Plugin x = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	if (x != null & x instanceof Essentials) {
	    ess = (Essentials) x;
	} else {
	    // Autorank.logMessage("Essentials was NOT found! Disabling AFK integration.");
	}

    }

    @Override
    public void run() {
	Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	updateMinutesPlayed(onlinePlayers);
    }

    private void updateMinutesPlayed(Player[] players) {
	for (int i = (players.length - 1); i >= 0; i--) {
	    if (players[i] != null) {
		updateMinutesPlayed(players[i]);
	    }
	}
    }

    private void updateMinutesPlayed(Player player) {
	if (!player.hasPermission("autorank.timeexclude")) {
	    String playerName = player.getName().toLowerCase();
	    if (!playtimes.getKeys().contains(playerName)) {
		playtimes.setTime(playerName, 0);
	    }
	    playtimes.modifyTime(playerName, Playtimes.INTERVAL_MINUTES);
	}
    }

}