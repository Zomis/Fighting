package net.zomis.fight.ext;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FightCollectors {

	private FightCollectors() {
		throw new UnsupportedOperationException();
	}
	
	public static <T> Collector<T, ?, Long> countingPredicate(Predicate<T> predicate) {
		return Collectors.collectingAndThen(Collectors.summarizingInt(fight -> predicate.test(fight) ? 1 : 0), sumInt -> sumInt.getSum());
	}

	public static <A, B> Indexer<Fight<A, B>> player1() {
		return (f) -> new Object[]{ f.getArenaParams().getFirstPlayer(), f.getArenaParams().getSecondPlayer() };
	}
	
	public static <A, B, C> Collector<A, B, C> filteredCollector(Predicate<A> predicate, Collector<A, B, C> collector) {
		return new Collector<A, B, C>() {
			@Override
			public BiConsumer<B, A> accumulator() {
				return new BiConsumer<B, A>() {
					@Override
					public void accept(B arg0, A arg1) {
						if (predicate.test(arg1))
							collector.accumulator().accept(arg0, arg1);
					}
				};
			}

			@Override
			public Set<java.util.stream.Collector.Characteristics> characteristics() {
				return collector.characteristics();
			}

			@Override
			public BinaryOperator<B> combiner() {
				return collector.combiner();
			}

			@Override
			public Function<B, C> finisher() {
				return collector.finisher();
			}

			@Override
			public Supplier<B> supplier() {
				return collector.supplier();
			}
		};
	}
	
	public static <A, B> Function<FNode<Fight<A, B>>, WinResult> fightResult(int myIndex,
			A drawValue, Function<FNode<Fight<A, B>>, A> winner) {
		return node -> node.hasIndex(myIndex) ?
				WinResult.resultFor(winner.apply(node), node.getIndex(myIndex), drawValue) : WinResult.DRAW;
	}
	
	public static <A, B> Indexer<FNode<Fight<A, B>>> player2(int firstIndex) {
		return f -> f.getIndex(firstIndex) == f.getF().getArenaParams().getFirstPlayer() ? 
				f.getF().getArenaParams().getSecondPlayer() : 
					f.getF().getArenaParams().getFirstPlayer();
	}
	
	public static Collector<WinResult, ?, WinStats> stats() {
		return new Collector<WinResult, WinStats, WinStats>() {
			@Override
			public BiConsumer<WinStats, WinResult> accumulator() {
				return WinStats::add;
			}

			@Override
			public Set<Collector.Characteristics> characteristics() {
				return EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
			}

			@Override
			public BinaryOperator<WinStats> combiner() {
				return WinStats::copyWith;
			}

			@Override
			public Function<WinStats, WinStats> finisher() {
				return Function.identity();
			}

			@Override
			public Supplier<WinStats> supplier() {
				return WinStats::new;
			}
		};
	}
	
}
