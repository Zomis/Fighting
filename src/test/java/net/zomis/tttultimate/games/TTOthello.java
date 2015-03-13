package net.zomis.tttultimate.games;

import java.util.ArrayList;
import java.util.List;

import net.zomis.common.Direction8;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTFactories;
import net.zomis.tttultimate.TTPlayer;

public class TTOthello extends TTController {

	public TTOthello() {
		this(8);
	}
	public TTOthello(int size) {
		super(new TTFactories().othello(size));
		this.onReset();
	}

	@Override
	public boolean isAllowedPlay(TTBase tile) {
		if (getGame().isWon())
			return false;
		if (tile.hasSubs())
			return false;
		if (tile.isWon())
			return false;
		return !fieldCover(tile, currentPlayer).isEmpty();
	}
	
	private List<TTBase> fieldCover(TTBase tile, TTPlayer player) {
		if (!player.isExactlyOnePlayer())
			throw new IllegalArgumentException();
		
		List<TTBase> tt = new ArrayList<>();
		TTBase parent = tile.getParent();
		for (Direction8 dir : Direction8.values()) {
			boolean matchFound = false;
			List<TTBase> thisDirection = new ArrayList<>();
			TTBase loop = tile;
			do {
				loop = parent.getSub(loop.getX() + dir.getDeltaX(), loop.getY() + dir.getDeltaY());
				if (loop == null)
					break;
				if (loop.getWonBy() == TTPlayer.NONE)
					break;
				if (player.and(loop.getWonBy()) == player) {
					matchFound = true;
				}
				if (player != loop.getWonBy()) {
					thisDirection.add(loop);
				}
			}
			while (!matchFound);
			
			if (matchFound)
				tt.addAll(thisDirection);
			
		}
		return tt;
	}
	

	@Override
	protected boolean performPlay(TTBase tile) {
		List<TTBase> convertingTiles = fieldCover(tile, currentPlayer);
		for (TTBase ff : convertingTiles) {
			ff.setPlayedBy(currentPlayer);
		}
		tile.setPlayedBy(currentPlayer);
		nextPlayer();
		if (!isMovePossible(currentPlayer)) {
			nextPlayer();
			if (!isMovePossible(currentPlayer)) {
				int x = countSquares(TTPlayer.X);
				int o = countSquares(TTPlayer.O);
				TTPlayer result = TTPlayer.NONE;
				if (x >= o)
					result = result.or(TTPlayer.X);
				if (o >= x)
					result = result.or(TTPlayer.O);
					
				game.setPlayedBy(result);
			}
		}
		
		return true;
	}

	private int countSquares(TTPlayer player) {
		int count = 0;
		for (int xx = 0; xx < game.getSizeX(); xx++) {
			for (int yy = 0; yy < game.getSizeY(); yy++) {
				TTBase sub = game.getSub(xx, yy);
				if (sub.getWonBy().is(player))
					count++;
			}
		}
		
		return count;
	}
	private boolean isMovePossible(TTPlayer currentPlayer) {
		for (int xx = 0; xx < this.game.getSizeX(); xx++) {
			for (int yy = 0; yy < this.game.getSizeY(); yy++) {
				if (this.isAllowedPlay(game.getSub(xx, yy)))
					return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onReset() {
		TTBase board = this.getGame();
		board.reset();
		int mX = board.getSizeX() / 2;
		int mY = board.getSizeY() / 2;
		board.getSub(mX - 1, mY - 1).setPlayedBy(TTPlayer.X);
		board.getSub(mX - 1, mY    ).setPlayedBy(TTPlayer.O);
		board.getSub(mX    , mY - 1).setPlayedBy(TTPlayer.O);
		board.getSub(mX    , mY    ).setPlayedBy(TTPlayer.X);
	}

}
