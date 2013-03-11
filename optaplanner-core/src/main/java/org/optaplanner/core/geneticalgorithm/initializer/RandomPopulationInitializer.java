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

package org.optaplanner.core.geneticalgorithm.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.optaplanner.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.core.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.domain.solution.SolutionDescriptor;
import org.optaplanner.core.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.geneticalgorithm.Population;
import org.optaplanner.core.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.score.director.ScoreDirector;
import org.optaplanner.core.score.director.ScoreDirectorFactory;
import org.optaplanner.core.solution.Solution;

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
        //TODO import solution from previous algorithms
        for (int i = 0; i < populationSize; i++) {
            Solution clone = solutionCloner.cloneSolution(phaseScope.getWorkingSolution());
            List<Object> planningEntityList = phaseScope.getSolutionDescriptor().getPlanningEntityList(clone);
            for (Object planningEntity : planningEntityList) {
                PlanningEntityDescriptor entityDescriptor =
                        entityClassToDescriptorMap.get(planningEntity.getClass());
                List<PlanningVariableDescriptor> variableDescriptors = entityDescriptorToVariableDescriptorsMap.get(
                        entityDescriptor);
                for (PlanningVariableDescriptor variableDescriptor : variableDescriptors) {
                    List<Object> planningValues = variableDescriptorToValuesMap.get(variableDescriptor);
                    if (variableDescriptor.isChained()) {
                        //TODO implement chained variable option - Use chainedChangeMove?
                        //TODO should keep track of already used values?
                    } else {
                        //TODO maybe use changeMoves so the isDoable option is available?
                        variableDescriptor.setValue(planningEntity,
                                planningValues.get(workingRandom.nextInt(planningValues.size())));
                    }
                }
            }
            ScoreDirector scoreDirector = scoreDirectorFactory.buildScoreDirector();
            scoreDirector.setWorkingSolution(clone);
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
