package dev.andrybak.curling.util;

import dev.andrybak.curling.Command;
import dev.andrybak.curling.game.Player;
import dev.andrybak.curling.game.TeamColor;

public final class Advice {
	private Advice() {
	}

	public static void showAimAdvice(TeamColor teamColor) {
		System.out.printf("Team %s! Type \"%s\" and \"%s\" to aim the stick!%n",
			teamColor.getDisplayName(),
			Command.LEFT.getText(),
			Command.RIGHT.getText()
		);
	}

	public static void showShootingAdvice(TeamColor currentTeamColor, Player leader) {
		System.out.printf("@%s: type \"%s\" to shoot!%n",
			leader.getChatName(),
			Command.SHOOT.getText()
		);
	}
}
