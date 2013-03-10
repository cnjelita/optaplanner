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

package org.optaplanner.core.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.optaplanner.core.domain.solution.SolutionDescriptor;
import org.optaplanner.core.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.geneticalgorithm.Individual;
import org.optaplanner.core.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.move.Move;
import org.optaplanner.core.score.director.ScoreDirector;
import org.optaplanner.core.util.RandomUtils;

public class OnePointCrossoverOperator extends GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter implements
        CrossoverOperator {

    private Random workingRandom;
    private List<Class<?>> entityClassList;
    private Map<Class<?>, Collection<PlanningVariableDescriptor>> entityClassToVariableDescriptorMap;
    private int entityClassSize;

    @Override
    public void performCrossover(GeneticAlgorithmStepScope stepScope) {
        List<ScoreDirector> individuals = stepScope.getIntermediatePopulation().getIndividuals();
        int populationSize = stepScope.getIntermediatePopulationSize();

        Collections.shuffle(individuals, workingRandom);

        for (int i = 0; i < populationSize / 2; i += 2) {
            ScoreDirector leftScoreDirector = individuals.get(i);
            Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
            ScoreDirector rightScoreDirector = individuals.get(i + 1);
            Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();

            Class<?> entityClass = entityClassList.get(workingRandom.nextInt(entityClassSize));
            Collection<PlanningVariableDescriptor> variableDescriptors = entityClassToVariableDescriptorMap.get(
                    entityClass);

            long entitySize = leftParent.getEntitySize(entityClass);
            long crossoverPoint = RandomUtils.nextLong(workingRandom, entitySize);

            for (int j = 0; j < crossoverPoint; j++) {
                swapValues(variableDescriptors, leftParent.getEntityByClassAndId(entityClass, j),
                        leftScoreDirector,
                        rightParent.getEntityByClassAndId(entityClass, j), rightScoreDirector);
            }
        }
    }

    private void swapValues(Collection<PlanningVariableDescriptor> planningVariableDescriptors, Object leftEntity,
            ScoreDirector leftScoreDirector, Object rightEntity, ScoreDirector rightScoreDirector) {
        Move leftMove = new SwapMove(planningVariableDescriptors, leftEntity, rightEntity);
        if (leftMove.isMoveDoable(leftScoreDirector)) {
            leftMove.doMove(leftScoreDirector);
        }
        Move rightMove = new SwapMove(planningVariableDescriptors, rightEntity, leftEntity);
        if (leftMove.isMoveDoable(rightScoreDirector)) {
            leftMove.doMove(rightScoreDirector);
        }
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        super.phaseStarted(phaseScope);
        workingRandom = phaseScope.getWorkingRandom();
        SolutionDescriptor solutionDescriptor = phaseScope.getSolutionDescriptor();
        entityClassList = new ArrayList<Class<?>>();
        Set<Class<?>> entityClassSet = solutionDescriptor.getPlanningEntityClassSet();
        entityClassSize = entityClassSet.size();
        entityClassToVariableDescriptorMap = new HashMap<Class<?>, Collection<PlanningVariableDescriptor>>();
        for (Class<?> entityClass : entityClassSet) {
            entityClassList.add(entityClass);
            entityClassToVariableDescriptorMap.put(entityClass,
                    solutionDescriptor.getPlanningEntityDescriptor(entityClass).getPlanningVariableDescriptors());
        }
    }
}
