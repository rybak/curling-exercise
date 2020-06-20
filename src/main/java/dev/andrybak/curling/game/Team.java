package dev.andrybak.curling.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Team {
	private static final Random RNG = new Random();

	private final Set<Player> allPlayers;
	private final Player leader;
	private final Set<Player> sweepingPlayers;

	private Team(List<Player> allPlayers, Player leader, List<Player> sweepingPlayers) {
		this.allPlayers = Collections.unmodifiableSet(new HashSet<>(allPlayers));
		this.leader = leader;
		this.sweepingPlayers = Collections.unmodifiableSet(new HashSet<>(sweepingPlayers));
	}

	public static Team create(List<Player> players, PlayerStatistics stats) {
		if (players.isEmpty())
			throw new IllegalArgumentException("Can't create a team with zero players.");
		List<Player> tmp = new ArrayList<>(players);
		int leaderIndex;
		Predicate<Player> isExperienced = p -> stats.getTotalGames(p) > 0;
		if (players.stream().anyMatch(isExperienced)) {
			List<Player> experiencedPlayers = players.stream()
				.filter(isExperienced)
				.collect(Collectors.toList());
			leaderIndex = tmp.indexOf(experiencedPlayers.get(RNG.nextInt(experiencedPlayers.size())));
		} else {
			// when all players are inexperienced
			leaderIndex = RNG.nextInt(tmp.size());
		}
		Player leader = tmp.remove(leaderIndex);

		return new Team(players, leader, tmp);
	}

	public Set<Player> getAllPlayers() {
		return allPlayers;
	}

	public Set<Player> getAimingPlayers() {
		return allPlayers;
	}

	public Player getLeader() {
		return leader;
	}

	public Set<Player> getSweepingPlayers() {
		return sweepingPlayers;
	}
}
