package dev.andrybak.curling;

import dev.andrybak.curling.game.Direction;
import dev.andrybak.curling.game.Game;
import dev.andrybak.curling.game.Player;
import dev.andrybak.curling.game.PlayerStatistics;
import dev.andrybak.curling.network.Irc;
import dev.andrybak.curling.physics.IceRinkModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public final class GameManager {

	private final PlayerStatistics stats = new PlayerStatistics();
	private final Irc irc;
	private final IceRinkModel iceRinkModel = new IceRinkModel();
	private final Game game;

	GameManager(String configFilePath) {
		Config config = null;
		try {
			config = Config.create(new FileInputStream(configFilePath));
		} catch (IOException e) {
			System.err.println("Could not read " + configFilePath);
			e.printStackTrace();
			System.exit(1);
		}
		irc = Irc.connect(config.getIrcAddress(), this::parseMessage);
		game = new Game(stats, iceRinkModel);
		game.startRegistration();

		stats.loadFromFile(config.getPlayerStatsFilePath());
	}

	private void parseMessage(String chatName, String message) {
		Optional<Command> maybeCommand = Command.fromText(message);
		if (!maybeCommand.isPresent())
			return;
		Player p = new Player(chatName);
		switch (maybeCommand.get()) {
		case ENTER:
			game.register(p);
			break;
		case LEFT:
			game.directionCommand(p, Direction.LEFT);
			break;
		case RIGHT:
			game.directionCommand(p, Direction.RIGHT);
			break;
		case SHOOT:
			game.shootCommand(p);
			break;
		}
	}
}
