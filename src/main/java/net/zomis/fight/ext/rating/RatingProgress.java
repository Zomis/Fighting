package net.zomis.fight.ext.rating;

import java.util.function.Function;

import net.zomis.fight.ext.WinResult;

public class RatingProgress<P> {

//	private final ConcurrentMap<P, Double> ratings;
//	private final ConcurrentMap<P, Double> startRatings;
	private final Function<P, Double>	startRating;
	
	public RatingProgress(Function<P, Double> startRating) {
		this.startRating = startRating;
//		this.ratings = new ConcurrentHashMap<>();
//		this.startRatings = new ConcurrentHashMap<>();
	}

	public RatingProgress(RatingProgress<P> a, RatingProgress<P> b) {
		this(a.startRating);
		
	}
	
	public <T> void add(T t, Function<T, P> who1, Function<T, P> who2, Function<T, WinResult> ratingCalc) {
		WinResult result = ratingCalc.apply(t);
		result.winValue();
		
		
	}

	public RatingResult<P> results() {
		return null;
	}

}
