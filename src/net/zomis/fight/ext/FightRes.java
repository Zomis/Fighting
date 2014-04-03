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
		List<Collector<FNode<T>, ?, ?>> nodelectors = fightIndexer.getNodelectors();
		List<Indexer<T>> indexers = fightIndexer.getIndexers();
		List<Indexer<FNode<T>>> nodexers = fightIndexer.getNodexes();
		List<String> keys = fightIndexer.getKeys();
		// It **is** actually useful to add data anywhere, even if it is not on last FightRes.
		
		Map<FNode<T>, FightRes<T>> whereToAddData = new HashMap<>();
		List<FNode<T>> nextDepths = new ArrayList<>();
		
		FNode<T> rootNode = new FNode<>(Collections.emptyList(), fight);
		whereToAddData.put(rootNode, this);
		nextDepths.add(rootNode);
		
		for (int i = 0; i < indexers.size(); i++) {
			// Is it an index? Create a new FNode
			// Is it 
			
			
			Indexer<T> indexer = indexers.get(i);
			Collector<T, ?, ?> collector = collectors.get(i);
			String key = keys.get(i);
			Collector<FNode<T>, ?, ?> nodelector = nodelectors.get(i);
			Indexer<FNode<T>> nodex = nodexers.get(i);
			
			Function<Object, FightRes<T>> suppl = f -> new FightRes<>(key + f);
			
			if (nodex != null) {
				
				List<FNode<T>> oldDepths = new ArrayList<>(nextDepths);
				oldDepths.stream().forEach((previousDepth) -> {
					nextDepths.remove(previousDepth);
					Object useIndex = nodex.apply(previousDepth);
					FightRes<T> newD = whereToAddData.get(previousDepth).index.computeIfAbsent(useIndex, useIndexValue -> suppl.apply(useIndexValue));
					List<Object> idx = previousDepth.getChosenIndexes();
					idx.add(useIndex);
					FNode<T> newNode = new FNode<>(idx, fight);
					nextDepths.add(newNode);
					whereToAddData.put(newNode, newD);
				});
				
				
//				Object useIndex = nodex.apply(all fnodes in nextDepths);
//				add "branch" method
			}
			else if (nodelector != null) {
				for (Entry<FNode<T>, FightRes<T>> where : whereToAddData.entrySet()) {
					where.getValue().data.addAdvancedData(key, fight, nodelector, where.getKey());
				}
			}
			else if (indexer != null) {
				// Is index
				Object useIndex = indexer.apply(fight);
				
				if (useIndex instanceof Object[]) {
					Object[] objArr = (Object[]) useIndex;
					
					List<FNode<T>> oldDepths = new ArrayList<>(nextDepths);
					oldDepths.stream().forEach((previousDepth) -> {
						nextDepths.remove(previousDepth);
						
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
				else {
					// Only one index was returned
					List<FNode<T>> oldDepths = new ArrayList<>(nextDepths);
					oldDepths.stream().forEach((previousDepth) -> {
						nextDepths.remove(previousDepth);
						FightRes<T> newD = whereToAddData.get(previousDepth).index.computeIfAbsent(useIndex, useIndexValue -> suppl.apply(useIndexValue));
						List<Object> idx = previousDepth.getChosenIndexes();
						idx.add(useIndex);
						FNode<T> newNode = new FNode<>(idx, fight);
						nextDepths.add(newNode);
						whereToAddData.put(newNode, newD);
					});
				}
				
//				nextDepth = nextDepth.index.computeIfAbsent(useIndex, (f) -> new FightRes<>(key));
//				whereToAddData.add(nextDepth);
			}
			else if (collector != null) {
//				System.out.println("" + whereToAddData);
				// Is data
//				Object value = indexer.apply(fight.getArena());
				for (Entry<FNode<T>, FightRes<T>> where : whereToAddData.entrySet()) {
					where.getValue().data.addData(key, fight, collector);
				}
			}
			else throw new AssertionError("Key " + key + " is nuts");
		}
		
	}
	
	@Override
	public String toString() {
		return "FightRes:" + this.label;
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
		index.values().forEach(fightRes -> fightRes.finish());
	}

}
