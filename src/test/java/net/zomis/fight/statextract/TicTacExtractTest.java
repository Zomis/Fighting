package net.zomis.fight.statextract;

import net.zomis.tttultimate.TTBase;
import net.zomis.tttultimate.TTFactories;
import net.zomis.tttultimate.TTPlayer;
import net.zomis.tttultimate.games.TTClassicController;
import net.zomis.tttultimate.players.TTAI;
import net.zomis.tttultimate.players.TTAIFactory;
import org.junit.jupiter.api.Test;

import java.util.function.ToIntFunction;

/**
 * Created by Simon on 3/13/2015.
 */
public class TicTacExtractTest {

    public static class TTExtract {
        ToIntFunction<TTBase> played = pl -> 1;
        ToIntFunction<TTClassicController> board = b -> b.getMoveCount();
        ToIntFunction<TTClassicController> player = b -> b.getCurrentPlayer().ordinal();
    }

    public void playGame(Extractor extractor, TTAI playerX, TTAI playerO) {
        TTBase ttt = new TTFactories().classicMNK(3);
        TTClassicController controller = new TTClassicController(ttt);
        Poster poster = extractor.postPrimary();
        while (!controller.isGameOver()) {
            TTAI ai = controller.getCurrentPlayer().is(TTPlayer.X) ? playerX : playerO;
            TTBase tile = ai.play(controller);
            if (tile == null) {
                break;
            }
            controller.play(tile);
            poster.post(tile);
        }
        poster.post(controller);
    }

    @Test
    public void ttExtract() {
        Extractor extractor = Extractor.extractor(new TTExtract());
        for (int i = 0; i < 100; i++) {
            playGame(extractor, TTAIFactory.idiot().build(), TTAIFactory.unreleased().build());
        }
        IndexableResults results = extractor.collectIndexable();
        for (ExtractResults res : results.getResults()) {
            System.out.println(res.getData());
        }
    }

}
