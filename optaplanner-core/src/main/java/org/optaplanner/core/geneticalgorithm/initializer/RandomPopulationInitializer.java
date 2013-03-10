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
import org.optaplanner.core.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.solution.Solution;

public class RandomPopulationInitializer extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter
        implements PopulationInitializer {

    protected Iterator<Object> entityIterator = null;
    private Random workingRandom;
    private SolutionDescriptor solutionDescriptor;
    private SolutionCloner solutionCloner;
    private Map<Class<?>, PlanningEntityDescriptor> entityClassToDescriptorMap;
    private Map<PlanningEntityDescriptor, List<PlanningVariableDescriptor>> entityDescriptorToVariableDescriptorsMap;
    private Map<PlanningVariableDescriptor, List<Object>> variableDescriptorToValuesMap;

    @Override
    public void initializePopulation(GeneticAlgorithmSolverPhaseScope phaseScope) {
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
                    //TODO implement chained variable option
                    //TODO should keep track of already used values?
                } else {
                    variableDescriptor.setValue(planningEntity,
                            planningValues.get(workingRandom.nextInt(planningValues.size())));
                }
            }
        }
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        workingRandom = phaseScope.getWorkingRandom();
        solutionDescriptor = phaseScope.getSolutionDescriptor();
        Solution workingSolution = phaseScope.getWorkingSolution();
        solutionCloner = solutionDescriptor.getSolutionCloner();

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
                variableDescriptorToValuesMap.put(variableDescriptor,
                        new ArrayList<Object>(variableDescriptor.extractAllPlanningValues(workingSolution)));
            }
        }
    }

}
