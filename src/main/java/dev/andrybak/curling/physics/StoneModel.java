package dev.andrybak.curling.physics;

import dev.andrybak.curling.game.TeamColor;

import java.awt.geom.Point2D;

/**
 * Stub for a physical model of a curling stone.
 */
public final class StoneModel {
	private final TeamColor teamColor;
	private Point2D.Float location = new Point2D.Float(0, 0);

	public StoneModel(TeamColor teamColor) {
		this.teamColor = teamColor;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}

	public Point2D.Float getLocation() {
		return location;
	}

	public void setCoordinates(Point2D aimCoordinates) {
		location.setLocation(aimCoordinates);
	}
}
