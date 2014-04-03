package net.zomis.fight.ext;

import java.util.function.Function;
import java.util.stream.Collector;

public class FightResHandler<T> {

	private final Function<FNode<T>, Object> indexer;
	private final Collector<FNode<T>, ?, ?> collector;

	public FightResHandler(Function<FNode<T>, Object> indexer, Collector<FNode<T>, ?, ?> collector) {
		this.indexer = indexer;
		this.collector = collector;
	}
	
	public Collector<FNode<T>, ?, ?> getCollector() {
		return collector;
	}
	
	public Function<FNode<T>, Object> getIndexer() {
		return indexer;
	}
	
	public boolean isCollector() {
		return collector != null;
	}
	
	public boolean isIndexer() {
		return indexer != null;
	}
	
}
