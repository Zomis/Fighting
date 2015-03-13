package net.zomis.tttultimate.ais;

import java.util.Collection;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.TTWinCondition;
import net.zomis.tttultimate.games.TTController;

public class BlockOpponentScorer extends AbstractScorer<TTController, TTBase> {

	@Override
	public double getScoreFor(TTBase field, ScoreParameters<TTController> scores) {
		Collection<TTWinCondition> conds = field.getParent().getWinConds();
		TTPlayer player = scores.getParameters().getCurrentPlayer().next();
		double has = 0;
		for (TTWinCondition cond : conds) {
			if (!cond.isWinnable(player))
				continue;
			if (!cond.hasWinnable(field))
				continue;
			int currently = cond.hasCurrently(player);
			double max = cond.size();
			has = Math.max(has, (double) currently / max);
		}
		return has;
	}

}
