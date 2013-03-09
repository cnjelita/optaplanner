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

package org.optaplanner.core.geneticalgorithm;

import org.optaplanner.core.geneticalgorithm.operator.crossover.CrossoverOperator;
import org.optaplanner.core.geneticalgorithm.operator.mutation.MutationOperator;
import org.optaplanner.core.geneticalgorithm.operator.selector.SolutionSelector;
import org.optaplanner.core.phase.AbstractSolverPhase;
import org.optaplanner.core.solver.scope.DefaultSolverScope;

public class GeneticAlgorithmSolverPhase extends AbstractSolverPhase
        implements GeneticAlgorithmSolverPhaseLifeCycleListener {

    private SolutionSelector solutionSelector;
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;
    private int populationSize;
    private boolean assertStepScoreIsUncorrupted;

    public void setSolutionSelector(SolutionSelector solutionSelector) {
        this.solutionSelector = solutionSelector;
    }

    public SolutionSelector getSolutionSelector() {
        return solutionSelector;
    }

    public void setCrossoverOperator(CrossoverOperator crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    public CrossoverOperator getCrossoverOperator() {
        return crossoverOperator;
    }

    public void setMutationOperator(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public MutationOperator getMutationOperator() {
        return mutationOperator;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setAssertStepScoreIsUncorrupted(boolean assertStepScoreIsUncorrupted) {
        this.assertStepScoreIsUncorrupted = assertStepScoreIsUncorrupted;
    }

    public boolean isAssertStepScoreIsUncorrupted() {
        return assertStepScoreIsUncorrupted;
    }

    @Override
    public void solve(DefaultSolverScope solverScope) {
        //TODO implement solve method for genetic algorithm

        //TODO Make geneticAlgorithmPhaseScope
        //TODO phaseStarted - Tell operators phase started

        //TODO initialize population

        //TODO make step scope

        /*TODO perform loop as long as not terminated
         *
         *
         *     assess fitness of individuals
         *     use selector for parent selection
         *     perform crossover on parents to form children
         *     perform mutation on children
         *     perform replacement of individuals from new population and old one
         *
         *     step ended - look for best individual, create new stepscope
         */

        //TODO phaseEnded
    }
}
