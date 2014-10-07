package net.zomis.fight.ext;


/**
 * 
 * 
 * @param <P> Fighter
 * @param <A> Arena they are fighting at
 */
public class Fight<P, A> {

	private final ArenaParams<P> arenaParams;
	private final A arena;

	public Fight(ArenaParams<P> arenaParams, A arena) {
		this.arenaParams = arenaParams;
		this.arena = arena;
	}
	
	public A getArena() {
		return arena;
	}
	
	public ArenaParams<P> getArenaParams() {
		return arenaParams;
	}
	
	@Override
	public String toString() {
		return arenaParams + " in " + arena;
	}

}
