package net.zomis.fight;

/**
 * Interface to provide implementation for determining the winner of a fight
 */
public interface FightInterface<PL> {
	/**
	 * The fight number for the first fight
	 */
	int	FIRST_FIGHT	= 1;

	/**
	 * Make two fighters fight each other
	 * 
	 * @param players The players that should fight each other
	 * @param fightNumber Which fight number in the series this is, starting at 1.
	 * @return The winner of the fight, or null if it is a draw
	 */
	PL determineWinner(PL[] players, int fightNumber);
}