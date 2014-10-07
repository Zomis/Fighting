package net.zomis.fight.ext.rating;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import net.zomis.fight.ext.WinResult;

@Deprecated
public class RatingCollector<T, P> implements Collector<T, RatingProgress<P>, RatingResult<P>> {

	private Function<T, WinResult> ratingCalc;
	private Function<P, Double> startRating;
	private Function<T, P> who1;
	private Function<T, P> who2;

	public RatingCollector(Function<T, WinResult> ratingCalc, Function<P, Double> startRating, Function<T, P> who1, Function<T, P> who2) {
		this.ratingCalc = ratingCalc;
		this.startRating = startRating;
		this.who1 = who1;
		this.who2 = who2;
	}

	@Override
	public BiConsumer<RatingProgress<P>, T> accumulator() {
		return (progress, t) -> progress.add(t, who1, who2, ratingCalc);
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		return EnumSet.noneOf(Characteristics.class);
	}

	@Override
	public BinaryOperator<RatingProgress<P>> combiner() {
		return (a, b) -> new RatingProgress<P>(a, b);
	}

	@Override
	public Function<RatingProgress<P>, RatingResult<P>> finisher() {
		return progress -> progress.results();
	}

	@Override
	public Supplier<RatingProgress<P>> supplier() {
		return () -> new RatingProgress<P>(startRating);
	}
	
}
