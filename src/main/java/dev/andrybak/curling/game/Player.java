package dev.andrybak.curling.game;

import java.util.*;

/**
 * Immutable.
 */
public final class Player {
	private static final int MAX_DISPLAYED_LENGTH = 15;
	private static final String ELLIPSIS = "...";

	private final String chatName;
	private final String displayedName;

	public Player(String chatName) {
		this.chatName = Objects.requireNonNull(chatName);
		if (chatName.length() > MAX_DISPLAYED_LENGTH) {
			displayedName = chatName.substring(0, MAX_DISPLAYED_LENGTH - ELLIPSIS.length()) + ELLIPSIS;
		} else {
			displayedName = chatName;
		}
	}

	public String getChatName() {
		return chatName;
	}

	public String getDisplayedName() {
		return displayedName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Player player = (Player)o;
		return chatName.equals(player.chatName);
	}

	@Override
	public int hashCode() {
		return chatName.hashCode();
	}
}
