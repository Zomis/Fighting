package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class OpponentShouldNotPlayScorerV2 extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		TTPlayer opponent = scores.getParameters().getCurrentPlayer().next();
		TTBase sendToBoard = TicUtils.getDestinationBoard(field);
		
		Collection<TTWinCondition> colls = sendToBoard.getWinConds();
		double i = 0;
		for (TTWinCondition conds : colls) {
			i += conds.neededForWin(opponent);
		}
		return i;
	}

}
