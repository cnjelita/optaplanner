package org.optaplanner.core.config.geneticalgorithm.initializer;

import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.initializer.PopulationInitializer;
import org.optaplanner.core.impl.geneticalgorithm.initializer.RandomPopulationInitializer;

public class RandomPopulationInitializerConfig extends PopulationInitializerConfig {

	@Override
	public PopulationInitializer buildPopulationInitializer(SolutionDescriptor solutionDescriptor) {
		RandomPopulationInitializer randomPopulationInitializer = new RandomPopulationInitializer();
		return randomPopulationInitializer;
	}
}
