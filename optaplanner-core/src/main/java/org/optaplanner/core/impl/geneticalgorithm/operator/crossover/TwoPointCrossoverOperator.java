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

package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class TwoPointCrossoverOperator extends AbstractCrossoverOperator {

	@Override
	protected void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector) {
		Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
		Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();
		int entitySize = leftParent.getEntitySize(entityClass);

		int leftCrossoverPoint = workingRandom.nextInt(entitySize);
		int rightCrossoverPoint;
		do {
			rightCrossoverPoint = workingRandom.nextInt(entitySize);
		} while (leftCrossoverPoint == rightCrossoverPoint);

		int temp = Math.max(leftCrossoverPoint, rightCrossoverPoint);
		leftCrossoverPoint = Math.min(leftCrossoverPoint, rightCrossoverPoint);
		rightCrossoverPoint = temp;

		for (int j = leftCrossoverPoint; j < rightCrossoverPoint; j++) {
			swapValues(leftParent.getEntityByClassAndId(entityClass, Long.valueOf(j)), leftScoreDirector,
					rightParent.getEntityByClassAndId(entityClass, Long.valueOf(j)), rightScoreDirector);
		}
	}
}
