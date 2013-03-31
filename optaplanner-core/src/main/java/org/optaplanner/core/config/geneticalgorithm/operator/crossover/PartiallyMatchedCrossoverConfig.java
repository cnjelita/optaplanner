package org.optaplanner.core.config.geneticalgorithm.operator.crossover;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractChainingCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.PartiallyMatchedCrossover;

@XStreamAlias("partiallyMatchedCrossoverOperator")
public class PartiallyMatchedCrossoverConfig extends ChainingCrossoverOperatorConfig {

	@Override
	public AbstractChainingCrossoverOperator createInstance() {
		return new PartiallyMatchedCrossover();
	}
}
