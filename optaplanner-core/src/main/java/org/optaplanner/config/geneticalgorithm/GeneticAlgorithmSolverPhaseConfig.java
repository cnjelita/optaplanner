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

import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.collections.CollectionUtils;
import org.optaplanner.config.EnvironmentMode;
import org.optaplanner.config.geneticalgorithm.operator.SolutionSelector.SolutionSelectorConfig;
import org.optaplanner.config.geneticalgorithm.operator.crossover.CrossoverOperatorConfig;
import org.optaplanner.config.geneticalgorithm.operator.mutation.MutationOperatorConfig;
import org.optaplanner.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.config.heuristic.selector.move.composite.UnionMoveSelectorConfig;
import org.optaplanner.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
import org.optaplanner.config.heuristic.selector.move.generic.SwapMoveSelectorConfig;
import org.optaplanner.config.phase.SolverPhaseConfig;
import org.optaplanner.core.domain.solution.SolutionDescriptor;
import org.optaplanner.core.geneticalgorithm.GeneticAlgorithmSolverPhase;
import org.optaplanner.core.geneticalgorithm.operator.crossover.CrossoverOperator;
import org.optaplanner.core.geneticalgorithm.operator.mutation.MutationOperator;
import org.optaplanner.core.geneticalgorithm.operator.selector.SolutionSelector;
import org.optaplanner.core.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.phase.SolverPhase;
import org.optaplanner.core.score.definition.ScoreDefinition;
import org.optaplanner.core.termination.Termination;

@XStreamAlias("geneticAlgorithm")
public class GeneticAlgorithmSolverPhaseConfig extends SolverPhaseConfig {

    // Warning: all fields are null (and not defaulted) because they can be inherited
    // and also because the input config file should match the output config file

    @XStreamAlias("populationParameters")
    private PopulationParametersConfig populationParametersConfig = null;

    //TODO all of the operators are read into a list due to XStream limitations
    @XStreamImplicit()
    List<SolutionSelectorConfig> solutionSelectorConfigList = null;

    @XStreamImplicit()
    List<CrossoverOperatorConfig> crossoverOperatorConfigList = null;

    @XStreamImplicit()
    List<MutationOperatorConfig> mutationOperatorConfigList = null;

    public PopulationParametersConfig getPopulationParametersConfig() {
        return populationParametersConfig;
    }

    public void setPopulationParametersConfig(PopulationParametersConfig populationParametersConfig) {
        this.populationParametersConfig = populationParametersConfig;
    }

    public List<SolutionSelectorConfig> getSolutionSelectorConfigList() {
        return solutionSelectorConfigList;
    }

    public void setSolutionSelectorConfigList(List<SolutionSelectorConfig> solutionSelectorConfigList) {
        this.solutionSelectorConfigList = solutionSelectorConfigList;
    }

    public List<CrossoverOperatorConfig> getCrossoverOperatorConfigList() {
        return crossoverOperatorConfigList;
    }

    public void setCrossoverOperatorConfigList(List<CrossoverOperatorConfig> crossoverOperatorConfigList) {
        this.crossoverOperatorConfigList = crossoverOperatorConfigList;
    }

    public List<MutationOperatorConfig> getMutationOperatorConfig() {
        return mutationOperatorConfigList;
    }

    public void setMutationOperatorConfig(List<MutationOperatorConfig> mutationOperatorConfig) {
        this.mutationOperatorConfigList = mutationOperatorConfig;
    }

    @Override
    public SolverPhase buildSolverPhase(int phaseIndex, EnvironmentMode environmentMode,
            SolutionDescriptor solutionDescriptor, ScoreDefinition scoreDefinition, Termination solverTermination) {
        //TODO do necessary checks
        GeneticAlgorithmSolverPhase solverPhase = new GeneticAlgorithmSolverPhase();
        configureSolverPhase(solverPhase, phaseIndex, environmentMode, scoreDefinition, solverTermination);

        populationParametersConfig.buildPopulationParameters(solverPhase);
        solverPhase.setSolutionSelector(buildSolutionSelector(solutionDescriptor));
        solverPhase.setCrossoverOperator(buildCrossoverOperator(solutionDescriptor));
        solverPhase.setMutationOperator(buildMutationOperator(environmentMode, solutionDescriptor));

        if (environmentMode == EnvironmentMode.FAST_ASSERT || environmentMode == EnvironmentMode.FULL_ASSERT) {
            solverPhase.setAssertStepScoreIsUncorrupted(true);
        }

        return solverPhase;
    }

    private MutationOperator buildMutationOperator(EnvironmentMode environmentMode,
            SolutionDescriptor solutionDescriptor) {
        //TODO implement build method for mutation operator
        MutationOperator mutationOperator = null;
        SelectionCacheType defaultCacheType = SelectionCacheType.JUST_IN_TIME;
        SelectionOrder defaultSelectionOrder = SelectionOrder.RANDOM;
        if (CollectionUtils.isEmpty(mutationOperatorConfigList)) {
            // Default to changeMoveSelector and swapMoveSelector
            UnionMoveSelectorConfig unionMoveSelectorConfig = new UnionMoveSelectorConfig();
            unionMoveSelectorConfig.setMoveSelectorConfigList(Arrays.asList(
                    new ChangeMoveSelectorConfig(), new SwapMoveSelectorConfig()));
            mutationOperator.setMoveSelector(
                    unionMoveSelectorConfig.buildMoveSelector(environmentMode, solutionDescriptor,
                            defaultCacheType, defaultSelectionOrder));
        } else if (mutationOperatorConfigList.size() == 1) {
            mutationOperator = mutationOperatorConfigList.get(0).buildMutationOperator(
                    environmentMode, solutionDescriptor, defaultCacheType, defaultSelectionOrder);
        } else {
            throw new IllegalArgumentException("The mutationOperatorConfigList (" + mutationOperatorConfigList
                    + ") must a singleton or empty.");
        }
        return mutationOperator;
    }

    private CrossoverOperator buildCrossoverOperator(SolutionDescriptor solutionDescriptor) {
        CrossoverOperator crossoverOperator;
        if (CollectionUtils.isEmpty(crossoverOperatorConfigList)) {
            //TODO to build a default crossover operator check whether solution uses chaining...
            //For now we're trowing an exception
            throw new IllegalArgumentException("The crossoverOperatorConfigList (" + crossoverOperatorConfigList
                    + ") should contain exactly one item.");
        } else if (crossoverOperatorConfigList.size() == 1) {
            crossoverOperator = crossoverOperatorConfigList.get(0).buildCrossoverOperator(solutionDescriptor);
        } else {
            throw new IllegalArgumentException("The crossoverOperatorConfigList (" + crossoverOperatorConfigList
                    + ") cannot contain multiple items.");
        }
        return crossoverOperator;
    }

    private SolutionSelector buildSolutionSelector(SolutionDescriptor solutionDescriptor) {
        SolutionSelector solutionSelector;
        if (CollectionUtils.isEmpty(solutionSelectorConfigList)) {
            //TODO Choose best working solution selector as default if user didn't choose one
            //For now we're trowing an exception
            throw new IllegalArgumentException("The solutionSelectorConfigList (" + solutionSelectorConfigList
                    + ") should contain exactly one item.");
        } else if (solutionSelectorConfigList.size() == 1) {
            solutionSelector = solutionSelectorConfigList.get(0).buildSolutionSelector(solutionDescriptor);
        } else {
            throw new IllegalArgumentException("The solutionSelectorConfigList (" + solutionSelectorConfigList
                    + ") must a singleton or empty.");
        }
        return solutionSelector;
    }
}
