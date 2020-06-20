package dev.andrybak.curling.game;

import dev.andrybak.curling.physics.IceRinkModel;
import dev.andrybak.curling.physics.StoneModel;
import dev.andrybak.curling.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Match {

	private final PlayerStatistics stats;
	private final Map<TeamColor, Team> teams;
	private final Curling curling;
	private final IceRinkModel iceRinkModel;
	private final EndOfMatchListener endOfMatchListener;
	private TeamColor currentTurn;
	private int turnsLeft;

	public Match(List<Player> allPlayers, PlayerStatistics stats, int turnsCount,
		EndOfMatchListener endOfMatchListener, IceRinkModel iceRinkModel) {
		this.stats = stats;
		this.iceRinkModel = iceRinkModel;
		List<Player> redTeamPlayers = new ArrayList<>();
		List<Player> yellowTeamPlayers = new ArrayList<>();
		TeamSelector.randomSplit(allPlayers, redTeamPlayers, yellowTeamPlayers);
		Team redTeam = Team.create(redTeamPlayers, stats);
		Team yellowTeam = Team.create(yellowTeamPlayers, stats);
		teams = Collections.unmodifiableMap(createTeamsMap(redTeam, yellowTeam));

		// first turn goes to more winning team
		int yellowWinCount = Utils.getTotalWins(yellowTeam, stats);
		int redWinCount = Utils.getTotalWins(redTeam, stats);
		currentTurn = (redWinCount > yellowWinCount) ? TeamColor.RED : TeamColor.YELLOW;
		turnsLeft = turnsCount * teams.size();

		this.endOfMatchListener = endOfMatchListener;

		curling = new Curling(this::turnEnded, iceRinkModel);

		displayTeams();
	}

	private static Map<TeamColor, Team> createTeamsMap(Team redTeam, Team yellowTeam) {
		Map<TeamColor, Team> tmp = new EnumMap<>(TeamColor.class);
		tmp.put(TeamColor.RED, redTeam);
		tmp.put(TeamColor.YELLOW, yellowTeam);
		return tmp;
	}

	private void displayTeams() {
		for (Map.Entry<TeamColor, Team> entry : teams.entrySet()) {
			Team team = entry.getValue();
			System.out.println(Utils.displayTeam(entry.getKey(), team, stats));
			System.out.println(String.format("Your leader is @%s.", team.getLeader()));
		}
	}

	private Team getCurrentTurnTeam() {
		return getTeam(currentTurn);
	}

	public void directionCommand(Player p, Direction dir) {
		switch (curling.getState()) {
		case AIMING:
			if (!getCurrentTurnTeam().getAimingPlayers().contains(p))
				return;
			curling.aim(dir);
			break;
		case SWEEPING:
			if (!getCurrentTurnTeam().getSweepingPlayers().contains(p))
				return;
			curling.sweep(dir);
			break;
		default:
			// do nothing
			break;
		}
	}

	public void shootCommand(Player p) {
		if (curling.getState() != Curling.State.SHOOTING)
			return;
		if (!getCurrentTurnTeam().getLeader().equals(p))
			return;
		curling.shoot();
	}

	private void turnEnded() {
		turnsLeft--;
		if (turnsLeft <= 0) {
			endMatch();
			return;
		}
		iceRinkModel.resetShootingStone();
		currentTurn = currentTurn.opposite();
		curling.startAiming(getCurrentTurnTeamColor(), getCurrentTurnTeam());
	}

	private void endMatch() {
		Optional<StoneModel> maybeClosest = iceRinkModel.getClosestStone();
		if (maybeClosest.isPresent()) {
			StoneModel closest = maybeClosest.get();
			double distance = IceRinkModel.distanceToHouse(closest.getLocation());
			TeamColor winner = closest.getTeamColor();
			endOfMatchListener.win(winner, distance);
			stats.recordGameWin(teams, winner);
		} else {
			endOfMatchListener.nobodyWon();
			stats.recordGameNobodyWon(teams);
		}
		iceRinkModel.resetIceRink();
		currentTurn = null;
	}

	/**
	 * This getter could be used from outside for team lists, for example.
	 */
	public Team getTeam(TeamColor color) {
		return teams.get(color);
	}

	/**
	 * This getter can be used from outside for visuals, for example.
	 */
	public TeamColor getCurrentTurnTeamColor() {
		return currentTurn;
	}

	public interface EndOfMatchListener {
		void win(TeamColor winner, double distance);

		void nobodyWon();
	}
}
