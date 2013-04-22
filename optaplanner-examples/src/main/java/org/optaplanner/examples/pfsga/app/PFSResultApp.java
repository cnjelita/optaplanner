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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.simpledouble.SimpleDoubleScore;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.core.impl.event.BestSolutionChangedEvent;
import org.optaplanner.core.impl.event.SolverEventListener;
import org.optaplanner.examples.cloudbalancingga.domain.CloudBalance;
import org.optaplanner.examples.cloudbalancingga.persistence.CloudBalancingDaoImpl;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;
import org.optaplanner.examples.pfsga.persistence.PFSSolutionImporter;

public class PFSResultApp {

    public static final String GA_SOLVER_CONFIG
            = "/org/optaplanner/examples/pfsga/solver/pfsgaGASolverConfig.xml";

    public static final String LS_SOLVER_CONFIG
            = "/org/optaplanner/examples/pfsga/solver/pfsgaLSSolverConfig.xml";

    public static final String TSP_GA__SOLVER_CONFIG
            = "/org/optaplanner/examples/tspga/solver/pfsgaGASolverConfig.xml";

    public static final String TSP_LS_SOLVER_CONFIG
            = "/org/optaplanner/examples/tspga/solver/pfsgaLSSolverConfig.xml";

    public static final String CB_GA_SOLVER_CONFIG
            = "/org/optaplanner/examples/cloudbalancingga/solver/cloudBalancingGASolverConfig.xml";

    public static final String CB_LS_SOLVER_CONFIG
            = "/org/optaplanner/examples/cloudbalancingga/solver/cloudBalancingLSSolverConfig.xml";

    private static int numberOfRuns = 10;

    public static void main(String[] args) throws IOException {

//        System.out.println(SimpleScore.valueOf(14).add(SimpleScore.valueOf(10)));
//        System.out.println(SimpleDoubleScore.valueOf(0).add(SimpleDoubleScore.valueOf(10).divide(3)));
//
//        System.exit(0);
//        List<String> dataFiles = Arrays
//                .asList("car1", "car2", "car3", "car4", "car5", "car6", "car7", "car8", "reC01", "reC03", "reC05",
//                        "reC07", "reC09", "reC11", "reC13", "reC15", "reC17", "reC19", "reC21", "reC23",
//                        "reC25", "reC27", "reC29", "reC31", "reC33", "reC35", "reC37", "reC39"
//                        , "reC41");
//
//        Map<String, Integer> dataFileToOptimalScoreMap = new HashMap<String, Integer>();
//
//        dataFileToOptimalScoreMap.put("car1", 7038);
//        dataFileToOptimalScoreMap.put("car2", 7166);
//        dataFileToOptimalScoreMap.put("car3", 7312);
//        dataFileToOptimalScoreMap.put("car4", 8003);
//        dataFileToOptimalScoreMap.put("car5", 7720);
//        dataFileToOptimalScoreMap.put("car6", 8505);
//        dataFileToOptimalScoreMap.put("car7", 6590);
//        dataFileToOptimalScoreMap.put("car8", 8366);
//        dataFileToOptimalScoreMap.put("reC01", 1247);
//        dataFileToOptimalScoreMap.put("reC03", 1109);
//        dataFileToOptimalScoreMap.put("reC05", 1242);
//        dataFileToOptimalScoreMap.put("reC07", 1566);
//        dataFileToOptimalScoreMap.put("reC09", 1537);
//        dataFileToOptimalScoreMap.put("reC11", 1431);
//        dataFileToOptimalScoreMap.put("reC13", 1930);
//        dataFileToOptimalScoreMap.put("reC15", 1950);
//        dataFileToOptimalScoreMap.put("reC17", 1902);
//        dataFileToOptimalScoreMap.put("reC19", 2093);
//        dataFileToOptimalScoreMap.put("reC21", 2017);
//        dataFileToOptimalScoreMap.put("reC23", 2011);
//        dataFileToOptimalScoreMap.put("reC25", 2513);
//        dataFileToOptimalScoreMap.put("reC27", 2373);
//        dataFileToOptimalScoreMap.put("reC29", 2287);
//        dataFileToOptimalScoreMap.put("reC31", 3045);
//        dataFileToOptimalScoreMap.put("reC33", 3114);
//        dataFileToOptimalScoreMap.put("reC35", 3277);
//        dataFileToOptimalScoreMap.put("reC37", 4951);
//        dataFileToOptimalScoreMap.put("reC39", 5087);
//        dataFileToOptimalScoreMap.put("reC41", 4960);
////                        ,
////
//
//        String fileName = new SimpleDateFormat("'pfs_ga_'dd-MM-yy:hh-mm'.txt'").format(new Date());
//        File outputFile = new File("/home/sam/Desktop/results/" + fileName);
//        PrintWriter sc = new PrintWriter(new FileWriter(outputFile));
//
//        String outputString = "";
//        // Build the Solver
//        for (String dataFile : dataFiles) {
//
//            File f = new File("/home/sam/git-repos/optaplanner/optaplanner-examples/data/pfsga/input/" + dataFile + ".txt");
//            System.out.println(f.getAbsolutePath());
//            sc.println("Optimal Result " + dataFileToOptimalScoreMap.get(dataFile));
//
//            outputString += dataFile;
//
//            sc.println("GA===========================");
//            List<Score> dataFileScoreList = new ArrayList<Score>();
//            List<Long> dataFileConvergenceTimeList = new ArrayList<Long>();
//            Score maxScore = null;
//            Score minScore = null;
//            double meanScore = 0;
//
//            for (int i = 0; i < numberOfRuns; i++) {
//                XmlSolverFactory solverFactory = new XmlSolverFactory();
//                Solver solver = solverFactory.configure(GA_SOLVER_CONFIG).buildSolver();
//                PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
//                solver.setPlanningProblem(instance);
//                Listener l = new Listener(solver, dataFileToOptimalScoreMap.get(dataFile));
//                solver.addEventListener(l);
//                solver.solve();
//                solver.getTimeMillisSpend();
//                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
//                Score currentScore = solver.getBestSolution().getScore();
//                meanScore += currentScore.toDoubleLevels()[0];
//                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
//                    maxScore = currentScore;
//                }
//                if (minScore == null || minScore.compareTo(currentScore) > 0) {
//                    minScore = currentScore;
//                }
//                dataFileScoreList.add(currentScore);
//
//            }
//
//            meanScore /= numberOfRuns;
//
//            sc.println(dataFile);
//            double meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
//            sc.println("Mean convergence time " + meanConvergenceTime);
//            sc.println("mean score " + meanScore);
//            double averageGap = calculateGap(SimpleDoubleScore.valueOf(meanScore), dataFileToOptimalScoreMap
//                    .get(dataFile));
//            sc.println("mean gap " + averageGap);
//            sc.println("max score " + maxScore);
//            sc.println("max gap " + calculateGap(maxScore, dataFileToOptimalScoreMap.get(dataFile)));
//            sc.println("min score " + minScore);
//            sc.println("min gap " + calculateGap(minScore, dataFileToOptimalScoreMap.get(dataFile)));
//
//            outputString += " & " + (-1 * minScore
//                    .toDoubleLevels()[0]) + " & " + (-1 * meanScore) + " & " + (-1 * maxScore
//                    .toDoubleLevels()[0]) + " & " + averageGap + " & " + meanConvergenceTime;
//
//            sc.println("Raw convergence time results: ");
//            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
//                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
//            }
//            sc.println();
//            sc.println("Raw score results: ");
//            for (int i = 0; i < dataFileScoreList.size(); i++) {
//                sc.print(dataFileScoreList.get(i) + ", ");
//            }
//
//            sc.println("\nLS===========================");
//            dataFileScoreList = new ArrayList<Score>();
//            dataFileConvergenceTimeList = new ArrayList<Long>();
//            maxScore = null;
//            minScore = null;
//            meanScore = 0;
//
//            for (int i = 0; i < numberOfRuns; i++) {
//                XmlSolverFactory solverFactory = new XmlSolverFactory();
//                Solver solver = solverFactory.configure(LS_SOLVER_CONFIG).buildSolver();
//                PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
//                solver.setPlanningProblem(instance);
//                Listener l = new Listener(solver, dataFileToOptimalScoreMap.get(dataFile));
//                solver.addEventListener(l);
//                solver.solve();
//                solver.getTimeMillisSpend();
//                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
//                Score currentScore = solver.getBestSolution().getScore();
//                meanScore += currentScore.toDoubleLevels()[0];
//                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
//                    maxScore = currentScore;
//                }
//                if (minScore == null || minScore.compareTo(currentScore) > 0) {
//                    minScore = currentScore;
//                }
//                dataFileScoreList.add(currentScore);
//
//            }
//
//            meanScore /= numberOfRuns;
//
//            meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
//            sc.println("Mean convergence time " + meanConvergenceTime);
//            sc.println("mean score " + meanScore);
//            averageGap = calculateGap(SimpleDoubleScore.valueOf(meanScore), dataFileToOptimalScoreMap
//                    .get(dataFile));
//            sc.println("mean gap " + averageGap);
//            sc.println("max score " + maxScore);
//            sc.println("max gap " + calculateGap(maxScore, dataFileToOptimalScoreMap.get(dataFile)));
//            sc.println("min score " + minScore);
//            sc.println("min gap " + calculateGap(minScore, dataFileToOptimalScoreMap.get(dataFile)));
//
//            outputString += " & " + (-1 * minScore
//                    .toDoubleLevels()[0]) + " & " + (-1 * meanScore) + " & " + (-1 * maxScore
//                    .toDoubleLevels()[0]) + " & " + averageGap + " & " + meanConvergenceTime;
//
//            sc.println("Raw convergence time results: ");
//            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
//                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
//            }
//            sc.println();
//            sc.println("Raw score results: ");
//            for (int i = 0; i < dataFileScoreList.size(); i++) {
//                sc.print(dataFileScoreList.get(i) + ", ");
//            }
//
//            sc.println();
//            sc.println();
//            sc.println();
//            sc.println();
//            outputString += "\n";
//        }
//        sc.println(outputString);
//        sc.close();
//
//        dataFiles = Arrays
//                .asList("cb-0100comp-0300proc"
//                        , "cb-0200comp-0600proc"
//                        , "cb-0400comp-1200proc",
//                        "cb-0800comp-2400proc");
////
////                        ,
////
//
//        fileName = new SimpleDateFormat("'cb_ga_'dd-MM-yy:hh-mm'.txt'").format(new Date());
//        outputFile = new File("/home/sam/Desktop/results/" + fileName);
//        sc = new PrintWriter(new FileWriter(outputFile));
//
//        outputString = "";
//        // Build the Solver
//        for (String dataFile : dataFiles) {
//
//            File f = new File("/home/sam/git-repos/optaplanner/optaplanner-examples/data/cloudbalancing/unsolved/" + dataFile + ".xml");
//
//            outputString += dataFile;
//
//            sc.println("GA===========================");
//            List<Score> dataFileScoreList = new ArrayList<Score>();
//            List<Long> dataFileConvergenceTimeList = new ArrayList<Long>();
//            Score maxScore = null;
//            Score minScore = null;
//            Score meanScore = HardSoftScore.valueOf(0, 0);
//
//            for (int i = 0; i < numberOfRuns; i++) {
//                XmlSolverFactory solverFactory = new XmlSolverFactory();
//                Solver solver = solverFactory.configure(CB_GA_SOLVER_CONFIG).buildSolver();
//                CloudBalance instance = (CloudBalance) new CloudBalancingDaoImpl().readSolution(f);
//                solver.setPlanningProblem(instance);
//                Listener l = new Listener(solver, -1);
//                solver.addEventListener(l);
//                solver.solve();
//                solver.getTimeMillisSpend();
//                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
//                Score currentScore = solver.getBestSolution().getScore();
//                meanScore.add(currentScore);
//                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
//                    maxScore = currentScore;
//                }
//                if (minScore == null || minScore.compareTo(currentScore) > 0) {
//                    minScore = currentScore;
//                }
//                dataFileScoreList.add(currentScore);
//
//            }
//
//            meanScore.divide(numberOfRuns);
//
//            sc.println(dataFile);
//            double meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
//            sc.println("Mean convergence time " + meanConvergenceTime);
//            sc.println("mean score " + meanScore);
//            sc.println("max score " + maxScore);
//            sc.println("min score " + minScore);
//
//            outputString += " & " + minScore + " & " + meanScore + " & " + maxScore + " & " + " & " + meanConvergenceTime;
//
//            sc.println("Raw convergence time results: ");
//            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
//                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
//            }
//            sc.println();
//            sc.println("Raw score results: ");
//            for (int i = 0; i < dataFileScoreList.size(); i++) {
//                sc.print(dataFileScoreList.get(i) + ", ");
//            }
//
//            sc.println("\nLS===========================");
//            dataFileScoreList = new ArrayList<Score>();
//            dataFileConvergenceTimeList = new ArrayList<Long>();
//            maxScore = null;
//            minScore = null;
//            meanScore = HardSoftScore.valueOf(0, 0);
//
//            for (int i = 0; i < numberOfRuns; i++) {
//                XmlSolverFactory solverFactory = new XmlSolverFactory();
//                Solver solver = solverFactory.configure(CB_LS_SOLVER_CONFIG).buildSolver();
//                CloudBalance instance = (CloudBalance) new CloudBalancingDaoImpl().readSolution(f);
//                solver.setPlanningProblem(instance);
//                Listener l = new Listener(solver, -1);
//                solver.addEventListener(l);
//                solver.solve();
//                solver.getTimeMillisSpend();
//                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
//                Score currentScore = solver.getBestSolution().getScore();
//                meanScore.add(currentScore);
//                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
//                    maxScore = currentScore;
//                }
//                if (minScore == null || minScore.compareTo(currentScore) > 0) {
//                    minScore = currentScore;
//                }
//                dataFileScoreList.add(currentScore);
//
//            }
//
//            meanScore.divide(numberOfRuns);
//
//            meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
//            sc.println("Mean convergence time " + meanConvergenceTime);
//            sc.println("mean score " + meanScore);
//            sc.println("max score " + maxScore);
//            sc.println("min score " + minScore);
////            sc.println("min gap " + calculateGap(minScore, dataFileToOptimalScoreMap.get(dataFile)));
//
//            outputString += " & " + minScore + " & " + meanScore + " & " + maxScore + " & " + meanConvergenceTime;
//
//            sc.println("Raw convergence time results: ");
//            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
//                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
//            }
//            sc.println();
//            sc.println("Raw score results: ");
//            for (int i = 0; i < dataFileScoreList.size(); i++) {
//                sc.print(dataFileScoreList.get(i) + ", ");
//            }
//
//            sc.println();
//            sc.println();
//            sc.println();
//            sc.println();
//            outputString += "\n";
//        }
//        sc.println(outputString);
//        sc.close();
        List<String> dataFiles = Arrays
                .asList("eil51", "eil76", "kroa100", "car4", "car5", "car6", "car7", "car8", "reC01", "reC03", "reC05",
                        "reC07", "reC09", "reC11", "reC13", "reC15", "reC17", "reC19", "reC21", "reC23",
                        "reC25", "reC27", "reC29", "reC31", "reC33", "reC35", "reC37", "reC39"
                        , "reC41");

        Map<String, Integer> dataFileToOptimalScoreMap = new HashMap<String, Integer>();

        dataFileToOptimalScoreMap.put("car1", 7038);
        dataFileToOptimalScoreMap.put("car2", 7166);
        dataFileToOptimalScoreMap.put("car3", 7312);
        dataFileToOptimalScoreMap.put("car4", 8003);
        dataFileToOptimalScoreMap.put("car5", 7720);
        dataFileToOptimalScoreMap.put("car6", 8505);
        dataFileToOptimalScoreMap.put("car7", 6590);
        dataFileToOptimalScoreMap.put("car8", 8366);
        dataFileToOptimalScoreMap.put("reC01", 1247);
        dataFileToOptimalScoreMap.put("reC03", 1109);
        dataFileToOptimalScoreMap.put("reC05", 1242);
        dataFileToOptimalScoreMap.put("reC07", 1566);
        dataFileToOptimalScoreMap.put("reC09", 1537);
        dataFileToOptimalScoreMap.put("reC11", 1431);
        dataFileToOptimalScoreMap.put("reC13", 1930);
        dataFileToOptimalScoreMap.put("reC15", 1950);
        dataFileToOptimalScoreMap.put("reC17", 1902);
        dataFileToOptimalScoreMap.put("reC19", 2093);
        dataFileToOptimalScoreMap.put("reC21", 2017);
        dataFileToOptimalScoreMap.put("reC23", 2011);
        dataFileToOptimalScoreMap.put("reC25", 2513);
        dataFileToOptimalScoreMap.put("reC27", 2373);
        dataFileToOptimalScoreMap.put("reC29", 2287);
        dataFileToOptimalScoreMap.put("reC31", 3045);
        dataFileToOptimalScoreMap.put("reC33", 3114);
        dataFileToOptimalScoreMap.put("reC35", 3277);
        dataFileToOptimalScoreMap.put("reC37", 4951);
        dataFileToOptimalScoreMap.put("reC39", 5087);
        dataFileToOptimalScoreMap.put("reC41", 4960);
//                        ,
//

        String fileName = new SimpleDateFormat("'pfs_ga_'dd-MM-yy:hh-mm'.txt'").format(new Date());
        File outputFile = new File("/home/sam/Desktop/results/" + fileName);
        PrintWriter sc = new PrintWriter(new FileWriter(outputFile));

        String outputString = "";
        // Build the Solver
        for (String dataFile : dataFiles) {

            File f = new File("/home/sam/git-repos/optaplanner/optaplanner-examples/data/pfsga/input/" + dataFile + ".txt");
            System.out.println(f.getAbsolutePath());
            sc.println("Optimal Result " + dataFileToOptimalScoreMap.get(dataFile));

            outputString += dataFile;

            sc.println("GA===========================");
            List<Score> dataFileScoreList = new ArrayList<Score>();
            List<Long> dataFileConvergenceTimeList = new ArrayList<Long>();
            Score maxScore = null;
            Score minScore = null;
            double meanScore = 0;

            for (int i = 0; i < numberOfRuns; i++) {
                XmlSolverFactory solverFactory = new XmlSolverFactory();
                Solver solver = solverFactory.configure(GA_SOLVER_CONFIG).buildSolver();
                PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
                solver.setPlanningProblem(instance);
                Listener l = new Listener(solver, dataFileToOptimalScoreMap.get(dataFile));
                solver.addEventListener(l);
                solver.solve();
                solver.getTimeMillisSpend();
                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
                Score currentScore = solver.getBestSolution().getScore();
                meanScore += currentScore.toDoubleLevels()[0];
                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
                    maxScore = currentScore;
                }
                if (minScore == null || minScore.compareTo(currentScore) > 0) {
                    minScore = currentScore;
                }
                dataFileScoreList.add(currentScore);

            }

            meanScore /= numberOfRuns;

            sc.println(dataFile);
            double meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
            sc.println("Mean convergence time " + meanConvergenceTime);
            sc.println("mean score " + meanScore);
            double averageGap = calculateGap(SimpleDoubleScore.valueOf(meanScore), dataFileToOptimalScoreMap
                    .get(dataFile));
            sc.println("mean gap " + averageGap);
            sc.println("max score " + maxScore);
            sc.println("max gap " + calculateGap(maxScore, dataFileToOptimalScoreMap.get(dataFile)));
            sc.println("min score " + minScore);
            sc.println("min gap " + calculateGap(minScore, dataFileToOptimalScoreMap.get(dataFile)));

            outputString += " & " + (-1 * minScore
                    .toDoubleLevels()[0]) + " & " + (-1 * meanScore) + " & " + (-1 * maxScore
                    .toDoubleLevels()[0]) + " & " + averageGap + " & " + meanConvergenceTime;

            sc.println("Raw convergence time results: ");
            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
            }
            sc.println();
            sc.println("Raw score results: ");
            for (int i = 0; i < dataFileScoreList.size(); i++) {
                sc.print(dataFileScoreList.get(i) + ", ");
            }

            sc.println("\nLS===========================");
            dataFileScoreList = new ArrayList<Score>();
            dataFileConvergenceTimeList = new ArrayList<Long>();
            maxScore = null;
            minScore = null;
            meanScore = 0;

            for (int i = 0; i < numberOfRuns; i++) {
                XmlSolverFactory solverFactory = new XmlSolverFactory();
                Solver solver = solverFactory.configure(LS_SOLVER_CONFIG).buildSolver();
                PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
                solver.setPlanningProblem(instance);
                Listener l = new Listener(solver, dataFileToOptimalScoreMap.get(dataFile));
                solver.addEventListener(l);
                solver.solve();
                solver.getTimeMillisSpend();
                dataFileConvergenceTimeList.add(l.getConvergenceTimeMillis());
                Score currentScore = solver.getBestSolution().getScore();
                meanScore += currentScore.toDoubleLevels()[0];
                if (maxScore == null || maxScore.compareTo(currentScore) < 0) {
                    maxScore = currentScore;
                }
                if (minScore == null || minScore.compareTo(currentScore) > 0) {
                    minScore = currentScore;
                }
                dataFileScoreList.add(currentScore);

            }

            meanScore /= numberOfRuns;

            meanConvergenceTime = calculateMeanConvergenceTime(dataFileConvergenceTimeList);
            sc.println("Mean convergence time " + meanConvergenceTime);
            sc.println("mean score " + meanScore);
            averageGap = calculateGap(SimpleDoubleScore.valueOf(meanScore), dataFileToOptimalScoreMap
                    .get(dataFile));
            sc.println("mean gap " + averageGap);
            sc.println("max score " + maxScore);
            sc.println("max gap " + calculateGap(maxScore, dataFileToOptimalScoreMap.get(dataFile)));
            sc.println("min score " + minScore);
            sc.println("min gap " + calculateGap(minScore, dataFileToOptimalScoreMap.get(dataFile)));

            outputString += " & " + (-1 * minScore
                    .toDoubleLevels()[0]) + " & " + (-1 * meanScore) + " & " + (-1 * maxScore
                    .toDoubleLevels()[0]) + " & " + averageGap + " & " + meanConvergenceTime;

            sc.println("Raw convergence time results: ");
            for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
                sc.print(dataFileConvergenceTimeList.get(i) + ", ");
            }
            sc.println();
            sc.println("Raw score results: ");
            for (int i = 0; i < dataFileScoreList.size(); i++) {
                sc.print(dataFileScoreList.get(i) + ", ");
            }

            sc.println();
            sc.println();
            sc.println();
            sc.println();
            outputString += "\n";
        }
        sc.println(outputString);
        sc.close();
//
    }

    private static double calculateMeanConvergenceTime(List<Long> dataFileConvergenceTimeList) {
        double total = 0;
        for (int i = 0; i < dataFileConvergenceTimeList.size(); i++) {
            total += dataFileConvergenceTimeList.get(i);
        }
        return total / dataFileConvergenceTimeList.size();
    }

    private static double calculateGap(Score score, int optimalResult) {
        return ((score.toDoubleLevels()[0] * -1) - optimalResult) / optimalResult;
    }

    private static class Listener implements SolverEventListener {

        private final int optimalScore;
        private final Solver solver;
        private long convergenceTimeMillis;

        public Listener(Solver solver, int optimalScore) {
            this.optimalScore = optimalScore;
            this.solver = solver;
        }

        private long getConvergenceTimeMillis() {
            return convergenceTimeMillis;
        }

        @Override
        public void bestSolutionChanged(BestSolutionChangedEvent event) {
            convergenceTimeMillis = event.getTimeMillisSpend();
            if (optimalScore != -1 && (int) (event.getNewBestSolution().getScore()
                    .toDoubleLevels()[0] * -1) == optimalScore) {
                convergenceTimeMillis = event.getTimeMillisSpend();
                solver.terminateEarly();
            }
        }
    }
}
