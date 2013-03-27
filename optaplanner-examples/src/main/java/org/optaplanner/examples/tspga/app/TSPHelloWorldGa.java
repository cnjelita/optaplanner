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

import java.io.File;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.examples.tspga.domain.TravelingSalesmanTour;
import org.optaplanner.examples.tspga.persistence.TspSolutionImporter;
import org.optaplanner.examples.tspga.score.TspIncrementalScoreCalculator;

public class TSPHelloWorldGa {

	public static final String SOLVER_CONFIG
			= "/org/optaplanner/examples/tspga/solver/tspSolverConfig.xml";

	public static void main(String[] args) {

		// Build the Solver
		XmlSolverFactory solverFactory = new XmlSolverFactory();
		Solver solver = solverFactory.configure(SOLVER_CONFIG).buildSolver();
		File f = new File("optaplanner-examples/data/tsp/input/dj38.tsp");
		System.out.println(f.getAbsolutePath());
		TravelingSalesmanTour tour = (TravelingSalesmanTour) new TspSolutionImporter().readSolution(f);

		solver.setPlanningProblem(tour);

		solver.solve();

		tour = (TravelingSalesmanTour) solver.getBestSolution();

		TspIncrementalScoreCalculator scoreCalculator = new TspIncrementalScoreCalculator();
		scoreCalculator.resetWorkingSolution(tour);
//		System.out.println(scoreCalculator.calculateScore());

//        // Load a problem with 400 computers and 1200 processes
//        //new CloudBalancingGenerator().generate();
//        CloudBalance unsolvedCloudBalance = new CloudBalancingGenerator().createCloudBalance(800, 2400);
//        unsolvedCloudBalance.generateIdMaps();
//        // Solve the problem
//        solver.setPlanningProblem(unsolvedCloudBalance);
//        solver.solve();
//        CloudBalance solvedCloudBalance = (CloudBalance) solver.getBestSolution();
////        CloudBalancingSimpleScoreCalculator sc = new CloudBalancingSimpleScoreCalculator();
////        System.out.println(solvedCloudBalance.getScore());
////        System.out.println(sc.calculateScore(solvedCloudBalance));
//        // Display the result
////        System.out.println("\nSolved cloudBalance with 400 computers and 1200 processes:\n"
////                + toDisplayString(solvedCloudBalance));
	}
}
