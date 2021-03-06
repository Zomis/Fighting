package net.zomis.fight.ext;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;


public class IndexResults {

	private final Map<String, Object> values;
	private final Map<String, Collector<?, ?, ?>> coll;
	
	public IndexResults() {
		this.values = new ConcurrentHashMap<>();
		this.coll = new ConcurrentHashMap<>();
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
	public <A, B, C> void addAdvancedData(String strkey, Collector<FNode<A>, B, C> collector, FNode<A> node) {
		@SuppressWarnings("unchecked")
		Collector<FNode<A>, Object, Object> mytcoll = (Collector<FNode<A>, Object, Object>) collector;
		Object handler = values.computeIfAbsent(strkey, (a) -> mytcoll.supplier().get());
		coll.put(strkey, collector);
		BiConsumer<Object, FNode<A>> accum = mytcoll.accumulator();
		accum.accept(handler, node);
	}
	
	
	public void finish() {
		for (Entry<String, Collector<?, ?, ?>> ee : coll.entrySet()) {
			Collector<?, ?, ?> collector = ee.getValue();
			@SuppressWarnings("unchecked")
			Function<Object, Object> finisher = (Function<Object, Object>) collector.finisher();
			
			Object value = values.get(ee.getKey());
			Objects.requireNonNull(value, "Internal error. Value is null. Is hashcode implemented correctly?");
			
			Object newValue = finisher.apply(value);
			if (!values.containsKey(ee.getKey())) {
				throw new AssertionError();
			}
			values.put(ee.getKey(), newValue);
		}
	}
	
}
