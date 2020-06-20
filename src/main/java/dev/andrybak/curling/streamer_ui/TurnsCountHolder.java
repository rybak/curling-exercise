package dev.andrybak.curling.streamer_ui;

import java.util.Random;

public final class TurnsCountHolder {
	private static final int MIN_RANDOM_COUNT = 2;
	private static final int MAX_RANDOM_COUNT = 8;

	private static final Random RNG = new Random();
	private int turnsCount = 4; // https://xkcd.com/221/

	public int getTurnsCount() {
		return turnsCount;
	}

	public void randomize() {
		turnsCount = MIN_RANDOM_COUNT + RNG.nextInt(MAX_RANDOM_COUNT - MIN_RANDOM_COUNT + 1);
	}
}
