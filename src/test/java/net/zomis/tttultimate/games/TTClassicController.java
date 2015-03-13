package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;

public class TTClassicController extends TTController {

	public TTClassicController(TTBase board) {
		super(board);
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		return tile.getParent() != null && !tile.hasSubs() &&
				tile.getParent().getWonBy().equals(TTPlayer.NONE) &&
				tile.getWonBy() == TTPlayer.NONE;
	}

	@Override
	public boolean performPlay(TTBase tile) {
		tile.setPlayedBy(currentPlayer);
		tile.getParent().determineWinner();
		nextPlayer();
		return true;
	}

	@Override
	protected void onReset() {
	}

}
