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
	
	@SuppressWarnings("unchecked")
	public <A, B, C> void addData(String strkey, Object param, Collector<A, B, C> collector) {
		Collector<Object, Object, Object> mytcoll = (Collector<Object, Object, Object>) collector;
		Object handler = values.computeIfAbsent(strkey, (a) -> mytcoll.supplier().get());
		coll.put(strkey, collector);
		BiConsumer<Object, Object> accum = mytcoll.accumulator();
//		Collectors.
		
		accum.accept(handler, param);
		
//		collector.finisher()
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
