package dev.andrybak.curling.physics;

import dev.andrybak.curling.game.Direction;
import dev.andrybak.curling.game.TeamColor;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

/**
 * Stub for physics and such.
 */
public final class IceRinkModel {

	private static final Point2D.Float AIM_COORDINATES = new Point2D.Float(0, -30);
	private static final Point2D.Float HOUSE_COORDINATES = new Point2D.Float(0, 30);
	private static final float SWEEP_ZONE_START_Y = -25;
	private static final float SWEEP_ZONE_END_Y = 25;

	private StoneModel shootingStone;
	private Set<StoneModel> stones; // stones currently on the ice

	public static double distanceToHouse(Point2D.Float point) {
		return HOUSE_COORDINATES.distance(point);
	}

	public void resetIceRink() {
		clearStones();
	}

	public void generateShootingStone(TeamColor color) {
		shootingStone = new StoneModel(color);
		shootingStone.setCoordinates(AIM_COORDINATES);
		stones.add(shootingStone);
	}

	public void shoot(float power, float aim) {
	}

	public void sweep(Direction dir) {
	}

	public void resetShootingStone() {
		shootingStone = null;
	}

	private void clearStones() {
		stones.clear();
	}

	public Optional<StoneModel> getClosestStone() {
		return stones.stream()
			.min(Comparator.comparingDouble(
				stone -> HOUSE_COORDINATES.distance(stone.getLocation())
			));
	}

	public boolean isSweepingAllowed() {
		if (shootingStone == null)
			return false;
		double y = shootingStone.getLocation().getY();
		return SWEEP_ZONE_START_Y < y && y < SWEEP_ZONE_END_Y;
	}
}
