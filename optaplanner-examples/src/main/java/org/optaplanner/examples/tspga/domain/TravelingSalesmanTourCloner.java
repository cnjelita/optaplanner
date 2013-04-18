/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.tspga.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;

public class TravelingSalesmanTourCloner implements SolutionCloner<TravelingSalesmanTour> {

	public TravelingSalesmanTour cloneSolution(TravelingSalesmanTour original) {
//		System.out.println("clone");
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
        clone.setName(original.getName());
        clone.setId(original.getId());
//		clone.generateIdMaps();
//		System.out.println(clonedVisitList.size());
		return clone;
	}
}
