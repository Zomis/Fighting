package net.zomis.fight.example;

import java.util.Random;

import net.zomis.fight.FightInterface;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;

import org.junit.Test;

public class SimpleFight {
	public static class Fighter {
		private final int random;
		private final int bonus;

		public Fighter(int fightRandom) {
			this(fightRandom, 0);
		}
		public Fighter(int fightRandom, int fightBonus) {
			this.random = fightRandom;
			this.bonus = fightBonus;
		}
		public int fightValue(Random rand) {
			return rand.nextInt(this.random) + this.bonus;
		}
		@Override
		public String toString() {
			return "(Fighter: " + random + "+" + bonus + ")";
		}
	}
	
	@Test
	public void main() {
		final Random random = new Random(42); // since this is a test we want the results to be stable
		
		GameFight<Fighter> fight = new GameFight<Fighter>();
		Fighter[] fighters = new Fighter[]{ 
			new Fighter(1), new Fighter(2), new Fighter(3, 2), new Fighter(4), 
			new Fighter(5), new Fighter(6), new Fighter(7), new Fighter(8), new Fighter(9),  
		};
		FightResults<Fighter> results = fight.fightEvenly(fighters, 10000, new FightInterface<Fighter>() {
			@Override
			public Fighter determineWinner(Fighter[] players, int fightNumber) {
				int a = players[0].fightValue(random);
				int b = players[1].fightValue(random);
				
				return a > b ? players[0] : players[1];
			}
		});
		System.out.println(results.toStringMultiLine());
		System.out.println(results.getPercentagesDesc());
	}
	

}
