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

package org.optaplanner.core.impl.geneticalgorithm.scope;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.phase.step.AbstractStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solution.Solution;

public class GeneticAlgorithmStepScope extends AbstractStepScope {

    private final GeneticAlgorithmSolverPhaseScope phaseScope;
    private Population intermediatePopulation;
    private int intermediatePopulationSize;

    public GeneticAlgorithmStepScope(GeneticAlgorithmSolverPhaseScope phaseScope) {
        this.phaseScope = phaseScope;
    }

    @Override
    public GeneticAlgorithmSolverPhaseScope getPhaseScope() {
        return phaseScope;
    }

    //TODO what is this for?
    @Override
    public boolean isBestSolutionCloningDelayed() {
        return false;
    }

    //TODO what is this for?
    @Override
    public int getUninitializedVariableCount() {
        return 0;
    }

    public Population getIntermediatePopulation() {
        return intermediatePopulation;
    }

    //TODO should ..OnIntermediatePopulation be added to method name?
    public void performScoreCalculation() {
        intermediatePopulation.performScoreCalculation();
    }

    public Population getCurrentGeneration() {
        return phaseScope.getGeneration();
    }

    public void setNewGeneration(Population newGeneration) {
        phaseScope.setNewGeneration(newGeneration);
    }

    public void setIntermediatePopulation(Population intermediatePopulation) {
        this.intermediatePopulation = intermediatePopulation;
    }

    public void setIntermediatePopulationSize(int intermediatePopulationSize) {
        this.intermediatePopulationSize = intermediatePopulationSize;
    }

    public int getIntermediatePopulationSize() {
        return intermediatePopulationSize;
    }

    public Score getScore() {
        return phaseScope.getBestIndividualScore();
    }

    public Solution createOrGetClonedSolution() {
        return phaseScope.getBestIndividual();
    }

    public void updateCalculateCount() {
        long calculateCount = 0;
        //TODO HACK in order to keep track of calculatecount
        for (ScoreDirector individual : intermediatePopulation) {
            calculateCount += individual.getCalculateCount();
        }
        phaseScope.addToCalculateCount(calculateCount);
    }
}
