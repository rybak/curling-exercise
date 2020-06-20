package dev.andrybak.curling.game;

import java.util.*;

public final class TeamSelector {
	private static final Random RNG = new Random();

	private TeamSelector() { // no construction for utility class
	}

	/**
	 * Emulates "playground" team choosing. Split the {@code input} into halves randomly in O(input.size()) time. If
	 * {@code input.size()} is odd, then {@code output1} will be one {@link Player} bigger than {@code output2}.
	 */
	static void randomSplit(List<Player> input, List<Player> output1, List<Player> output2) {
		int playersLeft = input.size();
		boolean firstTurn = true;
		for (int i = 0, n = input.size(); i < n; i++) {
			int chosenIndex = RNG.nextInt(playersLeft);
			playersLeft--;
			swap(input, chosenIndex, playersLeft);
			Player chosen = input.get(playersLeft);
			if (firstTurn) {
				output1.add(chosen);
			} else {
				output2.add(chosen);
			}
			firstTurn = !firstTurn;
		}
	}

	private static <E> void swap(List<E> list, int i, int j) {
		E tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);
	}
}
