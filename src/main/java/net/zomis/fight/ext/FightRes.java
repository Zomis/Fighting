package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collector;


public class FightRes<T> {
	// TODO: FightRes to JTree conversion?

	private final Map<Object, FightRes<T>> index;
	private final IndexResults data;
	private final String label;
	
	public FightRes(String label) {
		this.data = new IndexResults();
		this.index = new ConcurrentHashMap<>();
		this.label = label;
	}

	public boolean hasChilds() {
		return !index.isEmpty();
	}
	
	public IndexResults getData() {
		return data;
	}
	
	
	

	public void addFight(T fight, FightIndexer<T> fightIndexer) {
		
		Map<String, FightResHandler<T>> handlers = fightIndexer.getHandlers();
		// It **is** actually useful to add data anywhere, even if it is not on last FightRes.
		
		Map<FNode<T>, FightRes<T>> whereToAddData = new HashMap<>();
		List<FNode<T>> nextDepths = new ArrayList<>();
		
		FNode<T> rootNode = new FNode<>(Collections.emptyList(), fight);
		whereToAddData.put(rootNode, this);
		nextDepths.add(rootNode);
		
		for (Entry<String, FightResHandler<T>> ee : handlers.entrySet()) {
			String key = ee.getKey();
			FightResHandler<T> handler = ee.getValue();
			
			Function<Object, FightRes<T>> suppl = f -> new FightRes<>(key + f);
			
			if (handler.isIndexer()) {
				
				Function<FNode<T>, Object> nodex = handler.getIndexer();
				List<FNode<T>> oldDepths = new ArrayList<>(nextDepths);
				oldDepths.stream().forEach(previousDepth -> {
					nextDepths.remove(previousDepth);
					
					Object useIndex = nodex.apply(previousDepth);
					if (!(useIndex instanceof Object[])) {
						useIndex = new Object[]{ useIndex };
					}
					
					Object[] objArr = (Object[]) useIndex;
					for (Object obj : objArr) {
						FightRes<T> newD = whereToAddData.get(previousDepth).index.computeIfAbsent(obj, useIndexValue -> suppl.apply(useIndexValue));
						List<Object> idx = previousDepth.getChosenIndexes();
						idx.add(obj);
						FNode<T> newNode = new FNode<>(idx, fight);
						nextDepths.add(newNode);
						whereToAddData.put(newNode, newD);
					}
				});
			}
			if (handler.isCollector()) {
				Collector<FNode<T>, ?, ?> coll = handler.getCollector();
				
				for (Entry<FNode<T>, FightRes<T>> where : whereToAddData.entrySet()) {
					where.getValue().data.addAdvancedData(key, coll, where.getKey());
				}
			}
			if (!handler.isCollector() && !handler.isIndexer())
				throw new AssertionError("Key " + key + " is nuts");
		}
		
	}
	
	@Override
	public String toString() {
		return "FightRes:" + this.label;
	}
	
	public String toStringBig() {
		return label + toString(0);
	}
	
	public String toString(int indentation) {
		String indent = indentStr(indentation);
		StringBuilder str = new StringBuilder();
		str.append("\n");
		str.append(indent);
		str.append("    Data: ");
		str.append(data);
		str.append("\n");
		
		for (Entry<Object, FightRes<T>> ee : index.entrySet()) {
			indent = indentStr(indentation + 4);
			str.append(indent);
			str.append(ee.getValue().label);
			str.append(": ");
			str.append(ee.getValue().toString(indentation + 4));
		}
		
		
		return str.toString();
	}
	
	

	private String indentStr(int indentation) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < indentation; i++)
			str.append(' ');
		return str.toString();
	}

	public void finish() {
		data.finish();
		index.values().forEach(fightRes -> fightRes.finish());
	}

}
