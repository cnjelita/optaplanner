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

package org.optaplanner.config.geneticalgorithm;

import org.optaplanner.core.geneticalgorithm.GeneticAlgorithmSolverPhase;

//TODO rename to PopulationConfig?
public class PopulationParametersConfig {

    private Integer populationSize = null;
    private Integer elitistSize = null;

    public Integer getElitistSize() {
        return elitistSize;
    }

    public void setElitistSize(Integer elitistSize) {
        this.elitistSize = elitistSize;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public void buildPopulationParameters(GeneticAlgorithmSolverPhase solverPhase) {
        //TODO do necessary checks for populationSize > elitistSize ...
        //TODO maybe make population parameters a class?
        //TODO - update exception explanation so that user knows tag name and where it went wrong
        if (populationSize == null) {
            throw new IllegalArgumentException("The size of the population was not specified");
        } else if (populationSize == 0) {
            throw new IllegalArgumentException("The population size cannot be zero");
        } else {
            solverPhase.setPopulationSize(populationSize);
        }

        if (elitistSize != null) {
            if (elitistSize < 0) {
                throw new IllegalArgumentException("Elitist size cannot be smaller than zero");
            } else if (elitistSize > populationSize) {
                throw new IllegalArgumentException("Elitist size cannot be larger than population size");
            } else {
                solverPhase.setElitistSize(elitistSize);
            }
        }
    }
}
