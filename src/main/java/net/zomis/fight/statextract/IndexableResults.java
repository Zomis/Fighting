package net.zomis.fight.statextract;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Simon on 3/14/2015.
 */
public class IndexableResults {

    private final List<ExtractResults> results;
    private final List<InstancePoster> posters;

    public IndexableResults(List<InstancePoster> posters) {
        this.posters = posters;
        this.results = posters.stream().map(p -> p.collect()).collect(Collectors.toList());
    }

    public ExtractResults unindexed() {
        return null;
    }

    public ExtractResults indexBy(String... fields) {
        return null;
    }

    public List<ExtractResults> getResults() {
        return posters.stream().map(po -> po.collect()).collect(Collectors.toList());
    }

}
