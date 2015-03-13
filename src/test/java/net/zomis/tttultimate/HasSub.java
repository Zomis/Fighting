package net.zomis.tttultimate;

/**
 * Interface for classes that can contain other objects, 'subs', in a rectangular way
 *
 * @param <T> Type of sub
 */
public interface HasSub<T> extends Winnable {
	T getSub(int x, int y);
	Iterable<TTWinCondition> getWinConds();
	int getSizeX();
	int getSizeY();
	int getConsecutiveRequired();
	boolean hasSubs();
}
