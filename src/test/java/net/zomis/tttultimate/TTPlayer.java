package net.zomis.tttultimate;

public enum TTPlayer {

	NONE, X, O, XO;
	
	public TTPlayer next() {
		if (!isExactlyOnePlayer())
			throw new UnsupportedOperationException("Only possible to call .next() on a real player but it was called on " + this);
		return this == X ? O : X;
	}
	
	/**
	 * Determine if this player is (also) another player.<br>
	 * This is the same as <code>this.and(other) == other</code>
	 * 
	 * @param other
	 * @return
	 */
	public boolean is(TTPlayer other) {
		return this.and(other) == other;
	}
	
	public TTPlayer and(TTPlayer other) {
		if (this == NONE || other == NONE || other == null)
			return NONE;
		if (isExactlyOnePlayer() && other.isExactlyOnePlayer())
			return this == other ? this : NONE;
		if (this == XO)
			return other;
		return other.and(this);
	}
	
	public boolean isExactlyOnePlayer() {
		return this == X || this == O;
	}
	
	public static boolean isExactlyOnePlayer(TTPlayer winner) {
		return winner != null && winner.isExactlyOnePlayer();
	}
	
	public TTPlayer or(TTPlayer other) {
		if (this == NONE || other == null)
			return other;
		if (other == NONE)
			return this;
		if (this == XO)
			return this;
		if (this != other)
			return XO;
		return this;
	}
	
	public static TTPlayer getPlayerByIndex(int player) {
		switch (player) {
			case 0: return TTPlayer.X;
			case 1:	return TTPlayer.O;
			default: return null;
		}
	}
	
}
