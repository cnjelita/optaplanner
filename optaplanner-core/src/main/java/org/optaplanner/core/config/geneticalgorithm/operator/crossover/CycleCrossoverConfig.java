package org.optaplanner.core.config.geneticalgorithm.operator.crossover;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.AbstractChainingCrossoverOperator;
import org.optaplanner.core.impl.geneticalgorithm.operator.crossover.CycleCrossoverOperator;

@XStreamAlias("cycleCrossoverOperator")
public class CycleCrossoverConfig extends ChainingCrossoverOperatorConfig {

	@Override
	public AbstractChainingCrossoverOperator createInstance() {
		return new CycleCrossoverOperator();
	}
}
