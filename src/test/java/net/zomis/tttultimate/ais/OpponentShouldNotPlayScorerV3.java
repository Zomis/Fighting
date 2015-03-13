package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class OpponentShouldNotPlayScorerV3 extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		TTPlayer opponent = scores.getParameters().getCurrentPlayer().next();
		TTBase sendToBoard = TicUtils.getDestinationBoard(field);
		if (sendToBoard == null)
			sendToBoard = field.getParent();
		
		if (!sendToBoard.isWon()) // board is won, then opponent should definitely not play there. Let another scorer handle this case
			return 0;
		
		Collection<TTWinCondition> colls = sendToBoard.getWinConds();
		double i = 0;
		for (TTWinCondition conds : colls) {
			i += conds.neededForWin(opponent);
		}
		// x DestinationBoardIsWonScorer
		return i / colls.size();
	}

}
