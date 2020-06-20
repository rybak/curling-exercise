package dev.andrybak.curling.game;

public enum TeamColor {
	RED {
		@Override
		public TeamColor opposite() {
			return YELLOW;
		}

		@Override
		public String getDisplayName() {
			return "Red";
		}
	}, YELLOW {
		@Override
		public TeamColor opposite() {
			return RED;
		}

		@Override
		public String getDisplayName() {
			return "Yellow";
		}
	};

	public abstract TeamColor opposite();

	public abstract String getDisplayName();
}
