package dev.andrybak.curling.game;

import dev.andrybak.curling.physics.*;
import dev.andrybak.curling.player_ui.*;
import dev.andrybak.curling.util.*;

import javax.swing.*;

public final class Curling {

	// arbitrary time intervals
	private static final int AIMING_PHASE_LENGTH_MILLIS = 10_000;
	private static final int SHOOTING_PHASE_MAX_LENGTH_MILLIS = 9_000;
	private static final int CHECK_STONE_INTERVAL_MILLIS = 100;
	private static final int HOUSE_PHASE_LENGTH_MILLIS = 6_000;

	private final Runnable endOfTurnListener;
	private final IceRinkModel iceRinkModel;
	private AimHolder aimHolder;
	private PowerLevelHolder powerLevelHolder;
	private State state = State.PAUSE;
	private TeamColor currentTeamColor;
	private Team currentTeam;
	private Timer shootingTimer;
	private Timer checkSweepingTimer;

	public Curling(Runnable endOfTurnListener, IceRinkModel iceRinkModel) {
		this.endOfTurnListener = endOfTurnListener;
		this.iceRinkModel = iceRinkModel;
	}

	void startAiming(TeamColor teamColor, Team team) {
		if (state != State.PAUSE)
			return;
		iceRinkModel.generateShootingStone(teamColor);
		currentTeamColor = teamColor;
		currentTeam = team;
		state = State.AIMING;
		this.aimHolder = new AimHolder();
		Advice.showAimAdvice(currentTeamColor);
	}

	void aim(Direction dir) {
		if (state != State.AIMING)
			return;
		aimHolder.aim(dir);
		Timer aimTimer = new Timer(AIMING_PHASE_LENGTH_MILLIS, ignored -> endAiming());
		aimTimer.setRepeats(false);
		aimTimer.start();
	}

	void endAiming() {
		if (state != State.AIMING)
			return;
		state = State.SHOOTING;
		powerLevelHolder = new PowerLevelHolder();
		// TODO start fancy power level animation here
		Advice.showShootingAdvice(currentTeamColor, currentTeam.getLeader());
		shootingTimer = new Timer(SHOOTING_PHASE_MAX_LENGTH_MILLIS, ignored -> shoot());
		shootingTimer.setRepeats(false);
		shootingTimer.start();
	}

	void shoot() {
		if (state != State.SHOOTING)
			return;
		state = State.SWEEPING;
		iceRinkModel.shoot(powerLevelHolder.getPowerLevel(), aimHolder.getAimCoordinate());
		shootingTimer.stop();
		shootingTimer = null;
		checkSweepingTimer = new Timer(CHECK_STONE_INTERVAL_MILLIS, ignored -> checkSweeping());
		checkSweepingTimer.start();
	}

	private void checkSweeping() {
		if (!iceRinkModel.isSweepingAllowed())
			endSweeping();
	}

	void sweep(Direction dir) {
		if (state != State.SWEEPING)
			return;
		iceRinkModel.sweep(dir);
	}

	void endSweeping() {
		if (state != State.SWEEPING)
			return;
		checkSweepingTimer.stop();
		checkSweepingTimer = null;
		state = State.HOUSE;
		Timer showHouseTimer = new Timer(HOUSE_PHASE_LENGTH_MILLIS, ignored -> endHouse());
		showHouseTimer.setRepeats(false);
		showHouseTimer.start();
	}

	void endHouse() {
		if (state != State.HOUSE)
			return;
		state = State.PAUSE;
		endOfTurnListener.run();
	}

	public State getState() {
		return state;
	}

	public AimHolder getAim() {
		if (state != State.AIMING)
			return null;
		return aimHolder;
	}

	public enum State {
		PAUSE,
		AIMING,
		SHOOTING,
		SWEEPING,
		HOUSE,
	}
}
