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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.CrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.UniformCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.UnionCrossoverOperator;

@XStreamAlias("unionCrossoverOperator")
public class UnionCrossoverOperatorConfig extends CrossoverOperatorConfig {

	@XStreamImplicit()
	private List<CrossoverOperatorConfig> crossoverOperatorConfigList = null;

	@Override
	public List<CrossoverOperator> buildCrossoverOperator(Double alternativeCrossoverRate,
			SolutionDescriptor solutionDescriptor) {
		if (crossoverOperatorConfigList == null || crossoverOperatorConfigList.isEmpty()) {
			throw new IllegalArgumentException("unionCrossoverOperator should contain at least one crossover" +
					" operator.");
		}
		if (crossoverRate != null && (this.crossoverRate <= 0 || this.crossoverRate > 1)) {
			throw new IllegalArgumentException("Crossover rate should be larger than zero");
		} else if (crossoverRate == null) {
			alternativeCrossoverRate = Double.valueOf(1);
		}

		UnionCrossoverOperator unionCrossoverOperator = new UnionCrossoverOperator();
		for (CrossoverOperatorConfig crossoverOperatorConfig : crossoverOperatorConfigList) {
			List<CrossoverOperator> crossoverOperators = crossoverOperatorConfig.buildCrossoverOperator(
					alternativeCrossoverRate, solutionDescriptor);
			for (CrossoverOperator crossoverOperator : crossoverOperators) {
				unionCrossoverOperator.addCrossoverOperator(crossoverOperator);
			}
		}
		List<CrossoverOperator> crossoverOperators = new ArrayList<CrossoverOperator>();
		crossoverOperators.add(unionCrossoverOperator);
		return crossoverOperators;
	}

	@Override
	protected AbstractCrossoverOperator createInstance() {
		return new UniformCrossoverOperator();
	}
}
