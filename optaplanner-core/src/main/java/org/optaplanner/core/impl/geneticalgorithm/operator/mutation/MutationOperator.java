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

package org.optaplanner.core.impl.geneticalgorithm.operator.mutation;

import java.util.Iterator;
import java.util.Random;

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

//TODO Should this be called DefaultMutationOperator?
public class MutationOperator extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter {

    private MoveSelector moveSelector;
    private int mutationRate;
    private Random workingRandom;

    public void setMoveSelector(MoveSelector moveSelector) {
        this.moveSelector = moveSelector;
    }

    public MoveSelector getMoveSelector() {
        return moveSelector;
    }

    public void performMutation(GeneticAlgorithmStepScope stepScope) {

        Population population = stepScope.getIntermediatePopulation();
        for (ScoreDirector scoreDirector : population) {
            //TODO make moveselectors more population friendly so there's need to do the two things below
            stepScope.getPhaseScope().getSolverScope().setScoreDirector(scoreDirector);

            int mutationCount = 0;
            int numberOfMutations = mutationRate;

            while (mutationCount < numberOfMutations) {
                moveSelector.stepStarted(stepScope);
                Iterator<Move> moveIterator = moveSelector.iterator();
                boolean foundDoableMove = false;
                Move move = null;
                while (moveIterator.hasNext() && !foundDoableMove) {
                    move = moveIterator.next();
                    foundDoableMove = move.isMoveDoable(scoreDirector);
                }
                if (move != null) {
                    move.doMove(scoreDirector);
                }
                mutationCount++;
            }
        }
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        moveSelector.phaseStarted(phaseScope);
        workingRandom = phaseScope.getWorkingRandom();
    }

    @Override
    public void solvingStarted(DefaultSolverScope solverScope) {
        super.solvingStarted(solverScope);
        moveSelector.solvingStarted(solverScope);
    }

    public void setMutationRate(int mutationRate) {
        this.mutationRate = mutationRate;
    }
}
