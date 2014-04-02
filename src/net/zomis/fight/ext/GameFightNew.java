package net.zomis.fight.ext;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.zomis.fight.GuavaExt;

public class GameFightNew<T, A> {

	private final String name;
	private final Function<ArenaParams<T>, A> arenaCreator;

	@Deprecated
	public GameFightNew(String name) {
		this.name = name;
		this.arenaCreator = null;
	}
	public GameFightNew(String name, Function<ArenaParams<T>, A> arenaCreator) {
		this.name = name;
		this.arenaCreator = arenaCreator;
	}

	// TODO: Use Stream.flatMap to transform a stream of the 1-vs-1 player combinations into a stream of game fights?
	
	public Stream<Fight<T, A>> createEvenFights(List<T> players, int numFights) {
		if (players.size() != 2)
			throw new UnsupportedOperationException();
		
		return Stream.generate(generator(players)).limit(numFights);
	}

	private Supplier<Fight<T, A>> generator(List<T> players) {
		ArenaParams<T> arenaParams = new ArenaParams<T>(players);
		return () -> new Fight<T, A>(arenaParams, arenaCreator.apply(arenaParams));
	}
	
	
	public Stream<Fight<T, A>> createEvenFightStream(List<T> ais, int numFights) {
		Stream<ArenaParams<T>> arenas = createArenaStream(ais);
//		FightStream fights = f;
		Stream<Fight<T, A>> fightStream = arenas.flatMap(a -> createEvenFights(a.getPlayers(), numFights));
//		Stream.concat(arg0, arg1)
		return fightStream;
	}
	
	private Stream<ArenaParams<T>> createArenaStream(List<T> ais) {
		List<List<T>> subsets = GuavaExt.processSubsets(ais, 2);
		Stream<ArenaParams<T>> arenas = subsets.stream().map(list -> new ArenaParams<>(list));
		return arenas;
	}
	
	public FightRes<T, A> processStream(Stream<Fight<T, A>> fightStream, FightIndexer<A> indexer, Consumer<Fight<T, A>> process) {
		FightRes<T, A> results = new FightRes<>(name);
		fightStream.forEach(fight -> {
			process.accept(fight);
			results.addFight(fight, indexer);
		});
		results.finish();
		return results;
	}
	
	
	
}
