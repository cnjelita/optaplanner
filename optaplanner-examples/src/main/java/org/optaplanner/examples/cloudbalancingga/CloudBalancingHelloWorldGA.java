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

package org.optaplanner.examples.cloudbalancingga;

import org.optaplanner.config.SolverFactory;
import org.optaplanner.config.XmlSolverFactory;
import org.optaplanner.core.Solver;
import org.optaplanner.examples.cloudbalancingga.domain.CloudBalance;
import org.optaplanner.examples.cloudbalancingga.domain.CloudComputer;
import org.optaplanner.examples.cloudbalancingga.domain.CloudProcess;
import org.optaplanner.examples.cloudbalancingga.persistence.CloudBalancingGenerator;

public class CloudBalancingHelloWorldGA {

    public static void main(String[] args) {
        // Build the Solver
        SolverFactory solverFactory = new XmlSolverFactory(
                "/org/optaplanner/examples/cloudbalancingga/solver/cloudBalancingSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();

        // Load a problem with 400 computers and 1200 processes
        //new CloudBalancingGenerator().generate();
        CloudBalance unsolvedCloudBalance = new CloudBalancingGenerator().createCloudBalance(800, 2400);
        unsolvedCloudBalance.generateIdMaps();
        // Solve the problem
        solver.setPlanningProblem(unsolvedCloudBalance);
        solver.solve();
        CloudBalance solvedCloudBalance = (CloudBalance) solver.getBestSolution();
//        CloudBalancingSimpleScoreCalculator sc = new CloudBalancingSimpleScoreCalculator();
//        System.out.println(solvedCloudBalance.getScore());
//        System.out.println(sc.calculateScore(solvedCloudBalance));
        // Display the result
        System.out.println("\nSolved cloudBalance with 400 computers and 1200 processes:\n"
                + toDisplayString(solvedCloudBalance));
    }

    public static String toDisplayString(CloudBalance cloudBalance) {
        StringBuilder displayString = new StringBuilder();
        for (CloudProcess process : cloudBalance.getProcessList()) {
            CloudComputer computer = process.getComputer();
            displayString.append("  ").append(process.getLabel()).append(" -> ")
                    .append(computer == null ? null : computer.getLabel()).append("\n");
        }
        return displayString.toString();
    }

}
