package net.zomis.tttultimate.ais;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.zomis.aiscores.PreScorer;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class BoardImportanceAnalyze implements PreScorer<TTController> {
	// x AnalyzeHowImportantBoardIs for winning (X, O separately) -- especially in combination with INeedScorer

	@Override
	public Object analyze(TTController params) {
		Map<TTBase, Map<TTPlayer, Double>> importance = new HashMap<>();
		
		for (TTBase board : TicUtils.getAllSubs(params.getGame())) {
			Map<TTPlayer, Double> map = new HashMap<>();
			for (TTPlayer player : TTPlayer.values()) {
				if (!player.isExactlyOnePlayer())
					continue;
				
				double dd = 0;
				for (TTWinCondition win : TicUtils.getWinCondsWith(board, board.getParent())) {
					if (win.isWinnable(player)) {
						dd += 0.5 + win.hasCurrently(player);
					}
				}
				map.put(player, dd);
			}
			importance.put(board, map);
		}
		
		return new BoardImportance(importance);
	}

	@Override
	public void onScoringComplete() {
	}
	
	public static class BoardImportance {
		private final Map<TTBase, Map<TTPlayer, Double>> map;

		private BoardImportance(Map<TTBase, Map<TTPlayer, Double>> data) {
			this.map = Collections.unmodifiableMap(data);
		}
		public Map<TTBase, Map<TTPlayer, Double>> getMap() {
			return map;
		}
		public double getImportanceFor(TTBase board, TTPlayer player) {
			if (map.get(board) == null)
				return 0;
			return map.get(board).get(player);
		}
	}

}
