package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;


public class FightRes<P, A> {

	private final Map<Object, FightRes<P, A>> index;
	private final IndexResults data;
	private final String label;
	
	@Deprecated
	public FightRes() {
		this("No label");
	}
	
	public FightRes(String label) {
		this.data = new IndexResults();
		this.index = new ConcurrentHashMap<>();
		this.label = label;
	}


	public void addFight(Fight<P, A> fight, FightIndexer<A> fightIndexer) {
		
		List<Collector<? extends Fight<?, A>, ?, ?>> collectors = fightIndexer.getCollectors();
		List<Indexer<A>> indexers = fightIndexer.getIndexers();
		List<String> keys = fightIndexer.getKeys();
		// TODO: Is it really useful to add data anywhere except on last FightRes? It's going to use a Collector to cascade upwards either way!
		
		List<FightRes<P, A>> whereToAddData = new ArrayList<>();
		whereToAddData.add(this);
		FightRes<P, A> nextDepth = this;
		for (int i = 0; i < indexers.size(); i++) {
			Indexer<A> indexer = indexers.get(i);
			Collector<? extends Fight<?, A>, ?, ?> collector = collectors.get(i);
			String key = keys.get(i);
			
			if (collector == null) {
				// Is index
				Object useIndex = indexer.apply(fight.getArena());
				nextDepth = nextDepth.index.computeIfAbsent(useIndex, (f) -> new FightRes<>(key));
				whereToAddData.add(nextDepth);
			}
			else {
				// Is data
//				Object value = indexer.apply(fight.getArena());
				for (FightRes<P, A> where : whereToAddData) {
					where.data.addData(key, fight, indexer, collector);
				}
			}
		}
		
	}
	
	@Override
	public String toString() {
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
		
		for (Entry<Object, FightRes<P, A>> ee : index.entrySet()) {
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
