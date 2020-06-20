package dev.andrybak.curling.game;

import java.util.*;

public final class PlayerStatistics {
	private final Map<String /* chat name */, Integer> totalGames = new HashMap<>();
	private final Map<String /* chat name */, Integer> wins = new HashMap<>();

	public void recordGameWin(Map<TeamColor, Team> teams, TeamColor winner) {
		for (Player p : teams.get(winner).getAllPlayers())
			this.recordWin(p);
		TeamColor loser = winner.opposite();
		for (Player p : teams.get(loser).getAllPlayers())
			this.recordLoss(p);
	}

	public void recordGameNobodyWon(Map<TeamColor, Team> teams) {
		for (Team team : teams.values()) {
			for (Player p : team.getAllPlayers())
				recordLoss(p);
		}
	}

	public int getTotalGames(Player p) {
		return totalGames.computeIfAbsent(p.getChatName(), k -> 0);
	}

	public int getWins(Player p) {
		return wins.computeIfAbsent(p.getChatName(), k -> 0);
	}

	private void recordWin(Player p) {
		incrementGameCount(p);
		wins.compute(p.getChatName(), (k, v) -> v == null ? 1 : v + 1);
	}

	private void recordLoss(Player p) {
		incrementGameCount(p);
	}

	private void incrementGameCount(Player p) {
		totalGames.compute(p.getChatName(), (k, v) -> v == null ? 1 : v + 1);
	}

	public void loadFromFile(String playerStatsFilePath) {
		// TODO
		// 1. open file
		// 2. add numbers to fields totalGames and wins
	}
}
