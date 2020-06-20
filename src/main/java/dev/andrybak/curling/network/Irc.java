package dev.andrybak.curling.network;

/**
 * Stub for talking to IRC.
 */
public final class Irc {
	private final IrcListener listener;

	public interface IrcListener {
		void receiveMessage(String chatName, String message);
	}

	public Irc(IrcListener listener) {
		this.listener = listener;
	}

	public static Irc connect(String address, IrcListener listener) {
		System.out.println("Connecting to " + address);
		return new Irc(listener);
	}
}
