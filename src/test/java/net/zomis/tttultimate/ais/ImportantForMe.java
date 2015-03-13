package net.zomis.tttultimate.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.games.TTController;

public class ImportantForMe extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		return scores.getAnalyze(BoardImportanceAnalyze.BoardImportance.class)
				.getImportanceFor(field.getParent(), scores.getParameters().getCurrentPlayer());
	}

}
