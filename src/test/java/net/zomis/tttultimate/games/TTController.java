package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTMoveListener;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;

public abstract class TTController {
	
	protected final TTBase game;
	protected TTPlayer currentPlayer = TTPlayer.X;
	private TTMoveListener	moveListener;
	private StringBuilder history;
	private int moveCount;

	public TTController(TTBase board) {
		this.game = board;
		this.history = new StringBuilder();
	}

	public abstract boolean isAllowedPlay(TTBase tile);
	
	public final boolean play(TTBase tile) {
		if (tile == null)
			throw new IllegalArgumentException("Tile to play at cannot be null.");
		
		if (!isAllowedPlay(tile)) {
			System.out.println("Warning: Move was not made. Unable to play at " + tile);
			return false;
		}
		TTBase playedTile = tile;
		if (!this.performPlay(tile))
			return false;

		this.addToHistory(tile);
		
		if (this.moveListener != null)
			this.moveListener.onMove(playedTile);
		
		return true;
	}
	
	private void addToHistory(TTBase tile) {
		this.moveCount++;
		if (history.length() > 0)
			history.append(",");
		history.append(Integer.toString(tile.getGlobalX(), Character.MAX_RADIX));
		history.append(Integer.toString(tile.getGlobalY(), Character.MAX_RADIX));
	}

	protected abstract boolean performPlay(TTBase tile);
	
	public boolean play(int x, int y) {
		return this.play(game.getSmallestTile(x, y));
	}
	
	public TTPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	protected void nextPlayer() {
		currentPlayer = currentPlayer.next();
	}
	
	public TTBase getGame() {
		return game;
	}

	public boolean isGameOver() {
		return game.isWon();
	}

	public TTPlayer getWonBy() {
		return game.getWonBy();
	}

	public void setOnMoveListener(TTMoveListener moveListener) {
		this.moveListener = moveListener;
	}
	
	
	public void makeMoves(String history) throws IllegalStateException, IllegalArgumentException {
		for (String move : history.split(",")) {
			if (move.isEmpty())
				continue;
			if (move.length() != 2)
				throw new IllegalArgumentException("Unexcepted move length. " + move);
			
			int x = Integer.parseInt(String.valueOf(move.charAt(0)), Character.MAX_RADIX);
			int y = Integer.parseInt(String.valueOf(move.charAt(1)), Character.MAX_RADIX);
			
			TTBase tile = game.getSmallestTile(x, y);
			if (!this.play(tile))
				throw new IllegalStateException("Unable to make a move at " + x + ", " + y + ": " + tile);
		}
	}
	public String saveHistory() {
		return this.history.toString();
	}
	
	public void reset() {
		this.currentPlayer = TTPlayer.X;
		this.history = new StringBuilder();
		this.moveCount = 0;
		this.game.reset();
		this.onReset();
	}

	protected abstract void onReset();
	
	public String getViewFor(TTBase tile) {
		return tile.isWon() ? tile.getWonBy().toString() : "";
	}
	
	public int getMoveCount() {
		return moveCount;
	}
}
