package net.zomis.tttultimate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TTWinCondition implements Iterable<Winnable> {

	private final List<Winnable> winnables;
	private final int consecutive;

	public TTWinCondition(Winnable... winnables) {
		this(Arrays.asList(winnables));
	}
	
	public TTWinCondition(List<? extends Winnable> winnables) {
		this(winnables, winnables.size());
	}
	
	public TTWinCondition(List<? extends Winnable> winnables, int consecutive) {
		if (winnables.isEmpty())
			throw new IllegalArgumentException("Can't have an empty win condition!");
		this.winnables = Collections.unmodifiableList(new ArrayList<Winnable>(winnables));
		this.consecutive = consecutive;
	}
	
	public int neededForWin(TTPlayer player) {
		return winnables.size() - hasCurrently(player);
	}
	public boolean isWinnable(TTPlayer byPlayer) {
		return hasCurrently(byPlayer.next()) == 0;
	}
	public int hasCurrently(TTPlayer player) {
		int i = 0;
		for (Winnable winnable : winnables) {
			if (winnable.getWonBy().and(player) == player)
				i++;
		}
		return i;
	}
	
	public TTPlayer determineWinnerNew() {
		TTPlayer winner = TTPlayer.NONE;
		
		int[] consecutivePlayers = new int[TTPlayer.values().length];
		for (Winnable winnable : winnables) {
			TTPlayer current = winnable.getWonBy();
			for (TTPlayer pl : TTPlayer.values()) {
				int i = pl.ordinal();
				if (pl.and(current) == pl) {
					consecutivePlayers[i]++;
				}
				else consecutivePlayers[i] = 0;
				
				if (consecutivePlayers[i] >= this.consecutive) {
					winner = winner.or(pl);
				}
			}
		}
		return winner;
	}
	
	public boolean hasWinnable(Winnable field) {
		return winnables.contains(field);
	}
	
	public int size() {
		return winnables.size();
	}

	@Override
	public Iterator<Winnable> iterator() {
		return new ArrayList<Winnable>(this.winnables).iterator();
	}
	
}
