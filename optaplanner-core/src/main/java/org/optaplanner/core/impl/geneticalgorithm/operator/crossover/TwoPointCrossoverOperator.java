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
import org.optaplanner.core.impl.util.RandomUtils;

public class TwoPointCrossoverOperator extends AbstractCrossoverOperator {

	@Override
	protected void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector) {
		Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
		Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();
		long entitySize = leftParent.getEntitySize(entityClass);

		long leftCrossoverPoint = RandomUtils.nextLong(workingRandom, entitySize);
		long rightCrossoverPoint;
		do {
			rightCrossoverPoint = RandomUtils.nextLong(workingRandom, entitySize);
		} while (leftCrossoverPoint == rightCrossoverPoint);

		for (long j = leftCrossoverPoint; j < rightCrossoverPoint; j++) {
			swapValues(leftParent.getEntityByClassAndId(entityClass, j), leftScoreDirector,
					rightParent.getEntityByClassAndId(entityClass, j), rightScoreDirector);
		}
	}
}
