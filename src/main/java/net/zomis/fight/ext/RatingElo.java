package net.zomis.fight.ext;

import java.util.List;

public class RatingElo {
	private final double K;
	private final double DIV;
	
	public RatingElo() {
		this(30.0, 2400.0);
	}

	public RatingElo(double K, double DIV) {
		this.DIV = DIV;
		this.K = K;
	}
	
	@Deprecated
	public double calculateMultiRatingChange(double user, List<Double> opponents, Double w) {
		// TODO: When calculating multi-rating changes, you need to know who won/lost against who. A, B, C, D. C lost against A and B but won against D.
		double sum = 0;
		for (double value : opponents) {
			sum += calculateRatingChange(user, value, w);
		}
		return sum;
	}
	
	public double calculateWinExpected(double userRating, double opponentRating) {
		double ratingDiff = userRating - opponentRating;
		return 1 / (Math.pow(10, -ratingDiff / DIV) + 1);
	}
	
	public double calculateRatingChange(double userRating, double opponentRating, WinResult result) {
		return calculateRatingChange(userRating, opponentRating, result.winValue());
	}
	
	public double calculateRatingChange(double userRating, double opponentRating, double winValue) {
		double winExpected = calculateWinExpected(userRating, opponentRating);
		double myK = K;

		winValue = winValue / 2.0 + 0.5;// w 0 -. 0.5, w -1 -> 0 (inconsistency between parameter and how it"s used in formula below)
//		logger.trace(String.format("Rating calc: my=%f, opp=%f, k=%f w=%f we=%f dr=%f, result: %f", user, opponents, k, w, we, dr, 1.0 * k * (w - we)));
		return 1.0 * myK * (winValue - winExpected);
	}


}
