package net.zomis.tttultimate.players;

import java.util.Random;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.Scorers;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.ais.BlockOpponentScorer;
import net.zomis.tttultimate.ais.BoardImportanceAnalyze;
import net.zomis.tttultimate.ais.DestinationBoardIsWonScorer;
import net.zomis.tttultimate.ais.INeedScorerV1;
import net.zomis.tttultimate.ais.INeedScorerV2;
import net.zomis.tttultimate.ais.INeedScorerV3;
import net.zomis.tttultimate.ais.INeedScorerV4;
import net.zomis.tttultimate.ais.ImportantForMe;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV1;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV2;
import net.zomis.tttultimate.ais.OpponentShouldNotPlayScorerV3;
import net.zomis.tttultimate.ais.WhereCanOpponentSendMe;
import net.zomis.tttultimate.games.TTController;

public class TTAIFactory {
	private final String	name;
	private final ScoreConfigFactory<TTController, TTBase>	factory;

	private TTAIFactory(String name, ScoreConfigFactory<TTController, TTBase> factory) {
		this.name = name;
		this.factory = factory;
	}
	
	public TTAI build() {
		return new TTAI(name, factory.build());
	}
	private ScoreConfigFactory<TTController, TTBase> copy() {
		return factory.copy();
	}

	public static TTAIFactory idiot() {
		return new TTAIFactory("#AI_Complete_Idiot", new ScoreConfigFactory<TTController, TTBase>());
	}
	public static TTAIFactory versionOne() {
		return new TTAIFactory("#AI_First", 
				idiot().copy()
				.withScorer(new INeedScorerV1())
		);
	}
	public static TTAIFactory version2() {
		return new TTAIFactory("#AI_Second", 
				versionOne().copy()
				.withScorer(new OpponentShouldNotPlayScorerV1())
		);
	}
	public static TTAIFactory version3() {
		return new TTAIFactory("#AI_Third", 
				idiot().copy()
				.withScorer(new INeedScorerV2())
				.withScorer(new OpponentShouldNotPlayScorerV2())
		);
	}
	public static TTAIFactory improved3() {
		return new TTAIFactory("#AI_Medium", 
				versionOne().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				.withScorer(new INeedScorerV3())
				.withScorer(new OpponentShouldNotPlayScorerV3())
				.withScorer(new WhereCanOpponentSendMe())
				.withScorer(new DestinationBoardIsWonScorer())
				.withScorer(new BlockOpponentScorer())
		);
	}
	public static TTAIFactory unreleased() {
		// TODO: #AI_Unreleased is NOT THREAD-SAFE BECAUSE OF NORMALIZED SCORER!
		return new TTAIFactory("#AI_Unreleased", idiot().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				
				.withScorer(Scorers.normalized(Scorers.multiplication(new ImportantForMe(), new INeedScorerV4())))
				.withScorer(new OpponentShouldNotPlayScorerV3())
				.withScorer(new WhereCanOpponentSendMe())
				.withScorer(new DestinationBoardIsWonScorer(), 0.7)
				.withScorer(new BlockOpponentScorer(), 1.3)
		);
	}



	public static TTAIFactory best() {
		return unreleased(); // improved3();
	}

	private static Random random = new Random();
	public static TTAIFactory randomAllIn() {
		long seed = random.nextLong();
		Random random = new Random(seed);
		return new TTAIFactory("Random" + seed,
				idiot().copy()
				.withPreScorer(new BoardImportanceAnalyze())
				.withScorer(new INeedScorerV1(), r(random))
				.withScorer(new INeedScorerV2(), r(random))
				.withScorer(new INeedScorerV3(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV1(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV2(), r(random))
				.withScorer(new OpponentShouldNotPlayScorerV3(), r(random))
				.withScorer(new BlockOpponentScorer(), r(random))
				.withScorer(new DestinationBoardIsWonScorer(), r(random))
				.withScorer(new WhereCanOpponentSendMe(), r(random))
		);
	}

	private static double r(Random random) {
		return random.nextDouble();
	}

}
