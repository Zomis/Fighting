package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.zomis.fight.GuavaExt;

public class GameFightNew<P, A> {

	public Stream<Fight<P, A>> createEvenFights(List<P> players, int numFights, Function<ArenaParams<P>, A> arenaCreator) {
		if (players.size() != 2)
			throw new UnsupportedOperationException();
		
		return Stream.generate(fightGenerator(players, arenaCreator)).limit(numFights);
	}

	private Supplier<Fight<P, A>> fightGenerator(List<P> players, Function<ArenaParams<P>, A> arenaCreator) {
		ArenaParams<P> arenaParams = new ArenaParams<P>(players);
		return () -> new Fight<P, A>(arenaParams, arenaCreator.apply(arenaParams));
	}
	
	
	public Stream<Fight<P, A>> createEvenFightStream(List<P> ais, int numFights, Function<ArenaParams<P>, A> arenaCreator) {
		Stream<ArenaParams<P>> arenas = createArenaStream(ais);
		Stream<Fight<P, A>> fightStream = arenas.flatMap(a -> createEvenFights(a.getPlayers(), numFights, arenaCreator));
		return fightStream;
	}
	
	private Stream<ArenaParams<P>> createArenaStream(List<P> ais) {
		List<List<P>> subsets = GuavaExt.processSubsets(ais, 2);
		Stream<ArenaParams<P>> arenas = subsets.stream().map(list -> new ArenaParams<>(list));
		return arenas;
	}
	
	public <T> FightRes<T> processStream(String name, Stream<T> fightStream, FightIndexer<T> indexer, Consumer<T> process) {
		FightRes<T> results = new FightRes<>(name);
		fightStream.forEach(fight -> {
			process.accept(fight);
			results.addFight(fight, indexer);
		});
		results.finish();
		return results;
	}
	
	public Stream<Fight<P, A>> produceRandomStream(List<P> fighters, Function<ArenaParams<P>, A> arenaCreator) {
		return Stream.generate(() -> fightGenerateRandom(new Random(), fighters, arenaCreator));
	}
	
	private Fight<P, A> fightGenerateRandom(Random random, List<P> fighters, Function<ArenaParams<P>, A> arenaCreator) {
		List<P> currentFighters = new ArrayList<>();
		List<P> playerOptions = new ArrayList<P>(fighters);
		currentFighters.add(playerOptions.remove(random.nextInt(playerOptions.size())));
		currentFighters.add(playerOptions.remove(random.nextInt(playerOptions.size())));
		
		return fightGenerator(currentFighters, arenaCreator).get();
	}

	public FightRes<Fight<P, A>> fightRandom(String name, List<P> fighters, 
			int count, FightIndexer<Fight<P, A>> indexer, 
			Function<ArenaParams<P>, A> arenaCreator,
			Consumer<Fight<P, A>> process) {
		FightRes<Fight<P, A>> results = new FightRes<Fight<P, A>>(name);
		Random random = new Random();
		
		for (int i = 1; i <= count; i++) {
			List<P> currentFighters = new ArrayList<>();
			List<P> playerOptions = new ArrayList<P>(fighters);
			currentFighters.add(playerOptions.remove(random.nextInt(playerOptions.size())));
			currentFighters.add(playerOptions.remove(random.nextInt(playerOptions.size())));
			
			Stream<Fight<P, A>> fights = createEvenFights(currentFighters, 1, arenaCreator);
			fights.forEach(fight -> {
				process.accept(fight);
				results.addFight(fight, indexer);
			});
		}
		results.finish();
		return results;
	}
	
	
}
