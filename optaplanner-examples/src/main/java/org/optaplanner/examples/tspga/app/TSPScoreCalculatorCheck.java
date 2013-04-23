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

package org.optaplanner.examples.tspga.app;

import org.optaplanner.examples.tspga.score.TspIncrementalScoreCalculator;
import org.optaplanner.examples.tspga.domain.Appearance;
import org.optaplanner.examples.tspga.domain.TravelingSalesmanTour;
import org.optaplanner.examples.tspga.domain.Visit;
import org.optaplanner.examples.tspga.persistence.TspSolutionImporter;
import org.optaplanner.examples.tspga.score.TSPSimpleScoreCalculator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TSPScoreCalculatorCheck {
    public static void main(String[] args) throws FileNotFoundException {
        File dataSet = new File("data/tsp/input/eil51.tsp");
        TravelingSalesmanTour tour = (TravelingSalesmanTour) new TspSolutionImporter().readSolution(dataSet);
        File optTour = new File("data/tsp/input/eil51.opt.tour");
        Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(optTour)));
        skipTill(sc, "DIMENSION");
        //Skipping ":"
        sc.next();
        int numberOfCities = sc.nextInt();
        skipTill(sc, "TOUR_SECTION");
        //Skipping domicile
        sc.next();
        Appearance previousAppearance = tour.getDomicileList().get(0);
        for (int i = 0; i < numberOfCities - 2; i++) {
            Visit currentVisit = tour.getVisitList().get(sc.nextInt() - 2);
            currentVisit.setPreviousAppearance(previousAppearance);
            previousAppearance = currentVisit;
        }

        TSPSimpleScoreCalculator simpleScoreCalculator = new TSPSimpleScoreCalculator();
        System.out.println("Result simple score calculation " + simpleScoreCalculator.calculateScore(tour));

        TspIncrementalScoreCalculator incrementalScoreCalculator = new TspIncrementalScoreCalculator();
        incrementalScoreCalculator.resetWorkingSolution(tour);
        System.out.println("Result incremental score calculation " + incrementalScoreCalculator.calculateScore());
    }

    private static void skipTill(Scanner sc, String delimiter) {
        while (sc.hasNext() && !sc.next().contains(delimiter)) {
            //DO nothing
        }
    }
}
