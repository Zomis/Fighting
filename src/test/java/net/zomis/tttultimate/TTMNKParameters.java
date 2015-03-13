package net.zomis.tttultimate;

public class TTMNKParameters {

	private final int width;
	private final int height;
	private final int consecutiveRequired;

	public TTMNKParameters(int width, int height, int consecutiveRequired) {
		 this.width = width;
		 this.height = height;
		 this.consecutiveRequired = consecutiveRequired;
	}
	
	public int getConsecutiveRequired() {
		return consecutiveRequired;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
}
