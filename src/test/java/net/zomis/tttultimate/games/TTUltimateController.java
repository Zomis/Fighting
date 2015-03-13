package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TicUtils;

public class TTUltimateController extends TTController {
	// TODO: Try making it even more Ultimate by adding one more dimension, and use Map<TTBase, TTBase> activeBoards. Just for fun.
	private TTBase activeBoard = null;
	
	public TTUltimateController(TTBase board) {
		super(board);
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		TTBase area = tile.getParent();
		if (area == null)
			return false;
		TTBase game = tile.getParent().getParent();
		
		if (!tile.getWonBy().equals(TTPlayer.NONE))
			return false;
		if (area.getWonBy().isExactlyOnePlayer())
			return false;
		if (game.isWon())
			return false;
		
		return activeBoard == null || activeBoard == area || activeBoard.getWonBy() != TTPlayer.NONE;
	}

	@Override
	public boolean performPlay(TTBase tile) {
		tile.setPlayedBy(currentPlayer);
		activeBoard = TicUtils.getDestinationBoard(tile);
		nextPlayer();
		
		// Check for win condition on tile and if there is a win, cascade to it's parents
		do {
			tile.determineWinner();
			tile = tile.isWon() ? tile.getParent() : null;
		}
		while (tile != null);
		
		return true;
	}

	@Override
	protected void onReset() {
		this.activeBoard = null;
	}

}
