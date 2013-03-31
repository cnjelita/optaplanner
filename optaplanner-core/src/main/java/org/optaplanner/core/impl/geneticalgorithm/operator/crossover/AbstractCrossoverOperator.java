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

package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractCrossoverOperator extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
		CrossoverOperator {

	protected double crossoverRate;
	protected Class entityClass;
	protected List<PlanningVariableDescriptor> planningVariableDescriptors;
	protected Random workingRandom;
	protected SolutionDescriptor solutionDescriptor;

	@Override
	public final void performCrossover(GeneticAlgorithmStepScope stepScope) {
		List<ScoreDirector> individuals = stepScope.getIntermediatePopulation().getIndividuals();
		Collections.shuffle(individuals, workingRandom);
		int populationSize = stepScope.getIntermediatePopulationSize();
		for (int i = 0; i < populationSize / 2; i += 2) {
			if (workingRandom.nextDouble() < crossoverRate) {
				performCrossover(individuals.get(i), individuals.get(i + 1));
			}
		}
	}

	protected abstract void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector);

	@Override
	public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
		super.phaseStarted(phaseScope);
		workingRandom = phaseScope.getWorkingRandom();
		solutionDescriptor = phaseScope.getSolutionDescriptor();
	}

	protected void swapValues(Object leftEntity, ScoreDirector leftScoreDirector, Object rightEntity,
			ScoreDirector rightScoreDirector) {
		Move leftMove = new SwapMove(planningVariableDescriptors, leftEntity, rightEntity);
		if (leftMove.isMoveDoable(leftScoreDirector)) {
			leftMove.doMove(leftScoreDirector);
		}
		Move rightMove = new SwapMove(planningVariableDescriptors, rightEntity, leftEntity);
		if (rightMove.isMoveDoable(rightScoreDirector)) {
			rightMove.doMove(rightScoreDirector);
		}
	}

	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	public void setPlanningVariableDescriptors(List<PlanningVariableDescriptor> planningVariableDescriptors) {
		this.planningVariableDescriptors = planningVariableDescriptors;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
}
