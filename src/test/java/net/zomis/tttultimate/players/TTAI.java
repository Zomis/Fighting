package net.zomis.tttultimate.players;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Random;

import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TicUtils;
import net.zomis.tttultimate.games.TTController;

public class TTAI implements ScoreStrategy<TTController, TTBase> {

	private final ScoreConfig<TTController, TTBase> config;
	private FieldScoreProducer<TTController, TTBase>	producer;
	private Random	random = new Random();
	private final String	name;

	public TTAI(String name, ScoreConfig<TTController, TTBase> config) {
		this.name = name;
		this.config = config;
		this.producer = new FieldScoreProducer<TTController, TTBase>(this.config, this);
	}
	
	public TTBase play(TTController board) {
		ParamAndField<TTController, TTBase> ff = ScoreUtils.pickBest(producer, board, random);
		if (ff == null)
			return null;
		return ff.getField();
	}

	public FieldScores<TTController, TTBase> score(TTController board) {
		producer.setDetailed(true);
		FieldScores<TTController, TTBase> scores = producer.analyzeAndScore(board);
		producer.setDetailed(false);
		return scores;
	}
	public FieldScoreProducer<TTController, TTBase> getProducer() {
		return producer;
	}
	
	@Override
	public Collection<TTBase> getFieldsToScore(TTController params) {
		return TicUtils.getAllSmallestFields(params.getGame());
	}

	@Override
	public boolean canScoreField(ScoreParameters<TTController> parameters, TTBase field) {
		return parameters.getParameters().isAllowedPlay(field);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void logScores(TTController board) {
		FieldScores<TTController, TTBase> scores = this.score(board);
		for (Entry<TTBase, FieldScore<TTBase>> ee : scores.getScores().entrySet()) {
			System.out.println("Score for " + ee.getKey());
			FieldScore<TTBase> value = ee.getValue();
			System.out.println("Normalized: " + value.getNormalized());
			System.out.println("Rank: " + value.getRank());
			System.out.println("Details: " + value.getScoreMap());
			System.out.println();
		}
	}

	public ScoreConfig<TTController, TTBase> getConfig() {
		return this.config;
	}
	
	public void setRandomSeed(long random) {
		this.random = new Random(random);
	}
}
