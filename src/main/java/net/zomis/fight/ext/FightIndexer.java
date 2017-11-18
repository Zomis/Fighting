package net.zomis.fight.ext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FightIndexer<T> {

	private final Map<String, FightResHandler<T>> handlers;
	
	public FightIndexer() {
		this.handlers = new LinkedHashMap<>();
	}
	
	public Map<String, FightResHandler<T>> getHandlers() {
		return handlers;
	}
	
	// TODO: Factory / Builder pattern, use unmodifiable collections
	
	public FightIndexer<T> addIndex(final String key, final Indexer<T> index) {
		checkKey(key);
		this.handlers.put(key, new FightResHandler<>(f -> index.apply(f.getF()), null));
		return this;
	}
	
	private void checkKey(String key) {
		if (handlers.containsKey(key))
			throw new IllegalArgumentException("Key has already been added: " + key);
	}

	public FightIndexer<T> addData(String key, Collector<T, ?, ?> collector) {
		checkKey(key);
		Collector<FNode<T>, ?, ?> nodelector = Collectors.mapping(f -> f.getF(), collector);
		this.handlers.put(key, new FightResHandler<>(null, nodelector));
		return this;
	}
	
	public void addDataAdvanced(String key, Collector<FNode<T>, ?, ?> collector) {
		checkKey(key);
		this.handlers.put(key, new FightResHandler<>(null, collector));
	}
	
	public void addIndexPlus(String key, Indexer<FNode<T>> otherAI) {
		checkKey(key);
		this.handlers.put(key, new FightResHandler<>(otherAI, null));
	}
	
	
	
	// x To save the data, create one object per fight?
	// x How these advanced features could be used, see below:
	/* 
	 * Traditional: Use player as a key and find number of wins and losses
	 * 
	 * CWars2 - count cards used in fights, see if there is a pattern between cards used and match result
	 * CWars2 - average length of match
	 * 
	 * MFE - use starting move as an index to find best starting moves, no matter which AI did the move
	 * MFE - find the average number of 100% mines in 100 games of A vs. B (Make analysis before/after each click and check how many new appeared)
	 * 
	 * TTTUltimate - Find out the best starting moves
	 * TTTUltimate - Average length of match
	 * FilterInterface<FightInfo/FightExtras> ?
	 * */
	
	// TODO: Use MFE Replay database as a stream (easy to fix with .map?)
	// TODO: Use raw history strings as input stream (easy to fix with .map?)
	// Usage example: fightResults.indexBy(player).indexBy(otherPlayer).indexBy(firstMove).extraCount(wasBombUsed)
	
//	MFE - scan through replays to find out interesting information
//	PL determineWinner(FightParams<PL> fightInfo);
/*
accum
.indexPlayer()
	.indexOpponent()
		.index(isWinMiddle)
			.addInt(haveBottomRight)
TODO: PARALLEL STREAM! Java 8!

Example output:

IndexMap results: Map<AI, IndexMap>
	IndexMap opponents: Map<AI, IndexMap>
		IndexMap isWinMiddle: Map<Boolean, Results>
Results: x wins, x draws, x losses. Map<String, Extra>
Extra: 

<Header>
<ToString>
#AI_A (6 wins, 1 draws, 23 losses (21,67 %)) (7 haveBottomRight)
	vs. #AI_B: 3 wins, 1 draws, 6 losses (35,00 %) (7 haveBottomRight)
		isWinMiddle: false - 3 wins, 1 draws, 6 losses (42,00 %) (3 haveBottomRight)
		isWinMiddle: true - 3 wins, 1 draws, 6 losses (42,00 %) (4 haveBottomRight)
	vs. #AI_C: 3 wins, 1 draws, 6 losses (35,00 %) (7 haveBottomRight)
		isWinMiddle: false - 3 wins, 1 draws, 6 losses (42,00 %) (3 haveBottomRight)
		isWinMiddle: true - 3 wins, 1 draws, 6 losses (42,00 %) (4 haveBottomRight)
		
*/	
}
