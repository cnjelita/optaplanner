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

package org.optaplanner.core.geneticalgorithm.scope;

import org.optaplanner.core.geneticalgorithm.Population;
import org.optaplanner.core.phase.AbstractSolverPhaseScope;
import org.optaplanner.core.phase.step.AbstractStepScope;
import org.optaplanner.core.score.Score;
import org.optaplanner.core.solution.Solution;
import org.optaplanner.core.solver.scope.DefaultSolverScope;

public class GeneticAlgorithmSolverPhaseScope extends AbstractSolverPhaseScope {

    private Population generation;
    private int populationSize;
    private GeneticAlgorithmStepScope lastCompletedStepScope;

    public GeneticAlgorithmSolverPhaseScope(DefaultSolverScope solverScope, int populationSize) {
        //TODO make complete
        super(solverScope);
        this.populationSize = populationSize;
    }

    //TODO why should this method be implemented for all phases? Useless for genetic algorithm
    //Seems like it's used to assert undoMoves.
    @Override
    public AbstractStepScope getLastCompletedStepScope() {
        return lastCompletedStepScope;
    }

    //TODO overridden because might be used to calculate population fitness
    @Override
    public Score calculateScore() {
        return null; //TODO return something
    }

    public Population getGeneration() {
        return generation;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setNewGeneration(Population newGeneration) {
        this.generation = newGeneration;
//        //FIXME necessary because bestsolutionrecaller requires best solution to be in workingSolution
//        if (generation.getBestIndividual() != null) {
//            getScoreDirector().setWorkingSolution(generation.getBestIndividual().getWorkingSolution());
//            getScoreDirector().calculateScore();
//        }
    }

    @Override
    public Score getBestScore() {
        return solverScope.getBestScore();
    }

    public Solution getBestIndividual() {
        return generation.getBestIndividual().getWorkingSolution();
    }

    public Score getBestIndividualScore() {
        return generation.getBestIndividual().getWorkingSolution().getScore();
    }

    public void setLastCompletedStepScope(GeneticAlgorithmStepScope lastCompletedStepScope) {
        this.lastCompletedStepScope = lastCompletedStepScope;
    }
}
