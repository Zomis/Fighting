package net.zomis.fight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameFight<T> {
	
	private final boolean	separateIndexes;
	private String	label;
	
	/**
	 * @param label The name of this fight, which will be showed in the output
	 * @param useSeparateIndexes True to alternate fighters between index 0 and index 1.
	 */
	public GameFight(String label, boolean useSeparateIndexes) {
		this.separateIndexes = useSeparateIndexes;
		this.label = label;
	}
	public GameFight(String label) {
		this(label, false);
	}
	public GameFight() {
		this(null);
	}
	
	/**
	 * Creates 1 vs. 1 games for each possible combination of fighters and fights them against each other.
	 * 
	 * @param fighters An array of the fighters
	 * @param gamesPerGroup How many fights each 1 vs. 1 pair should make.
	 * @param fightStrategy An interface providing implementation to determine the winner of a fight
	 * @return The results of the fighting
	 */
	public FightResults<T> fightEvenly(T[] fighters, int gamesPerGroup, FightInterface<T> fightStrategy) {
		FightResults<T> results = new FightResults<T>(label, separateIndexes);
		// TODO: Change PL[] to List instead, or even better: Use a custom type, with specific getFirstFighter() getSecondFighter() isFirstFight() getFightNumber()
		
		List<List<T>> groups = GuavaExt.processSubsets(Arrays.asList(fighters), 2);
		for (List<T> group : groups) {
			T[] currentFighters = Arrays.copyOf(fighters, 2);
			currentFighters[0] = group.get(0);
			currentFighters[1] = group.get(1);
			if (currentFighters[0] == currentFighters[1])
				throw new UnsupportedOperationException("Fighters cannot be equal at the moment");
			
			// Fight the games
			for (int i = 1; i <= gamesPerGroup; i++)
				results.saveResult(currentFighters, fightStrategy.determineWinner(currentFighters, i));
			
			if (separateIndexes) {
				currentFighters[0] = group.get(1);
				currentFighters[1] = group.get(0);
				for (int i = 1; i <= gamesPerGroup; i++)
					results.saveResult(currentFighters, fightStrategy.determineWinner(currentFighters, i));
			}
		}
		results.finished();
		return results;
	}
	/**
	 * Perform a specific number of randomly selected 1 vs. 1 fights.
	 * 
	 * @param fighters Array of fighters that can be chosen to participate in fights
	 * @param count The number of fights to make in total
	 * @param fightStrategy An interface providing implementation to determine the winner of a fight
	 * @return The results of the fighting
	 */
	public FightResults<T> fightRandom(T[] fighters, int count, FightInterface<T> fightStrategy) {
		FightResults<T> results = new FightResults<T>(label, separateIndexes);
		Random random = new Random();
		for (int i = 1; i <= count; i++) {
			T[] currentFighters = Arrays.copyOf(fighters, 2);
			List<T> playerOptions = new ArrayList<T>(Arrays.asList(fighters));
			currentFighters[0] = playerOptions.remove(random.nextInt(playerOptions.size()));
			currentFighters[1] = playerOptions.remove(random.nextInt(playerOptions.size()));
			if (currentFighters[0] == currentFighters[1])
				throw new IllegalStateException("Fighters cannot be equal at the moment");
			
			results.saveResult(currentFighters, fightStrategy.determineWinner(currentFighters, i));
		}
		results.finished();
		return results;
	}
}