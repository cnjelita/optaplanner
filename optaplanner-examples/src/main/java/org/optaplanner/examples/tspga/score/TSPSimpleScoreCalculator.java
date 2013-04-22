package org.optaplanner.examples.tspga.score;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;
import org.optaplanner.examples.tspga.domain.*;

import java.util.HashMap;
import java.util.Map;

public class TSPSimpleScoreCalculator implements SimpleScoreCalculator<TravelingSalesmanTour> {

    @Override
    public Score calculateScore(TravelingSalesmanTour solution) {
        Domicile domicile = solution.getDomicileList().get(0);
        Map<City, City> inverseChain = new HashMap<City, City>();
        for (Visit visit : solution.getVisitList()) {
            if (visit.getPreviousAppearance() != null) {
                inverseChain.put(visit.getPreviousAppearance().getCity(), visit.getCity());
            }
        }
        City currentCity = domicile.getCity();
        int score = 0;
        City previousCity = currentCity;
        while ((currentCity = inverseChain.get(previousCity)) != null) {
            score -= previousCity.getDistance(currentCity);
            previousCity = currentCity;
        }
        score -= previousCity.getDistance(domicile.getCity());
        return SimpleScore.valueOf(score);
    }
}
