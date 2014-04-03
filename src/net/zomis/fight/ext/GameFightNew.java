package net.zomis.fight.ext;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.zomis.fight.GuavaExt;

public class GameFightNew<P, A> {

	private final String name;
	private final Function<ArenaParams<P>, A> arenaCreator;

	@Deprecated
	public GameFightNew(String name) {
		this.name = name;
		this.arenaCreator = null;
	}
	public GameFightNew(String name, Function<ArenaParams<P>, A> arenaCreator) {
		this.name = name;
		this.arenaCreator = arenaCreator;
	}

	// TODO: Use Stream.flatMap to transform a stream of the 1-vs-1 player combinations into a stream of game fights?
	
	public Stream<Fight<P, A>> createEvenFights(List<P> players, int numFights) {
		if (players.size() != 2)
			throw new UnsupportedOperationException();
		
		return Stream.generate(generator(players)).limit(numFights);
	}

	private Supplier<Fight<P, A>> generator(List<P> players) {
		ArenaParams<P> arenaParams = new ArenaParams<P>(players);
		return () -> new Fight<P, A>(arenaParams, arenaCreator.apply(arenaParams));
	}
	
	
	public Stream<Fight<P, A>> createEvenFightStream(List<P> ais, int numFights) {
		Stream<ArenaParams<P>> arenas = createArenaStream(ais);
		Stream<Fight<P, A>> fightStream = arenas.flatMap(a -> createEvenFights(a.getPlayers(), numFights));
		return fightStream;
	}
	
	private Stream<ArenaParams<P>> createArenaStream(List<P> ais) {
		List<List<P>> subsets = GuavaExt.processSubsets(ais, 2);
		Stream<ArenaParams<P>> arenas = subsets.stream().map(list -> new ArenaParams<>(list));
		return arenas;
	}
	
	public FightRes<Fight<P, A>> processStream(Stream<Fight<P, A>> fightStream, FightIndexer<Fight<P, A>> indexer, Consumer<Fight<P, A>> process) {
		FightRes<Fight<P, A>> results = new FightRes<>(name);
		fightStream.forEach(fight -> {
			process.accept(fight);
			results.addFight(fight, indexer);
		});
		results.finish();
		return results;
	}
	
	
	
}
