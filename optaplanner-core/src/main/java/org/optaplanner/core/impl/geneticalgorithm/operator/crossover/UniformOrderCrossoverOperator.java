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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class UniformOrderCrossoverOperator extends AbstractChainingCrossoverOperator {

	@Override
	protected void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector) {
		Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
		Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();

		List<Object> leftEntityList = getOrderedList(leftScoreDirector);
		List<Object> rightEntityList = getOrderedList(rightScoreDirector);

		//TODO can sizes really differ?
		int entitySize = leftEntityList.size();
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
			Long rightId = rightParent.getEntityId(rightEntityList.get(j));
			if (!rightSwapMap.containsKey(leftId)) {
				leftAvailableIds.add(leftId);
			}
			if (!leftSwapMap.containsKey(rightId)) {
				rightAvailableIds.add(rightId);
			}
		}

		for (int j = 0; j < leftCrossoverPoint; j++) {
			Object fromLeftEntity = leftEntityList.get(j);
			Object toLeftEntity = leftParent.getEntityByClassAndId(entityClass, rightAvailableIds.remove());

			Object fromRightEntity = rightEntityList.get(j);
			Object toRightEntity = rightParent.getEntityByClassAndId(entityClass, leftAvailableIds.remove());

			performMove(leftScoreDirector, fromLeftEntity, toLeftEntity);
			performMove(rightScoreDirector, fromRightEntity, toRightEntity);
		}

		for (int j = rightCrossoverPoint; j < entitySize; j++) {
			Object fromLeftEntity = leftEntityList.get(j);
			Object toLeftEntity = leftParent.getEntityByClassAndId(entityClass, rightAvailableIds.remove());

			Object fromRightEntity = rightEntityList.get(j);
			Object toRightEntity = rightParent.getEntityByClassAndId(entityClass, leftAvailableIds.remove());

			performMove(leftScoreDirector, fromLeftEntity, toLeftEntity);
			performMove(rightScoreDirector, fromRightEntity, toRightEntity);
		}
	}

}
