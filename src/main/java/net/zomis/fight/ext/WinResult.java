package net.zomis.fight.ext;

public enum WinResult {
	WIN, LOSS, DRAW;
	
	public static <A> WinResult resultFor(A winner, A me, A drawValue) {
		if (winner == null) {
			if (drawValue == null) {
				return DRAW;
			}
			throw new NullPointerException("Winner was null");
		}
		
		if (winner.equals(me)) {
			return WIN;
		}
		else if (winner == drawValue || winner.equals(drawValue)) {
			return DRAW;
		}
		else {
			return LOSS;
		}
	}

	public static WinResult result(boolean draw, boolean win) {
		if (draw) {
			return WinResult.DRAW;
		}
		return win ? WinResult.WIN : WinResult.LOSS;
	}
	
	public int winValueInt() {
		switch (this) {
			case DRAW:
				return 0;
			case LOSS:
				return -1;
			case WIN:
				return 1;
		}
		throw new IllegalStateException("Unexpected value: " + this);
	}
	
	public double winValue() {
		return winValueInt();
	}

	public WinResult reversed() {
		return resultFor(this, LOSS, DRAW);
	}
}
