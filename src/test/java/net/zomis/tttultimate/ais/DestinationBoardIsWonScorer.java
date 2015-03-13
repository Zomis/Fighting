package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class DestinationBoardIsWonScorer extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		TTBase sendToBoard = TicUtils.getDestinationBoard(field);
		return sendToBoard != null && !sendToBoard.isWon() ? 1 : 0;
	}

}
