package dev.andrybak.curling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
	private static final String CURLING_ADDRESS_PROPERTY = "curling.address";
	private static final String CHANNEL_NAME_PROPERTY = "curling.channel";
	private static final String STATS_FILE_PATH_PROPERTY = "curling.stats";

	private final String ircAddress;
	private final String channelName;
	private final String playerStatsFilePath;

	public Config(String ircAddress, String channelName, String playerStatsFilePath) {
		this.ircAddress = ircAddress;
		this.channelName = channelName;
		this.playerStatsFilePath = playerStatsFilePath;
	}

	public static Config create(InputStream inputStream) throws IOException {
		Properties props = new Properties();
		props.load(inputStream);
		return new Config(
			props.getProperty(CURLING_ADDRESS_PROPERTY),
			props.getProperty(CHANNEL_NAME_PROPERTY),
			props.getProperty(STATS_FILE_PATH_PROPERTY)
		);
	}

	public String getIrcAddress() {
		return ircAddress;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getPlayerStatsFilePath() {
		return playerStatsFilePath;
	}
}
