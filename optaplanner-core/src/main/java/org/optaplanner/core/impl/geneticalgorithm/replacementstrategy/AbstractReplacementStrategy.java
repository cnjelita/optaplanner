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

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.optaplanner.core.impl.geneticalgorithm.ScoreDirectorComparator;
import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractReplacementStrategy extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
		ReplacementStrategy {

	protected Comparator<ScoreDirector> scoreDirectorComparator;
	protected int populationSize;
	protected int elitistSize;
	protected Random workingRandom;

	@Override
	public abstract void createNewGeneration(GeneticAlgorithmStepScope stepScope);

	@Override
	public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
		super.phaseStarted(phaseScope);
		scoreDirectorComparator = Collections.reverseOrder(new ScoreDirectorComparator());
		populationSize = phaseScope.getPopulationSize();
		elitistSize = phaseScope.getElitistSize();
		workingRandom = phaseScope.getWorkingRandom();
	}
}
