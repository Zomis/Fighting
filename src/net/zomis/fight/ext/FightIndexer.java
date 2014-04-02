package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class FightIndexer<P, A> {

	private final List<Indexer<Fight<P, A>>> indexes;
	private final List<Collector<? extends Fight<P, A>, ?, ?>> collectors;
	private final List<String> keys;
	
	public FightIndexer() {
		this.indexes = new ArrayList<>();
		this.collectors = new ArrayList<>();
		this.keys = new ArrayList<>();
	}
	// TODO: Factory / Builder pattern, use unmodifiable collections
	public List<Indexer<Fight<P, A>>> getIndexers() {
		return indexes;
	}
	public List<Collector<? extends Fight<P, A>, ?, ?>> getCollectors() {
		return collectors;
	}
	public List<String> getKeys() {
		return keys;
	}
	
	public FightIndexer<P, A> addIndex(String key, Indexer<Fight<P, A>> index) {
		this.indexes.add(index);
		this.collectors.add(null);
		this.keys.add(key);
		return this;
	}
	
//	Collector<T, A, R> result;
	public <R, B, C> FightIndexer<P, A> addData(String key, Collector<? extends Fight<P, A>, ?, ?> collector) {
//		Collectors.averagingInt(arg0)
//		Collectors.maxBy(arg0)
//		Collectors.counting()
//		Collectors.summarizingInt(arg0)
		this.indexes.add(null);
		this.collectors.add(collector);
		this.keys.add(key);
		return this;
	}
	// TODO: Use FightInformation interface? for firstPlayer, secondPlayer, isFirstFight, getFightIterationNumber...
	// x Add ExtraInformation data, such as "average length of fight"
	
	// x To save the data, create one object per fight?
	// x How these advanced features could be used, see below:
	/* 
	 * Traditional: Use player as a key and find number of wins and losses
	 * 
	 * CWars2 - count cards used in fights, see if there is a pattern between cards used and match result
	 * CWars2 - average length of match
	 * 
	 * MFE - use starting move as an index to find best starting moves, no matter which AI did the move
	 * MFE - find the average number of 100% mines in 100 games of A vs. B
	 * 
	 * TTTUltimate - Find out the best starting moves
	 * TTTUltimate - Average length of match
	 * FilterInterface<FightInfo/FightExtras> ?
	 * */
	
	// TODO: Use MFE Replay database as a stream (easy to fix with .map?)
	// TODO: Use raw history strings as input stream (easy to fix with .map?)
	// fightResults.indexBy(player).indexBy(otherPlayer).indexBy(firstMove).extraCount(wasBombUsed)
	
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
