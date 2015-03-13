package net.zomis.tttultimate;

import java.util.Collections;
import java.util.List;


public class TTBase implements Winnable, HasSub<TTBase> {
	// Container
	private final TTBase[][] subs;
	private final TTMNKParameters mnkParams;
	private final List<TTWinCondition> winConditions;
	
	// Winnable
	private final TTBase parent;
	private final int x;
	private final int y;
	private TTPlayer playedBy = TTPlayer.NONE;
	
	public TTBase(TTBase parent, TTMNKParameters parameters, TicFactory factory) {
		this(parent, 0, 0, parameters, factory);
	}
	public TTBase(TTBase parent, int x, int y, TTMNKParameters parameters, TicFactory factory) {
		this.parent = parent;
		this.mnkParams = parameters;
		this.x = x;
		this.y = y;
		
		this.subs = new TTBase[parameters.getWidth()][parameters.getHeight()];
		for (int xx = 0; xx < parameters.getWidth(); xx++) {
			for (int yy = 0; yy < parameters.getHeight(); yy++) {
				this.subs[xx][yy] = factory.construct(this, xx, yy);
			}
		}
		this.winConditions = Collections.unmodifiableList(TicUtils.setupWins(this));
	}
	
	public void determineWinner() {
		TTPlayer winner = TTPlayer.NONE;
		for (TTWinCondition cond : this.winConditions) {
			winner = winner.or(cond.determineWinnerNew());
		}
		this.playedBy = winner;
	}
	
	@Override
	public TTBase getSub(int x, int y) {
		if (!hasSubs() && x == 0 && y == 0)
			return this;
		if (x < 0 || y < 0)
			return null;
		if (x >= getSizeX() || y >= getSizeY())
			return null;
		return subs[x][y];
	}

	@Override
	public List<TTWinCondition> getWinConds() {
		return winConditions;
	}

	@Override
	public TTPlayer getWonBy() {
		return this.playedBy;
	}

	@Override
	public int getSizeX() {
		return this.mnkParams.getWidth();
	}

	@Override
	public int getSizeY() {
		return this.mnkParams.getHeight();
	}

	@Override
	public int getConsecutiveRequired() {
		return this.mnkParams.getConsecutiveRequired();
	}

	public TTMNKParameters getMNKParameters() {
		return this.mnkParams;
	}

	public TTBase getParent() {
		return parent;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isWon() {
		return playedBy != TTPlayer.NONE;
	}
	
	public void setPlayedBy(TTPlayer playedBy) {
		if (playedBy == null && this.hasSubs() && parent != null)
			new Exception().printStackTrace();
		this.playedBy = playedBy;
	}
	
	public boolean hasSubs() {
		return getSizeX() != 0 && getSizeY() != 0;
	}
	
	@Override
	public String toString() {
		return "{Pos " + x + ", " + y + "; Size " + getSizeX() + ", " + getSizeY() + "; Played by " + getWonBy() + ". Parent is " + parent + "}";
	}
	
	public void reset() {
		this.setPlayedBy(TTPlayer.NONE);
		for (int xx = 0; xx < getSizeX(); xx++) {
			for (int yy = 0; yy < getSizeY(); yy++) {
				this.getSub(xx, yy).reset();
			}
		}
	}
	public int getGlobalX() {
		if (parent == null)
			return 0;
		if (parent.getParent() == null)
			return x;
		return parent.getX() * parent.getParent().getSizeX() + this.x;
	}
	
	public int getGlobalY() {
		if (parent == null)
			return 0;
		if (parent.getParent() == null)
			return y;
		return parent.getY() * parent.getParent().getSizeY() + this.y;
	}
	
	public TTBase getSmallestTile(int x, int y) {
		int subX = x / getSizeX();
		int subY = y / getSizeY();
		TTBase board = getSub(subX, subY);
		if (board == null)
			throw new NullPointerException("No such smallest tile found: " + x + ", " + y);
		
		return board.getSub(x - subX*getSizeX(), y - subY*getSizeY());
	}
	
}
