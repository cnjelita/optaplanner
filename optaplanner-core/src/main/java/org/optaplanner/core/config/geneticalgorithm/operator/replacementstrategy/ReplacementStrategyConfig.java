package org.optaplanner.core.config.geneticalgorithm.operator.replacementstrategy;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.geneticalgorithm.ReplacementStrategyType;
import org.optaplanner.core.impl.geneticalgorithm.initializer.PopulationInitializer;
import org.optaplanner.core.impl.geneticalgorithm.replacementstrategy.KeepBestStrategy;
import org.optaplanner.core.impl.geneticalgorithm.replacementstrategy.KeepNewStrategy;
import org.optaplanner.core.impl.geneticalgorithm.replacementstrategy.RandomStrategy;
import org.optaplanner.core.impl.geneticalgorithm.replacementstrategy.ReplacementStrategy;
import org.optaplanner.core.impl.geneticalgorithm.replacementstrategy.SteadyStateStrategy;

@XStreamAlias("replacementStrategy")
public class ReplacementStrategyConfig {

    ReplacementStrategyType replacementStrategyType = null;
    Double diversityRate = null;

    public void setReplacementStrategyType(ReplacementStrategyType replacementStrategyType) {
        this.replacementStrategyType = replacementStrategyType;
    }

    public void setDiversityRate(Double diversityRate) {
        this.diversityRate = diversityRate;
    }

    public ReplacementStrategy buildReplacementStrategy(PopulationInitializer initializer) {
        ReplacementStrategy replacementStrategy;
        if (replacementStrategyType == null) {
            //TODO set best option as default
            replacementStrategy = new KeepNewStrategy();
        } else {
            switch (replacementStrategyType) {
                case KEEP_NEW:
                    replacementStrategy = new KeepNewStrategy();
                    break;
                case RANDOM:
                    replacementStrategy = new RandomStrategy();
                    break;
                case STEADY_STATE:
                    replacementStrategy = new SteadyStateStrategy();
                default:
                    //TODO default to best replacement strategy
                    replacementStrategy = new KeepBestStrategy();
                    break;
            }
        }
        if (diversityRate != null) {
            replacementStrategy.setDiversityRate(diversityRate);
            replacementStrategy.setPopulationInitializer(initializer);
        }
        return replacementStrategy;
    }
}
