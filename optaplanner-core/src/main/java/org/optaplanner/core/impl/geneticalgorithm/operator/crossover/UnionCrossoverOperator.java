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

package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;

public class UnionCrossoverOperator extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
        CrossoverOperator {

    private List<CrossoverOperator> crossoverOperatorList;

    public UnionCrossoverOperator() {
        crossoverOperatorList = new ArrayList<CrossoverOperator>();
    }

    @Override
    public void performCrossover(GeneticAlgorithmStepScope stepScope) {
        crossoverOperatorList.get(stepScope.getWorkingRandom().nextInt(crossoverOperatorList.size()))
                .performCrossover(stepScope);
    }

    @Override
    public void setCrossoverRate(double crossoverRate) {
        //TODO implement
    }

    public void addCrossoverOperator(CrossoverOperator crossoverOperator) {
        crossoverOperatorList.add(crossoverOperator);
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        for (CrossoverOperator crossoverOperator : crossoverOperatorList) {
            crossoverOperator.phaseStarted(phaseScope);
        }
    }

    @Override
    public void phaseEnded(GeneticAlgorithmSolverPhaseScope phaseScope) {
        for (CrossoverOperator crossoverOperator : crossoverOperatorList) {
            crossoverOperator.phaseEnded(phaseScope);
        }
    }

    @Override
    public void stepStarted(GeneticAlgorithmStepScope stepScope) {
        for (CrossoverOperator crossoverOperator : crossoverOperatorList) {
            crossoverOperator.stepStarted(stepScope);
        }
    }

    @Override
    public void stepEnded(GeneticAlgorithmStepScope stepScope) {
        for (CrossoverOperator crossoverOperator : crossoverOperatorList) {
            crossoverOperator.stepEnded(stepScope);
        }
    }
}
