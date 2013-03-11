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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.optaplanner.core.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.geneticalgorithm.Individual;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.score.director.ScoreDirector;
import org.optaplanner.core.util.RandomUtils;

public class TwoPointCrossoverOperator extends AbstractCrossoverOperator {

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

            long entitySize = leftParent.getEntitySize(entityClass);

            long leftCrossoverPoint = RandomUtils.nextLong(workingRandom, entitySize);
            long rightCrossoverPoint;
            do {
                rightCrossoverPoint = RandomUtils.nextLong(workingRandom, entitySize);
            } while (leftCrossoverPoint == rightCrossoverPoint);

            for (long j = leftCrossoverPoint; j < rightCrossoverPoint; j++) {
                swapValues(variableDescriptors, leftParent.getEntityByClassAndId(entityClass, j),
                        leftScoreDirector,
                        rightParent.getEntityByClassAndId(entityClass, j), rightScoreDirector);
            }
        }
    }
}
