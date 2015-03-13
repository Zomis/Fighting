package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.games.TTController;

public class WhereCanOpponentSendMe extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		// TODO: Check which fields opponent can send me to on the target board, and check how much I want to play on those fields.
		 // Could there be a recursive approach for this? A deeper expected value calculation?
		
		return 0;
	}

}
