package org.optaplanner.config.geneticalgorithm.operator.crossover;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.domain.solution.SolutionDescriptor;
import org.optaplanner.core.geneticalgorithm.operator.crossover.CrossoverOperator;
import org.optaplanner.core.geneticalgorithm.operator.crossover.CycleCrossoverOperator;

@XStreamAlias("cycleCrossoverOperator")
public class CycleCrossoverConfig extends CrossoverOperatorConfig {

    @Override
    public CrossoverOperator buildCrossoverOperator(SolutionDescriptor solutionDescriptor) {
        CycleCrossoverOperator cycleCrossoverOperator = new CycleCrossoverOperator();
        return cycleCrossoverOperator;
    }
}
