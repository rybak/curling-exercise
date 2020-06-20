package dev.andrybak.curling.util;

import dev.andrybak.curling.game.*;

public final class Utils {
	private Utils() {
	}

	public static int getTotalWins(Team team, PlayerStatistics stats) {
		return team.getAllPlayers().stream()
			.mapToInt(stats::getWins)
			.sum();
	}

	public static String displayTeam(TeamColor color, Team t, PlayerStatistics stats) {
		return String.format("%s Team (%d)", color, getTotalWins(t, stats));
	}

	public static String displayPlayer(Player p, PlayerStatistics stats) {
		return String.format("%s (%d/%d)", p.getDisplayedName(), stats.getWins(p), stats.getTotalGames(p));
	}
}
