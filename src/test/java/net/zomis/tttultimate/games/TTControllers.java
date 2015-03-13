package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTFactories;


public class TTControllers {

	public static TTController connectFour() {
		return new TTClassicControllerWithGravity(new TTFactories().classicMNK(7, 6, 4));
	}

	public static TTController classicTTT() {
		return new TTClassicController(new TTFactories().classicMNK(3));
	}

	public static TTController ultimateTTT() {
		return new TTUltimateController(new TTFactories().ultimate());
	}

}
