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

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.util.RandomUtils;

public class StochasticUniversalSamplingSelector extends AbstractFitnessProportionateSelector {

    @Override
    public void selectParents(GeneticAlgorithmStepScope stepScope) {
        int intermediatePopulationSize = populationSize % 2 == 0 ? populationSize : populationSize + 1;
        double offset = totalProbability / intermediatePopulationSize;
        Population intermediatePopulation = new Population(intermediatePopulationSize);
        double nextOffset = RandomUtils.nextDouble(workingRandom, offset);
        for (int i = 0; i < intermediatePopulationSize; i++) {
            ScoreDirector nextIndividual = fitnessMap.ceilingEntry(nextOffset).getValue().clone();
//            System.out.println(nextIndividual.getWorkingSolution().getScore());
            intermediatePopulation.addIndividual(nextIndividual);
            nextOffset += offset;
        }
        stepScope.setIntermediatePopulation(intermediatePopulation);
        stepScope.setIntermediatePopulationSize(intermediatePopulationSize);

    }
}
