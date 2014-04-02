package net.zomis.fight.ext;

import java.util.ArrayList;
import java.util.List;

public class ArenaParams<T> {

	private final List<T> players;

	public ArenaParams(List<T> players) {
		if (players.size() != 2)
			throw new UnsupportedOperationException();
		this.players = new ArrayList<>(players);
	}
	
	public T getFirstPlayer() {
		return players.get(0);
	}
	
	public T getSecondPlayer() {
		return players.get(1);
	}
	
	public List<T> getPlayers() {
		return new ArrayList<>(players);
	}
	
	@Override
	public String toString() {
		return getFirstPlayer() + " vs. " + getSecondPlayer();
	}
	
}
