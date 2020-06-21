package dev.andrybak.curling.network;

import javax.swing.*;

/**
 * Stub for talking to IRC.
 */
public final class Irc {
	private final IrcListener listener;

	public Irc(IrcListener listener) {
		this.listener = listener;
	}

	public static Irc connect(String address, IrcListener listener) {
		System.out.println("Connecting to " + address);
		Irc irc = new Irc(listener);
		SwingUtilities.invokeLater(() -> irc.processIrcMessage("example", "enter stub message"));
		return irc;
	}

	private void processIrcMessage(String ircName, String message) {
		listener.receiveMessage(ircName, message);
	}

	public interface IrcListener {
		void receiveMessage(String chatName, String message);
	}
}
