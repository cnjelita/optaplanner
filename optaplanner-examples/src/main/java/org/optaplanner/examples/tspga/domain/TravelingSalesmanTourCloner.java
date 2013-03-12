package org.optaplanner.examples.tspga.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.api.domain.solution.cloner.SolutionCloner;

public class TravelingSalesmanTourCloner implements SolutionCloner<TravelingSalesmanTour> {

    public TravelingSalesmanTour cloneSolution(TravelingSalesmanTour original) {
        TravelingSalesmanTour clone = new TravelingSalesmanTour();

        List<Domicile> clonedDomicileList = new ArrayList<Domicile>();
        Map<City, Appearance> cityToAppearanceMap = new HashMap<City, Appearance>();
        for (Domicile domicile : original.getDomicileList()) {
            Domicile clonedDomicile = domicile.cloneDomicile();
            clonedDomicileList.add(clonedDomicile);
            cityToAppearanceMap.put(clonedDomicile.getCity(), clonedDomicile);
        }
        List<Visit> clonedVisitList = new ArrayList<Visit>();
        Map<City, City> cityToPreviousAppearanceCityMap = new HashMap<City, City>();

        for (Visit visit : original.getVisitList()) {
            if (visit.getPreviousAppearance() != null) {
                cityToPreviousAppearanceCityMap.put(visit.getCity(), visit.getPreviousAppearance().getCity());
            }
            Visit clonedVisit = visit.cloneVisit();
            clonedVisitList.add(clonedVisit);
            cityToAppearanceMap.put(clonedVisit.getCity(), clonedVisit);
        }

        for (Visit visit : clonedVisitList) {
            visit.setPreviousAppearance(cityToAppearanceMap.get(cityToPreviousAppearanceCityMap.get(visit.getCity())));
        }

        clone.setCityList(original.getCityList());
        clone.setDomicileList(clonedDomicileList);
        clone.setVisitList(clonedVisitList);
        clone.setScore(original.getScore());
        clone.generateIdMaps();
        return clone;
    }
}
