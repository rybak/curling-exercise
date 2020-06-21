package dev.andrybak.curling.game;

import dev.andrybak.curling.physics.IceRinkModel;
import dev.andrybak.curling.streamer_ui.TurnsCountHolder;
import dev.andrybak.curling.util.Advice;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Game {
	private static final int REGISTRATION_PHASE_LENGTH_MILLIS = 30_000;
	private static final int END_SCREEN_PHASE_LENGTH_MILLIS = 10_000;
	private final PlayerStatistics stats;
	private final IceRinkModel iceRinkModel;
	private final TurnsCountHolder turnsCountHolder;
	private State state;
	private final Set<Player> players = new HashSet<>();
	private Match match;

	public Game(PlayerStatistics stats, IceRinkModel iceRinkModel) {
		this.stats = stats;
		this.iceRinkModel = iceRinkModel;
		this.turnsCountHolder = new TurnsCountHolder();
	}

	public void startRegistration() {
		state = State.REGISTRATION;
		turnsCountHolder.randomize();
		Advice.showEnterAdvice();
	}

	public void register(Player p) {
		if (state != State.REGISTRATION)
			return;
		players.add(Objects.requireNonNull(p));
		if (players.size() == 2)
			startRegistrationTimer();
	}

	private void startRegistrationTimer() {
		Timer registrationTimer = new Timer(REGISTRATION_PHASE_LENGTH_MILLIS, ignored -> prepareMatch());
		registrationTimer.setRepeats(false);
		registrationTimer.start();
	}

	private void prepareMatch() {
		match = new Match(
			new ArrayList<>(players),
			stats,
			turnsCountHolder.getTurnsCount(),
			new Match.EndOfMatchListener() {
				@Override
				public void win(TeamColor winner, double distance) {
					matchEndWin(winner, distance);
				}

				@Override
				public void nobodyWon() {
					matchEndNobodyWon();
				}
			},
			iceRinkModel
		);
		state = State.PLAY;
	}

	public void directionCommand(Player p, Direction dir) {
		if (state != State.PLAY)
			return;
		match.directionCommand(p, dir);
	}

	public void shootCommand(Player p) {
		if (state != State.PLAY)
			return;
		match.shootCommand(p);
	}

	private void matchEndWin(TeamColor winner, double distance) {
		matchEnd(String.format("Team %s has won! Distance is %.2f.", winner, distance));
	}

	private void matchEndNobodyWon() {
		matchEnd("Nobody won!");
	}

	private void matchEnd(String message) {
		if (state != State.PLAY)
			return;
		System.out.println(message);
		players.clear();
		state = State.END_SCREEN;
		Timer endScreenTimer = new Timer(END_SCREEN_PHASE_LENGTH_MILLIS, ignored -> startRegistration());
		endScreenTimer.setRepeats(false);
		endScreenTimer.start();
	}

	enum State {
		REGISTRATION,
		PLAY,
		END_SCREEN
	}
}
