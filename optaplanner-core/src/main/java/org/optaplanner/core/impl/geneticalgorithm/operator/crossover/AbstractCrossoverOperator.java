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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

	protected Random workingRandom;
	protected List<Class<?>> entityClassList;
	protected Map<Class<?>, Collection<PlanningVariableDescriptor>> entityClassToVariableDescriptorMap;
	protected int entityListClassSize;

	@Override
	public abstract void performCrossover(GeneticAlgorithmStepScope stepScope);

	@Override
	public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
		super.phaseStarted(phaseScope);
		workingRandom = phaseScope.getWorkingRandom();
		SolutionDescriptor solutionDescriptor = phaseScope.getSolutionDescriptor();
		entityClassList = new ArrayList<Class<?>>();
		Set<Class<?>> entityClassSet = solutionDescriptor.getPlanningEntityClassSet();
		entityListClassSize = entityClassSet.size();
		entityClassToVariableDescriptorMap = new HashMap<Class<?>, Collection<PlanningVariableDescriptor>>();
		for (Class<?> entityClass : entityClassSet) {
			entityClassList.add(entityClass);
			//TODO check for chaining!
			entityClassToVariableDescriptorMap.put(entityClass,
					solutionDescriptor.getPlanningEntityDescriptor(entityClass).getPlanningVariableDescriptors());
		}
	}

	protected void swapValues(Collection<PlanningVariableDescriptor> planningVariableDescriptors, Object leftEntity,
			ScoreDirector leftScoreDirector, Object rightEntity, ScoreDirector rightScoreDirector) {
		Move leftMove = new SwapMove(planningVariableDescriptors, leftEntity, rightEntity);
		if (leftMove.isMoveDoable(leftScoreDirector)) {
			leftMove.doMove(leftScoreDirector);
		}
		Move rightMove = new SwapMove(planningVariableDescriptors, rightEntity, leftEntity);
		if (rightMove.isMoveDoable(rightScoreDirector)) {
			rightMove.doMove(rightScoreDirector);
		}
	}
}
