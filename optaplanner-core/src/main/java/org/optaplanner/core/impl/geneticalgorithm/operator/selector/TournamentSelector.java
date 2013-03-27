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

import java.util.Collections;
import java.util.List;

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.ScoreDirectorComparator;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class TournamentSelector extends AbstractSolutionSelector {

	private int tournamentSize;

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
		//if parentSize is not even we have to do strange tricks during crossover
		int intermediatePopulationSize = populationSize % 2 == 0 ? populationSize : populationSize + 1;

		for (int i = 0; i < intermediatePopulationSize; i++) {
			int leftIndex = workingRandom.nextInt(populationSize);
			ScoreDirector leftIndividual = generation.get(leftIndex);
			for (int j = 0; j < tournamentSize; j++) {
				int rightIndex;
				do {
					rightIndex = workingRandom.nextInt(populationSize);
				} while (rightIndex == leftIndex);
				ScoreDirector rightIndividual = generation.get(rightIndex);
				//FIXME how does scoredirectorcomparator work?
				if (scoreDirectorComparator.compare(leftIndividual, rightIndividual) > 0) {
//                    System.out.println(rightIndividual.getWorkingSolution().getScore() + " better than" +
//                            leftIndividual.getWorkingSolution().getScore());
					leftIndividual = rightIndividual;
					leftIndex = rightIndex;
				}
			}
//            if (selectedIndices.contains(leftIndividual)) {
			parents.addIndividual(leftIndividual.clone());
//            } else {
//                parents.addIndividual(leftIndividual);
//                selectedIndices.add(leftIndividual);
//            }
		}

		stepScope.setIntermediatePopulation(parents);
		stepScope.setIntermediatePopulationSize(intermediatePopulationSize);
	}

}
