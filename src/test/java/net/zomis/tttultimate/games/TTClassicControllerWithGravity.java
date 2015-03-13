package net.zomis.tttultimate.games;

import net.zomis.tttultimate.TTBase;

public class TTClassicControllerWithGravity extends TTClassicController {

	public TTClassicControllerWithGravity(TTBase board) {
		super(board);
	}
	
	@Override
	public boolean isAllowedPlay(TTBase tile) {
		boolean sup = super.isAllowedPlay(tile);
		if (!sup)
			return false;
		
		TTBase parent = tile.getParent();
		TTBase below = parent.getSub(tile.getX(), tile.getY() + 1);
		if (below == null)
			return true;
		return below.isWon();
	}

}
