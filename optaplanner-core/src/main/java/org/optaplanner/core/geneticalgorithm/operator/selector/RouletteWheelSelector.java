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

package org.optaplanner.core.geneticalgorithm.operator.selector;

import org.optaplanner.core.geneticalgorithm.Population;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.util.RandomUtils;

public class RouletteWheelSelector extends AbstractFitnessProportionateSelector {

    @Override
    public void selectParents(GeneticAlgorithmStepScope stepScope) {
        int intermediatePopulationSize = populationSize % 2 == 0 ? populationSize : populationSize + 1;
        Population intermediatePopulation = new Population(intermediatePopulationSize);
        for (int i = 0; i < intermediatePopulationSize; i++) {
            double randomOffset = RandomUtils.nextDouble(workingRandom, totalProbability);
            intermediatePopulation.addIndividual(fitnessMap.ceilingEntry(randomOffset).getValue().clone());
        }
        stepScope.setIntermediatePopulation(intermediatePopulation);
        stepScope.setIntermediatePopulationSize(intermediatePopulationSize);

    }
}
