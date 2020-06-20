package dev.andrybak.curling;

import java.util.Optional;

public enum Command {
	ENTER("enter"),
	LEFT("left"),
	RIGHT("right"),
	SHOOT("shoot");

	private final String text;

	Command(String text) {
		this.text = text;
	}

	public static Optional<Command> fromText(String text) {
		text = text.toLowerCase();
		for (Command c : values()) {
			if (text.startsWith(c.getText()))
				return Optional.of(c);
		}
		return Optional.empty();
	}

	public String getText() {
		return text;
	}
}
