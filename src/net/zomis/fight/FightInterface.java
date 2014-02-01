package net.zomis.fight;

public interface FightInterface<PL> {
	int	FIRST_FIGHT	= 1;

	PL determineWinner(PL[] players, int fightNumber);
}