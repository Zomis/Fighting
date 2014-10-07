package net.zomis.fight.annot;

import java.util.IntSummaryStatistics;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import net.zomis.fight.ext.Fight;
import net.zomis.fight.ext.Indexer;

public class ExampleFight {
	
	private class RemovedEvent {
		
	}
	
	private class ExampleMove {
		
//		indexer.addIndex("Player", FightCollectors.player1());
//		indexer.addIndexPlus("Opponent", FightCollectors.player2(0));
//		indexer.addIndex("Middle won by", (f) -> f.getArena().getGame().getSub(1, 1).getWonBy());
//		indexer.addData("SamePlayer", FightCollectors.countingPredicate(middleWinnerWonGame));
//		indexer.addDataAdvanced("Stats", winStatsCollector);
//		indexer.addData("Moves", maxMoves);
	}
	
	@Data(level = 3, value = "MoveData")
	private Collector<ExampleMove, ?, IntSummaryStatistics> someInterestingData = Collectors.summarizingInt(move -> 1);
	
	@Data(level = 3, value = "Removed Entities")
	private Collector<RemovedEvent, ?, IntSummaryStatistics> dataAboutSpecificEvent = Collectors.summarizingInt(move -> 1);
	
	@Index(level = 0, value = "Player")
	private Function<ExampleMove, String> fds;
	
	@Index(level = 1, value = "Opponent")
	private Indexer<Fight<ExampleFight, String>> fdsdsa;
	

}
