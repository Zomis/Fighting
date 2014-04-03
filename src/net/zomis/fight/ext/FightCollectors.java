package net.zomis.fight.ext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class FightCollectors {

	private FightCollectors() {
		throw new UnsupportedOperationException();
	}
	
	public static Collector<WinResult, ?, WinStats> stats() {
		return new Collector<WinResult, WinStats, WinStats>() {
			@Override
			public BiConsumer<WinStats, WinResult> accumulator() {
				return WinStats::add;
			}

			@Override
			public Set<Collector.Characteristics> characteristics() {
				return new HashSet<>(Arrays.asList(Characteristics.CONCURRENT, Characteristics.IDENTITY_FINISH));
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
