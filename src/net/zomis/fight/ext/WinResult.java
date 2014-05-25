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
	
	public double winValue() {
		switch (this) {
			case DRAW:
				return 0.0;
			case LOSS:
				return -1.0;
			case WIN:
				return 1.0;
		}
		throw new IllegalStateException("Unexpected value: " + this);
	}

	public WinResult reversed() {
		return resultFor(this, LOSS, DRAW);
	}
}
