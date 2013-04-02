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

package org.optaplanner.core.impl.geneticalgorithm.operator.selector;

import java.util.List;

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class TruncationSelector extends AbstractSolutionSelector {

    private double threshold;
    private int numberOfParents;

    public TruncationSelector(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void selectParents(GeneticAlgorithmStepScope stepScope) {
        List<ScoreDirector> generation = stepScope.getCurrentGeneration().getIndividuals();
        Population children = new Population(populationSize);
        int intermediatePopulationSize = populationSize % 2 == 0 ? populationSize : populationSize + 1;

        int count = 0;
        int parentIndex = 0;
        while (count != intermediatePopulationSize) {
            children.addIndividual(generation.get(parentIndex).clone());
            parentIndex = (parentIndex + 1) % numberOfParents;
            count++;
        }

        stepScope.setIntermediatePopulation(children);
        stepScope.setIntermediatePopulationSize(intermediatePopulationSize);
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        numberOfParents = (int) (populationSize * threshold);
    }
}
