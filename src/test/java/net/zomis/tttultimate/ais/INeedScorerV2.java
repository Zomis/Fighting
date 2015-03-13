package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class INeedScorerV2 extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		TTPlayer currentPlayer = scores.getParameters().getCurrentPlayer();
		Collection<TTWinCondition> colls = TicUtils.getWinCondsWith(field, field.getParent());
		double i = 0;
		for (TTWinCondition conds : colls) {
			if (conds.isWinnable(currentPlayer))
				i += conds.hasCurrently(currentPlayer);
		}
		return i;
	}

}
