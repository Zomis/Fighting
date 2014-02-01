package net.zomis.fight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;

public class FightResults<PL> {
	private final Map<PL, PlayerResults<PL>> playerData = new HashMap<PL, PlayerResults<PL>>();
	private final boolean	separateIndexes;
	private final String label;
	
	public FightResults(String label, boolean separateIndexes) {
		this.label = label;
		this.separateIndexes = separateIndexes;
	}
	
	public List<PlayerResults<PL>> getResults() {
		List<PlayerResults<PL>> list = new ArrayList<PlayerResults<PL>>(playerData.values());
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

	private static class ProduceValue<PL> implements Function<PL, PlayerResults<PL>> {
		@Override
		public PlayerResults<PL> apply(PL arg0) {
			return new PlayerResults<PL>(arg0);
		}
	}
	
	public void saveResult(PL[] fighters, PL winner) {
		final int DEFAULT_INDEX = 0;
		ProduceValue<PL> prodValue = new ProduceValue<PL>();
		for (int i = 0; i < fighters.length; i++) {
			PL pp1 = fighters[i];
			PlayerResults<PL> result = GuavaExt.mapGetOrPut(playerData, pp1, prodValue);
			
			for (int j = 0; j < fighters.length; j++) {
				if (i == j)
					continue;
				
//				PlayerResults<PL> result2 = GuavaExt.mapGetOrPut(playerData, fighters[j], prodValue);
				result.addResult(separateIndexes ? i : DEFAULT_INDEX , fighters[j], winner);
//				result2.addResult(separateIndexes ? j : DEFAULT_INDEX, pp1, winner);
			}
		}
	}
	
	@Override
	public String toString() {
		return playerData.toString();
	}
	
	public String toStringMultiLine() {
		StringBuilder str = new StringBuilder();
		if (this.label != null) {
			str.append(this.label);
			str.append("\n");
		}
		str.append(super.toString());
		str.append("\n");
		for (Entry<PL, PlayerResults<PL>> ee : this.playerData.entrySet()) {
			str.append(ee.getKey());
			str.append("\n");
			str.append(ee.getValue().toStringMultiLine());
			str.append("\n");
		}
		
		return str.toString();
	}

}
