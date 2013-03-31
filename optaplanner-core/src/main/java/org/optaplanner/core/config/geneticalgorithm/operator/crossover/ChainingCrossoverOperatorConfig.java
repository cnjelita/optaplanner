package org.optaplanner.core.config.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractChainingCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.CrossoverOperator;

public abstract class ChainingCrossoverOperatorConfig extends CrossoverOperatorConfig {

	private Class planningEntityClass = null;

	@Override
	public List<CrossoverOperator> buildCrossoverOperator(Double alternativeCrossoverRate,
			SolutionDescriptor solutionDescriptor) {
		List<CrossoverOperator> crossoverOperatorList = new ArrayList<CrossoverOperator>();
		List<PlanningVariableDescriptor> planningVariableDescriptors;
		if (planningEntityClass != null) {
			planningVariableDescriptors =
					getPlanningVariableDescriptors(planningEntityClass, solutionDescriptor);
			List<PlanningVariableDescriptor> chainedVariableDescriptors =
					getChainedVariableDescriptors(planningVariableDescriptors);
			if (chainedVariableDescriptors.isEmpty()) {
				throw new IllegalArgumentException(
						this.getClass() + " only works for entities which are chained, no entities with " +
								"chained variables were found.");
			}
			for (PlanningVariableDescriptor planningVariableDescriptor : chainedVariableDescriptors) {
				AbstractChainingCrossoverOperator crossoverOperator = createInstance();
				crossoverOperator.setEntityClass(planningEntityClass);
				crossoverOperator.setPlanningVariableDescriptors(planningVariableDescriptors);
				crossoverOperator.setChainedPlanningVariableDescriptor(planningVariableDescriptor);
				setCrossoverRate(crossoverOperator, alternativeCrossoverRate);
				crossoverOperatorList.add(crossoverOperator);
			}
		} else {
			List<Class> planningEntityClassList = new ArrayList<Class>(solutionDescriptor.getPlanningEntityClassSet());
			for (Class planningEntityClass : planningEntityClassList) {
				planningVariableDescriptors = getPlanningVariableDescriptors(planningEntityClass, solutionDescriptor);
				List<PlanningVariableDescriptor> chainedVariableDescriptors =
						getChainedVariableDescriptors(planningVariableDescriptors);
				if (!chainedVariableDescriptors.isEmpty()) {
					for (PlanningVariableDescriptor planningVariableDescriptor : chainedVariableDescriptors) {
						AbstractChainingCrossoverOperator crossoverOperator = createInstance();
						crossoverOperator.setEntityClass(planningEntityClass);
						crossoverOperator.setPlanningVariableDescriptors(planningVariableDescriptors);
						crossoverOperator.setChainedPlanningVariableDescriptor(planningVariableDescriptor);
						setCrossoverRate(crossoverOperator, alternativeCrossoverRate);
						crossoverOperatorList.add(crossoverOperator);
					}
				}
			}
			if (crossoverOperatorList.isEmpty()) {
				throw new IllegalArgumentException(
						this.getClass() + " only works for entities which are chained, no entities with " +
								"chained variables were found.");
			}
		}
		return crossoverOperatorList;
	}

	public abstract AbstractChainingCrossoverOperator createInstance();
}
