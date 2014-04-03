package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collector;


public class FightRes<T> {

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
		
		List<Collector<T, ?, ?>> collectors = fightIndexer.getCollectors();
		List<Indexer<T>> indexers = fightIndexer.getIndexers();
		List<String> keys = fightIndexer.getKeys();
		// It **is** actually useful to add data anywhere, even if it is not on last FightRes.
		
		List<FightRes<T>> whereToAddData = new ArrayList<>();
		List<FightRes<T>> nextDepths = new ArrayList<>();
		whereToAddData.add(this);
		nextDepths.add(this);
		
//		FightRes<P, A> nextDepth = this;
		for (int i = 0; i < indexers.size(); i++) {
			Indexer<T> indexer = indexers.get(i);
			Collector<T, ?, ?> collector = collectors.get(i);
			String key = keys.get(i);
			
			if (collector == null) {
				// Is index
				Object useIndex = indexer.apply(fight);
				Function<Object, FightRes<T>> suppl = f -> new FightRes<>(key + f);
				
				if (useIndex instanceof Object[]) {
					Object[] objArr = (Object[]) useIndex;
//					System.out.println("We have an array! " + Arrays.toString(objArr));
					
					List<FightRes<T>> oldDepths = new ArrayList<>(nextDepths);
					oldDepths.stream().forEach((previousDepth) -> {
						nextDepths.remove(previousDepth);
						
						for (Object obj : objArr) {
							FightRes<T> newD = previousDepth.index.computeIfAbsent(obj, useIndexValue -> suppl.apply(useIndexValue));
							nextDepths.add(newD);
							whereToAddData.add(newD);
						}
					});
				}
				else {
					List<FightRes<T>> oldDepths = new ArrayList<>(nextDepths);
					oldDepths.stream().forEach((previousDepth) -> {
						nextDepths.remove(previousDepth);
						FightRes<T> newD = previousDepth.index.computeIfAbsent(useIndex, useIndexValue -> suppl.apply(useIndexValue));
						nextDepths.add(newD);
						whereToAddData.add(newD);
					});
				}
				
//				nextDepth = nextDepth.index.computeIfAbsent(useIndex, (f) -> new FightRes<>(key));
//				whereToAddData.add(nextDepth);
			}
			else {
//				System.out.println("" + whereToAddData);
				// Is data
//				Object value = indexer.apply(fight.getArena());
				for (FightRes<T> where : whereToAddData) {
					where.data.addData(key, fight, collector);
				}
			}
		}
		
	}
	
	@Override
	public String toString() {
		return "FightRes:" + this.label;
//		return toString(0);
	}
	
	public String toStringBig() {
		return toString(0);
	}
	
	public String toString(int indentation) {
		String indent = indentStr(indentation);
		StringBuilder str = new StringBuilder();
		str.append(indent);
		if (indentation == 0)
			str.append(label);
		str.append(" Data: ");
		str.append(data);
		str.append("\n");
		if (!index.isEmpty()) {
//			str.append(indent);
//			str.append("Index Keys: " + index.keySet() + "\n");
		}
		
		for (Entry<Object, FightRes<T>> ee : index.entrySet()) {
			indent = indentStr(indentation + 4);
			str.append(indent);
			str.append(ee.getValue().label);
			str.append(": ");
			str.append(ee.getKey());
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
		index.values().forEach(f -> f.finish());
	}

}
