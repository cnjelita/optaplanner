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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.optaplanner.core.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.geneticalgorithm.Individual;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.heuristic.selector.move.generic.chained.ChainedSwapMove;
import org.optaplanner.core.move.Move;
import org.optaplanner.core.score.director.ScoreDirector;

public class UniformOrderCrossover extends AbstractCrossoverOperator {

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

            Class<?> entityClass = entityClassList.get(workingRandom.nextInt(entityListClassSize));

            Collection<PlanningVariableDescriptor> variableDescriptors = entityClassToVariableDescriptorMap.get(
                    entityClass);

            //TODO assuming that there is only one chained variable
            //TODO we're assuming solutions are completely initialized
            PlanningVariableDescriptor chainedVariableDescriptor =
                    solutionDescriptor.getChainedVariableDescriptors().iterator().next();

            List<Object> leftEntityList = getOrderedList(
                    solutionDescriptor.getPlanningEntityListByPlanningEntityClass(leftParent,
                            entityClass).get(0), leftScoreDirector, chainedVariableDescriptor);
            List<Object> rightEntityList = getOrderedList(
                    solutionDescriptor.getPlanningEntityListByPlanningEntityClass(rightParent,
                            entityClass).get(0), rightScoreDirector, chainedVariableDescriptor);

            int entitySize = leftEntityList.size();

            //TODO update exception
            if (rightEntityList.size() != entitySize) {
                throw new IllegalArgumentException("Both lists should be same size!");
            }

            Map<Long, Integer> leftEntityIdToIndexMap = new HashMap<Long, Integer>();
            Map<Long, Integer> rightEntityIdToIndexMap = new HashMap<Long, Integer>();
            for (int j = 0; j < entitySize; j++) {
                leftEntityIdToIndexMap.put(leftParent.getEntityId(leftEntityList.get(j)), j);
                rightEntityIdToIndexMap.put(rightParent.getEntityId(rightEntityList.get(j)), j);
            }

            int leftTempCrossoverPoint = workingRandom.nextInt(entitySize);
            int rightTempCrossoverPoint;
            do {
                rightTempCrossoverPoint = workingRandom.nextInt(entitySize);
            } while (leftTempCrossoverPoint == rightTempCrossoverPoint);

            int leftCrossoverPoint = Math.min(leftTempCrossoverPoint, rightTempCrossoverPoint);
            int rightCrossoverPoint = Math.max(leftTempCrossoverPoint, rightTempCrossoverPoint);

            Map<Integer, Long> leftSwapMap = new HashMap<Integer, Long>();
            Map<Integer, Long> rightSwapMap = new HashMap<Integer, Long>();

            //TODO why use long if set can't contain that many items?
            for (int j = leftCrossoverPoint; j < rightCrossoverPoint; j++) {
                Long leftId = leftParent.getEntityId(leftEntityList.get(j));
                Long rightId = rightParent.getEntityId(rightEntityList.get(j));
                leftSwapMap.put(j, leftId);
                rightSwapMap.put(j, rightId);
            }

            Queue<Long> leftAvailableIds = new LinkedList<Long>();
            Queue<Long> rightAvailableIds = new LinkedList<Long>();

            for (int j = 0; j < entitySize; j++) {
                Long leftId = leftParent.getEntityId(leftEntityList.get(j));
                if (!rightSwapMap.containsKey(leftId)) {
                    leftAvailableIds.add(leftId);
                }
                Long rightId = rightParent.getEntityId(rightEntityList.get(j));
                if (!leftSwapMap.containsKey(rightId)) {
                    rightAvailableIds.add(rightId);
                }
            }

            for (int j = 0; j < leftCrossoverPoint; j++) {
                Object fromLeftEntity = leftEntityList.get(j);
                Object toLeftEntity = leftParent.getEntityByClassAndId(entityClass, rightAvailableIds.remove());
                Move leftSwapMove = new ChainedSwapMove(variableDescriptors, fromLeftEntity, toLeftEntity);
                if (leftSwapMove.isMoveDoable(leftScoreDirector)) {
                    leftSwapMove.doMove(leftScoreDirector);
                }

                Object fromRightEntity = rightEntityList.get(j);
                Object toRightEntity = rightParent.getEntityByClassAndId(entityClass, leftAvailableIds.remove());
                Move rightSwapMove = new ChainedSwapMove(variableDescriptors, fromRightEntity, toRightEntity);
                if (rightSwapMove.isMoveDoable(rightScoreDirector)) {
                    rightSwapMove.doMove(rightScoreDirector);
                }

            }

            for (int j = rightCrossoverPoint; j < entitySize; j++) {
                Object fromLeftEntity = leftEntityList.get(j);
                Object toLeftEntity = leftParent.getEntityByClassAndId(entityClass, rightAvailableIds.remove());
                Move leftSwapMove = new ChainedSwapMove(variableDescriptors, fromLeftEntity, toLeftEntity);
                if (leftSwapMove.isMoveDoable(leftScoreDirector)) {
                    leftSwapMove.doMove(leftScoreDirector);
                }

                Object fromRightEntity = rightEntityList.get(j);
                Object toRightEntity = rightParent.getEntityByClassAndId(entityClass, leftAvailableIds.remove());
                Move rightSwapMove = new ChainedSwapMove(variableDescriptors, fromRightEntity, toRightEntity);
                if (rightSwapMove.isMoveDoable(rightScoreDirector)) {
                    rightSwapMove.doMove(rightScoreDirector);
                }
            }

//            System.exit(0);

        }

    }

    private List<Object> getOrderedList(Object planningEntity, ScoreDirector scoreDirector,
            PlanningVariableDescriptor chainedVariableDescriptor) {
        List<Object> orderedList = new ArrayList<Object>();
        PlanningEntityDescriptor entityDescriptor = chainedVariableDescriptor.getPlanningEntityDescriptor();
        while (scoreDirector.getTrailingEntity(chainedVariableDescriptor, planningEntity) != null) {
            planningEntity = scoreDirector.getTrailingEntity(chainedVariableDescriptor, planningEntity);
        }
        while (planningEntity != null && entityDescriptor.appliesToPlanningEntity(planningEntity)) {
            orderedList.add(planningEntity);
//            System.out.println(planningEntity);
            planningEntity = chainedVariableDescriptor.getValue(planningEntity);
        }
        Collections.reverse(orderedList);
        return orderedList;
    }
}
