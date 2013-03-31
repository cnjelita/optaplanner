package org.optaplanner.core.config.geneticalgorithm.operator.crossover;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.CrossoverOperator;

@XStreamAlias("partiallyMatchedCrossoverOperator")
public class PartiallyMatchedCrossoverConfig extends CrossoverOperatorConfig {

	@Override
	public List<CrossoverOperator> buildCrossoverOperator(Double alternativeCrossoverRate,
			SolutionDescriptor solutionDescriptor) {
		throw new UnsupportedOperationException("Partially Matched crossover not supported  yet");
	}

	@Override
	protected AbstractCrossoverOperator createInstance() {
		throw new UnsupportedOperationException("Partially Matched crossover not supported  yet");
	}

}
