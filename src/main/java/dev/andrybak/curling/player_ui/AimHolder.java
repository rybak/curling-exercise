package dev.andrybak.curling.player_ui;

import dev.andrybak.curling.game.Direction;

/**
 * Simple placeholder for more complex physics
 */
public final class AimHolder {
	private int aimCoordinate = 0;

	public void aim(Direction dir) {
		switch (dir) {
		case LEFT:
			aimCoordinate--;
			break;
		case RIGHT:
			aimCoordinate++;
			break;
		}
	}

	public int getAimCoordinate() {
		return aimCoordinate;
	}
}
