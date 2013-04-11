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

package org.optaplanner.core.impl.geneticalgorithm.replacementstrategy;

import java.util.List;

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class KeepNewStrategy extends AbstractReplacementStrategy {

    @Override
    public void createNewGeneration(GeneticAlgorithmStepScope stepScope) {
        stepScope.performScoreCalculation();
        stepScope.getIntermediatePopulation().sort();
        List<ScoreDirector> intermediatePopulation = stepScope.getIntermediatePopulation().getIndividuals();
        List<ScoreDirector> currentGeneration = stepScope.getCurrentGeneration().getIndividuals();

        Population newGeneration = new Population(populationSize);
        ScoreDirector intermediateIndividual = intermediatePopulation.get(0);
        ScoreDirector generationIndividual = currentGeneration.get(0);

        ScoreDirector bestIndividual =
                scoreDirectorComparator.compare(intermediateIndividual, generationIndividual) < 0 ?
                        intermediateIndividual : generationIndividual;
        newGeneration.setBestIndividual(bestIndividual);

        int numberOfNewIndividuals = (int) (populationSize * diversityRate);

        for (int i = 0; i < elitistSize; i++) {
            newGeneration.addIndividual(currentGeneration.get(i));
        }
        for (int i = 0; i < populationSize - elitistSize; i++) {
            if (workingRandom.nextDouble() < diversityRate) {
                newGeneration
                        .addIndividual(initializer.diversifyIndividual(intermediatePopulation.get(i), diversityRate));
            } else {
                newGeneration.addIndividual(intermediatePopulation.get(i));
            }
        }
        newGeneration.sort();
        stepScope.setNewGeneration(newGeneration);
    }

}
