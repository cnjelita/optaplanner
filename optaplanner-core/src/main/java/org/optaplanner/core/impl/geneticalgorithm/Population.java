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

package org.optaplanner.core.impl.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.optaplanner.core.impl.score.director.ScoreDirector;

//TODO should be in other package?
public class Population implements Iterable<ScoreDirector> {

	private List<ScoreDirector> individuals;
	private int populationSize;
	private ScoreDirector bestIndividual;

	public Population(int populationSize) {
		this.populationSize = populationSize;
		individuals = new ArrayList<ScoreDirector>(populationSize);
	}

	@Override
	public Iterator<ScoreDirector> iterator() {
		return individuals.iterator();
	}

	public void addIndividual(ScoreDirector scoreDirector) {
		individuals.add(scoreDirector);
	}

	public List<ScoreDirector> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<ScoreDirector> individuals) {
		this.individuals = individuals;
	}

	public void performScoreCalculation() {
		for (ScoreDirector individual : individuals) {
			individual.calculateScore();
		}
//        for (ScoreDirector individual : individuals){
//            System.out.println(individual.getWorkingSolution().getScore());
//        }
		//TODO calculate other statistics
	}

	public void setBestIndividual(ScoreDirector bestIndividual) {
		this.bestIndividual = bestIndividual;
	}

	public ScoreDirector getBestIndividual() {
		return bestIndividual;
	}

	public void sort() {
		Collections.sort(individuals, Collections.reverseOrder(new ScoreDirectorComparator()));
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < populationSize; i++) {
			buffer.append(individuals.get(i).getWorkingSolution().getScore()).append("\n");
		}
		return buffer.toString();
	}

	//TODO this only works for maximization problems
	public double[][] calculatePopulationParameters() {
		double[] worstScorePerLevel = null;
		double[] bestScorePerLevel = null;
		for (ScoreDirector individual : individuals) {
			double[] scoreDoubleLevels = individual.getWorkingSolution().getScore().toDoubleLevels();
			if (bestScorePerLevel == null) {
				bestScorePerLevel = new double[scoreDoubleLevels.length];
				for (int i = 0; i < bestScorePerLevel.length; i++) {
					bestScorePerLevel[i] = -Double.MAX_VALUE;
				}
			}
			if (worstScorePerLevel == null) {
				worstScorePerLevel = new double[scoreDoubleLevels.length];
			}
			for (int i = 0; i < scoreDoubleLevels.length; i++) {
				if (worstScorePerLevel[i] > scoreDoubleLevels[i]) {
					worstScorePerLevel[i] = scoreDoubleLevels[i];
				}
				if (bestScorePerLevel[i] < scoreDoubleLevels[i]) {
					bestScorePerLevel[i] = scoreDoubleLevels[i];
				}
			}
		}
		double[] weightPerScoreLevel = new double[worstScorePerLevel.length];
		weightPerScoreLevel[0] = 1;
		for (int i = 1; i < worstScorePerLevel.length; i++) {
			weightPerScoreLevel[i] =
					(bestScorePerLevel[i - 1] - worstScorePerLevel[i - 1]) * weightPerScoreLevel[i - 1] + 1;
		}
		return new double[][]{weightPerScoreLevel, worstScorePerLevel};
	}
}
