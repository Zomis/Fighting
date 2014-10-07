package net.zomis.fight.ext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class RatingContext<P> {
	// TODO: JavaFX Charts? http://docs.oracle.com/javafx/2/charts/jfxpub-charts.htm
	// TODO: Add rating fight, cleanup code (taken from MFE). Process either x fights (random or evenly distributed), or process fights until no change anymore.
	// Can there be a situation where A is good vs B and bad vs C, while B is good vs C ? A > B, B > C, C > A. Like a roundabout.
	// Rating could be as a collector, possibly with some initial ratings. -- DoubleSumStatistics
	
	private final Function<P, Double> startings;
	private final RatingElo elo;
	private final Map<P, Double> ratings = new HashMap<>();

	public RatingContext(Function<P, Double> startingRatings) {
		this.startings = startingRatings;
		this.elo = new RatingElo();
	}

	public void addFight(P player1, P player2, WinResult result) {
		double original1 = ratings.computeIfAbsent(player1, startings);
		double original2 = ratings.computeIfAbsent(player2, startings);
		double change1 = elo.calculateRatingChange(ratings.get(player1), ratings.get(player2), result);
		double change2 = elo.calculateRatingChange(ratings.get(player2), ratings.get(player1), result.reversed());
		System.out.println(String.format("%s (%f) vs. %s (%f): %s. Changes: %f, %f", player1, original1, player2, original2, result, change1, change2));
		ratings.put(player1, original1 + change1);
		ratings.put(player2, original2 + change2);
	}
	
	public void setRating(P player, double rating) {
		Objects.requireNonNull(player);
		this.ratings.put(player, rating);
	}
	
	public void clearRating(P player) {
		Objects.requireNonNull(player);
		this.ratings.remove(player);
	}
	
	public <A> Map<P, Double> calculateRatings(Stream<A> fightStream, Function<A, P> who1, Function<A, P> who2, Function<A, WinResult> ratingCalc) {
		fightStream.sequential()
			.forEach(fight -> this.addFight(who1.apply(fight), who2.apply(fight), ratingCalc.apply(fight))
		);
		return new HashMap<>(ratings);
	}
}
