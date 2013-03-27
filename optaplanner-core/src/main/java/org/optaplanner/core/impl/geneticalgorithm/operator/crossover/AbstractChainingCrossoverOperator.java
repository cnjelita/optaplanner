package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractChainingCrossoverOperator extends
		GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
		CrossoverOperator {

	protected Random workingRandom;
	protected SolutionDescriptor solutionDescriptor;
	protected ArrayList<Class<?>> entityClassList;
	protected int entityListClassSize;
	protected HashMap<Class<?>, Collection<PlanningVariableDescriptor>> entityClassToVariableDescriptorMap;

	@Override
	public abstract void performCrossover(GeneticAlgorithmStepScope stepScope);

	@Override
	public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
		super.phaseStarted(phaseScope);
		workingRandom = phaseScope.getWorkingRandom();
		solutionDescriptor = phaseScope.getSolutionDescriptor();
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

	protected List<Object> getOrderedList(Object planningEntity, ScoreDirector scoreDirector,
			PlanningVariableDescriptor chainedVariableDescriptor) {
		List<Object> orderedList = new ArrayList<Object>();
		PlanningEntityDescriptor entityDescriptor = chainedVariableDescriptor.getPlanningEntityDescriptor();
		while (scoreDirector.getTrailingEntity(chainedVariableDescriptor, planningEntity) != null) {
			planningEntity = scoreDirector.getTrailingEntity(chainedVariableDescriptor, planningEntity);
		}
		while (planningEntity != null && entityDescriptor.appliesToPlanningEntity(planningEntity)) {
			orderedList.add(planningEntity);
			planningEntity = chainedVariableDescriptor.getValue(planningEntity);
		}
		Collections.reverse(orderedList);
		return orderedList;
	}
}
