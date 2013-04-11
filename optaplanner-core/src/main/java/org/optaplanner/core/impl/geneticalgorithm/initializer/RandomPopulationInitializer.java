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

package org.optaplanner.core.impl.geneticalgorithm.initializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.Population;
import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.impl.heuristic.selector.move.generic.ChangeMove;
import org.optaplanner.core.impl.heuristic.selector.move.generic.chained.ChainedChangeMove;
import org.optaplanner.core.impl.heuristic.selector.move.generic.chained.ChainedSwapMove;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.optaplanner.core.impl.solution.Solution;

public class RandomPopulationInitializer extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter
        implements PopulationInitializer {

    //TODO Reuse moves and selectors from localSearch?
    protected Iterator<Object> entityIterator = null;
    private Random workingRandom;
    private SolutionDescriptor solutionDescriptor;
    private SolutionCloner solutionCloner;
    private Map<Class<?>, PlanningEntityDescriptor> entityClassToDescriptorMap;
    private Map<PlanningEntityDescriptor, List<PlanningVariableDescriptor>> entityDescriptorToVariableDescriptorsMap;
    private Map<PlanningVariableDescriptor, List<Object>> variableDescriptorToValuesMap;
    private int populationSize;
    private ScoreDirectorFactory scoreDirectorFactory;

    @Override
    //TODO make sure no solutions are equal
    public void initializePopulation(GeneticAlgorithmSolverPhaseScope phaseScope) {
        Population population = new Population(populationSize);
        ScoreDirector originalScoreDirector = null;
        int numberOfIndividuals = populationSize;
        if (phaseScope.isBestSolutionInitialized()) {
            originalScoreDirector = phaseScope.getScoreDirector();
        } else {
            Map<PlanningVariableDescriptor, Set<Object>> chainedPlanningVariableToUsedValuesMap =
                    new HashMap<PlanningVariableDescriptor, Set<Object>>();
            originalScoreDirector = phaseScope.getScoreDirector().clone();
            Solution clone = originalScoreDirector.getWorkingSolution();
            List<Object> planningEntityList = phaseScope.getSolutionDescriptor().getPlanningEntityList(clone);
            Collections.shuffle(planningEntityList, workingRandom);
            for (Object planningEntity : planningEntityList) {
                PlanningEntityDescriptor entityDescriptor =
                        entityClassToDescriptorMap.get(planningEntity.getClass());
                List<PlanningVariableDescriptor> variableDescriptors = entityDescriptorToVariableDescriptorsMap.get(
                        entityDescriptor);
                for (PlanningVariableDescriptor variableDescriptor : variableDescriptors) {
                    List<Object> planningValues = variableDescriptorToValuesMap.get(variableDescriptor);
                    if (variableDescriptor.isChained()) {
                        Set<Object> usedValues = chainedPlanningVariableToUsedValuesMap.get(variableDescriptor);
                        if (usedValues == null) {
                            usedValues = new HashSet<Object>();
                        }
                        Collection<?> values = variableDescriptor.extractPlanningValues(clone, planningEntity);
                        //TODO Problem here: if excludeUninitialized... is disabled this does not work!
                        for (Object value : values) {
                            if (!usedValues.contains(value)) {
                                Move move = new ChainedChangeMove(planningEntity, variableDescriptor, value);
                                move.doMove(originalScoreDirector);
                                usedValues.add(value);
                                break;
                            }
                        }
                    } else {
                        //TODO maybe use changeMoves so the isDoable option is available?
                        Move move = new ChangeMove(planningEntity, variableDescriptor,
                                planningValues.get(workingRandom.nextInt(planningValues.size())));
                        move.doMove(originalScoreDirector);
                    }
                }
            }
        }

        population.addIndividual(originalScoreDirector);

        //TODO import solution from previous algorithms
        for (int i = 0; i < numberOfIndividuals - 1; i++) {
            //TODO make set of sets!
            ScoreDirector scoreDirector = originalScoreDirector.clone();
            Solution clone = scoreDirector.getWorkingSolution();
            phaseScope.getSolverScope().setScoreDirector(scoreDirector);
            List<Object> planningEntityList = phaseScope.getSolutionDescriptor().getPlanningEntityList(clone);
            Collections.shuffle(planningEntityList, workingRandom);
            for (int j = planningEntityList.size() - 1; j > 0; j--) {
                Object planningEntity = planningEntityList.get(j);
                PlanningEntityDescriptor entityDescriptor =
                        entityClassToDescriptorMap.get(planningEntity.getClass());
                List<PlanningVariableDescriptor> variableDescriptors = entityDescriptorToVariableDescriptorsMap.get(
                        entityDescriptor);
                for (PlanningVariableDescriptor variableDescriptor : variableDescriptors) {
                    if (variableDescriptor.isChained()) {
                        int index = workingRandom.nextInt(j + 1);
                        Move swapMove = new ChainedSwapMove(Arrays
                                .asList(variableDescriptor), planningEntity, planningEntityList
                                .get(index));
                        swapMove.doMove(scoreDirector);
                    } else {
                        List<Object> planningValues = variableDescriptorToValuesMap.get(variableDescriptor);
                        //TODO maybe use changeMoves so the isDoable option is available?
                        Move move = new ChangeMove(planningEntity, variableDescriptor,
                                planningValues.get(workingRandom.nextInt(planningValues.size())));
                        move.doMove(scoreDirector);
                    }
                }
            }
            population.addIndividual(scoreDirector);
        }
        population.performScoreCalculation();
        phaseScope.setNewGeneration(population);
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        workingRandom = phaseScope.getWorkingRandom();
        solutionDescriptor = phaseScope.getSolutionDescriptor();
        Solution workingSolution = phaseScope.getWorkingSolution();
        scoreDirectorFactory = phaseScope.getScoreDirector().getScoreDirectorFactory();
        solutionCloner = solutionDescriptor.getSolutionCloner();
        populationSize = phaseScope.getPopulationSize();
        Set<Class<?>> planningEntityClassSet = solutionDescriptor.getPlanningEntityClassSet();
        entityClassToDescriptorMap = new HashMap<Class<?>, PlanningEntityDescriptor>(planningEntityClassSet.size());
        variableDescriptorToValuesMap = new HashMap<PlanningVariableDescriptor, List<Object>>();
        entityDescriptorToVariableDescriptorsMap =
                new HashMap<PlanningEntityDescriptor, List<PlanningVariableDescriptor>>();
        for (Class<?> entityClass : planningEntityClassSet) {
            PlanningEntityDescriptor entityDescriptor = solutionDescriptor.getPlanningEntityDescriptor(entityClass);
            entityClassToDescriptorMap.put(entityClass, entityDescriptor);
            List<PlanningVariableDescriptor> variableDescriptors = new ArrayList<PlanningVariableDescriptor>(
                    entityDescriptor.getPlanningVariableDescriptors());
            entityDescriptorToVariableDescriptorsMap.put(entityDescriptor, variableDescriptors);
            for (PlanningVariableDescriptor variableDescriptor : variableDescriptors) {
                //TODO can variables be added here or do they get filtered as more are added?
                variableDescriptorToValuesMap.put(variableDescriptor,
                        new ArrayList<Object>(variableDescriptor.extractAllPlanningValues(workingSolution)));
            }
        }
    }

}
