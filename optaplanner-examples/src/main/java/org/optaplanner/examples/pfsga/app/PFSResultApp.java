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

package org.optaplanner.examples.pfsga.app;

import java.util.Arrays;
import java.util.List;

import org.optaplanner.core.config.solver.XmlSolverFactory;

public class PFSResultApp {

    public static void main(String[] args) {
        List<String> dataFiles = Arrays
                .asList("reC03", "reC05", "reC07", "reC09", "reC11", "reC13", "reC15", "reC17", "reC19", "reC21", "reC23",
                        "reC27", "reC29");
        // Build the Solver
        XmlSolverFactory solverFactory = new XmlSolverFactory();
//        Solver solver = solverFactory.configure(SOLVER_CONFIG).buildSolver();
//
//        File f = new File("optaplanner-examples/data/pfsga/input/car1.txt");
//        PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
//        solver.setPlanningProblem(instance);
//        solver.solve();
    }
}
