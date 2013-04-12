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

import java.util.TreeMap;

import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractFitnessProportionateSelector extends AbstractSolutionSelector {

	protected TreeMap<Double, ScoreDirector> fitnessMap;
	protected double totalProbability;

	@Override
	public void stepStarted(GeneticAlgorithmStepScope stepScope) {
		Population generation = stepScope.getCurrentGeneration();
		double[][] resultingParameters = generation.calculatePopulationParameters();
		double[] weightsPerScoreLevel = resultingParameters[0];
		double[] worstPerScoreLevel = resultingParameters[1];
		fitnessMap = new TreeMap<Double, ScoreDirector>();
		for (ScoreDirector individual : generation) {
            double[] scoreDoubleLevels = individual.getWorkingSolution().getScore().toDoubleLevels();
			double flattenedScore = 0;
			for (int i = 0; i < scoreDoubleLevels.length; i++) {
				flattenedScore += (scoreDoubleLevels[i] - worstPerScoreLevel[i]) * weightsPerScoreLevel[i];
			}
			totalProbability += flattenedScore;
			fitnessMap.put(totalProbability, individual);
		}
	}
}
