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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.optaplanner.core.geneticalgorithm.Population;
import org.optaplanner.core.geneticalgorithm.ScoreDirectorComparator;
import org.optaplanner.core.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.score.director.ScoreDirector;

public class TournamentSelector extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
        SolutionSelector {

    private int tournamentSize;
    private Comparator<ScoreDirector> scoreDirectorComparator;
    private int populationSize;
    private Random workingRandom;

    public TournamentSelector() {
        scoreDirectorComparator = Collections.reverseOrder(new ScoreDirectorComparator());
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    @Override
    public void selectParents(GeneticAlgorithmStepScope stepScope) {
        List<ScoreDirector> generation = stepScope.getCurrentGeneration().getIndividuals();
        Population parents = new Population(populationSize);
        Set<Integer> selectedIndices = new HashSet<Integer>();

        for (int i = 0; i < populationSize; i++) {
            int leftIndex = workingRandom.nextInt(populationSize);
            ScoreDirector leftIndividual = generation.get(leftIndex);
            for (int j = 0; j < tournamentSize; j++) {
                int rightIndex;
                do {
                    rightIndex = workingRandom.nextInt(populationSize);
                } while (rightIndex == leftIndex);
                ScoreDirector rightIndividual = generation.get(rightIndex);
                //FIXME how does scoredirectorcomparator work?
                if (scoreDirectorComparator.compare(leftIndividual, rightIndividual) < 0) {
                    leftIndividual = rightIndividual;
                    leftIndex = rightIndex;
                }
            }
            if (selectedIndices.contains(leftIndex)) {
                parents.addIndividual(leftIndividual.clone());
            } else {
                parents.addIndividual(leftIndividual);
                selectedIndices.add(leftIndex);
            }
        }

        stepScope.setIntermediatePopulation(parents);
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        populationSize = phaseScope.getPopulationSize();
        workingRandom = phaseScope.getWorkingRandom();
    }
}
