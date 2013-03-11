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

package org.optaplanner.core.geneticalgorithm.replacementstrategy;

import java.util.Collections;
import java.util.List;

import org.optaplanner.core.geneticalgorithm.Population;
import org.optaplanner.core.geneticalgorithm.ScoreDirectorComparator;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.score.director.ScoreDirector;

public class KeepBestStrategy extends AbstractReplacementStrategy {

    @Override
    public void createNewGeneration(GeneticAlgorithmStepScope stepScope) {

        stepScope.performScoreCalculation();
        stepScope.getIntermediatePopulation().sort();
//        System.out.println("currgen");
//        System.out.println(stepScope.getCurrentGeneration());
//        System.out.println("intermediate");
//        System.out.println(stepScope.getIntermediatePopulation());
        List<ScoreDirector> intermediatePopulation = stepScope.getIntermediatePopulation().getIndividuals();
        List<ScoreDirector> currentGeneration = stepScope.getCurrentGeneration().getIndividuals();

        Population newGeneration = new Population(populationSize);

        int intermediateListIndex = 0;
        int generationListIndex = 0;

        ScoreDirector intermediateIndividual = intermediatePopulation.get(intermediateListIndex);
        ScoreDirector generationIndividual = currentGeneration.get(generationListIndex);

        ScoreDirector bestIndividual =
                scoreDirectorComparator.compare(intermediateIndividual, generationIndividual) < 0 ?
                        intermediateIndividual : generationIndividual;
        newGeneration.setBestIndividual(bestIndividual);

        for (int i = 0; i < elitistSize; i++) {
            newGeneration.addIndividual(currentGeneration.get(generationListIndex));
            generationListIndex++;
        }
        for (int i = 0; i < populationSize - elitistSize; i++) {
            intermediateIndividual = intermediatePopulation.get(intermediateListIndex);
            generationIndividual = currentGeneration.get(generationListIndex);
            //TODO does this method sort from worst to best?
            if (scoreDirectorComparator.compare(intermediateIndividual, generationIndividual) < 0) {
                newGeneration.addIndividual(intermediateIndividual);
                intermediateListIndex++;
            } else {
                newGeneration.addIndividual(generationIndividual);
                generationListIndex++;
            }
        }

        if (elitistSize > 0) {
            newGeneration.sort();
        }
//        System.out.println("new");
//        System.out.println(newGeneration);
        stepScope.setNewGeneration(newGeneration);

    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        scoreDirectorComparator = Collections.reverseOrder(new ScoreDirectorComparator());
        populationSize = phaseScope.getPopulationSize();
        elitistSize = phaseScope.getElitistSize();
    }
}
