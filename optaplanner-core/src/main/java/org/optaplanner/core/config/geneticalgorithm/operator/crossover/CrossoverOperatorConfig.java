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

package org.optaplanner.core.config.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamInclude;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.CrossoverOperator;

@XStreamInclude({
		OnePointCrossoverConfig.class,
		TwoPointCrossoverConfig.class,
		OnePointCrossoverConfig.class,
		TwoPointCrossoverConfig.class,
		UniformCrossoverConfig.class,
		UniformOrderCrossoverConfig.class,
		CycleCrossoverConfig.class,
		PartiallyMatchedCrossoverConfig.class
})
public abstract class CrossoverOperatorConfig {

	protected Double crossoverRate = null;

	private Class planningEntityClass = null;

	public List<CrossoverOperator> buildCrossoverOperator(Double alternativeCrossoverRate,
			SolutionDescriptor solutionDescriptor) {
		List<CrossoverOperator> crossoverOperatorList = new ArrayList<CrossoverOperator>();
		List<PlanningVariableDescriptor> planningVariableDescriptors;
		if (planningEntityClass != null) {
			planningVariableDescriptors =
					getPlanningVariableDescriptors(planningEntityClass, solutionDescriptor);
			List<PlanningVariableDescriptor> nonChainedVariableDescriptors = getNonChainedVariableDescriptors(
					planningVariableDescriptors);
			if (nonChainedVariableDescriptors.isEmpty()) {
				throw new IllegalArgumentException(
						this.getClass().getSimpleName() + " only works for entities which have variables which " +
								"are not chained, none of those exist for provided class.");
			}
			AbstractCrossoverOperator crossoverOperator = createInstance();
			crossoverOperator.setEntityClass(planningEntityClass);
			crossoverOperator.setPlanningVariableDescriptors(planningVariableDescriptors);
			setCrossoverRate(crossoverOperator, alternativeCrossoverRate);
			crossoverOperatorList.add(crossoverOperator);
		} else {
			List<Class> planningEntityClassList = new ArrayList<Class>(solutionDescriptor.getPlanningEntityClassSet());
			for (Class planningEntityClass : planningEntityClassList) {
				planningVariableDescriptors = getPlanningVariableDescriptors(planningEntityClass, solutionDescriptor);
				List<PlanningVariableDescriptor> nonChainedVariableDescriptors = getNonChainedVariableDescriptors(
						planningVariableDescriptors);
				if (!nonChainedVariableDescriptors.isEmpty()) {
					AbstractCrossoverOperator crossoverOperator = createInstance();
					crossoverOperator.setEntityClass(planningEntityClass);
					crossoverOperator.setPlanningVariableDescriptors(planningVariableDescriptors);
					setCrossoverRate(crossoverOperator, alternativeCrossoverRate);
					crossoverOperatorList.add(crossoverOperator);
				}
			}
			if (crossoverOperatorList.isEmpty()) {
				throw new IllegalArgumentException(
						"All planning variables for the given domain are chained. " +
								this.getClass().getSimpleName() + " is not suitable for your domain.");
			}
		}
		return crossoverOperatorList;
	}

	protected abstract AbstractCrossoverOperator createInstance();

	protected void setCrossoverRate(CrossoverOperator crossoverOperator, Double alternativeCrossoverRate) {
		if (crossoverRate != null && (crossoverRate < 0 || crossoverRate > 1)) {
			throw new IllegalArgumentException(
					"Crossover rate should be larger than 0 and smaller than or equal to 1");
		} else if (crossoverRate == null && alternativeCrossoverRate != null) {
			crossoverOperator.setCrossoverRate(alternativeCrossoverRate);
		} else if (crossoverRate != null) {
			crossoverOperator.setCrossoverRate(crossoverRate);
		} else {
			crossoverOperator.setCrossoverRate(1);
		}
	}

	protected List<PlanningVariableDescriptor> getPlanningVariableDescriptors(Class planningEntityClass,
			SolutionDescriptor solutionDescriptor) {
		List<PlanningVariableDescriptor> planningVariableDescriptors;
		if (solutionDescriptor.getPlanningEntityClassSet().contains(planningEntityClass)) {
			planningVariableDescriptors = new ArrayList<PlanningVariableDescriptor>(
					solutionDescriptor.getPlanningEntityDescriptor(
							planningEntityClass).getPlanningVariableDescriptors());
		} else {
			throw new IllegalArgumentException("The planning entity class " + planningEntityClass.getSimpleName()
					+ "is not a planning entity class.");
		}
		return planningVariableDescriptors;
	}

	protected List<PlanningVariableDescriptor> getChainedVariableDescriptors(
			List<PlanningVariableDescriptor> receivedPlanningVariableDescriptors) {
		List<PlanningVariableDescriptor> chainedVariableDescriptors = new ArrayList<PlanningVariableDescriptor>();
		for (PlanningVariableDescriptor planningVariableDescriptor : receivedPlanningVariableDescriptors) {
			if (planningVariableDescriptor.isChained()) {
				chainedVariableDescriptors.add(planningVariableDescriptor);
			}
		}
		return chainedVariableDescriptors;
	}

	protected List<PlanningVariableDescriptor> getNonChainedVariableDescriptors(
			List<PlanningVariableDescriptor> receivedPlanningVariableDescriptors) {
		List<PlanningVariableDescriptor> nonChainedVariableDescriptors = new ArrayList<PlanningVariableDescriptor>();
		for (PlanningVariableDescriptor planningVariableDescriptor : receivedPlanningVariableDescriptors) {
			if (!planningVariableDescriptor.isChained()) {
				nonChainedVariableDescriptors.add(planningVariableDescriptor);
			}
		}
		return nonChainedVariableDescriptors;
	}
}
