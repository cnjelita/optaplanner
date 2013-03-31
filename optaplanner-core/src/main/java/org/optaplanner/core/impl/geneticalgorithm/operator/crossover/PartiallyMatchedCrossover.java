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

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class PartiallyMatchedCrossover extends AbstractChainingCrossoverOperator {

	@Override
	protected void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector) {
		Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
		Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();

		List<Object> leftEntityList = getOrderedList(leftScoreDirector);
		List<Object> rightEntityList = getOrderedList(rightScoreDirector);

		//TODO can it be possible that their not same size?
		int entitySize = leftEntityList.size();
		if (rightEntityList.size() != entitySize) {
			throw new IllegalStateException("Trying to perform crossover with individuals which have a different" +
					" amount of entities. This is not possible, report bug.");
		}

		List<Long> originalLeftIds = new ArrayList<Long>();
		List<Long> originalRightIds = new ArrayList<Long>();

		for (int j = 0; j < entitySize; j++) {
			originalLeftIds.add(leftParent.getEntityId(leftEntityList.get(j)));
			originalRightIds.add(rightParent.getEntityId(rightEntityList.get(j)));
		}

//		System.out.println(originalLeftIds);
//		System.out.println(originalRightIds);

		int leftCrossoverPoint = workingRandom.nextInt(entitySize);
		int rightCrossoverPoint;
		do {
			rightCrossoverPoint = workingRandom.nextInt(entitySize);
		} while (leftCrossoverPoint == rightCrossoverPoint);

		int temp = Math.max(leftCrossoverPoint, rightCrossoverPoint);
		leftCrossoverPoint = Math.min(leftCrossoverPoint, rightCrossoverPoint);
		rightCrossoverPoint = temp;

		List<Long> newLeftIds = new ArrayList<Long>(originalLeftIds);
		List<Long> newRightIds = new ArrayList<Long>(originalRightIds);
//		System.out.println("leftCrossoverPoint " + leftCrossoverPoint);
//		System.out.println("rightCrossoverPoint " + rightCrossoverPoint);

		for (int i = leftCrossoverPoint; i < rightCrossoverPoint; i++) {
			long leftId = newLeftIds.get(i);
			long originalRightId = originalRightIds.get(i);
			long rightId = newRightIds.get(i);
			long originalLeftId = originalLeftIds.get(i);

			Object oldLeftEntity = leftEntityList.get(i);
			Object oldRightEntity = rightEntityList.get(i);

			Object newLeftEntity = null;
			for (int j = 0; j < entitySize; j++) {
				if (newLeftIds.get(j) == originalRightId) {
					newLeftEntity = leftEntityList.get(j);
					leftEntityList.set(j, oldLeftEntity);
					newLeftIds.set(j, leftId);
				}
			}
			leftEntityList.set(i, newLeftEntity);
			newLeftIds.set(i, rightId);

			Object newRightEntity = null;
			for (int j = 0; j < entitySize; j++) {
				if (newRightIds.get(j) == originalLeftId) {
					newRightEntity = rightEntityList.get(j);
					rightEntityList.set(j, oldRightEntity);
					newRightIds.set(j, rightId);
				}
			}
			rightEntityList.set(i, newRightEntity);
			newRightIds.set(i, leftId);

			swapChainedValues(oldLeftEntity, newLeftEntity, leftScoreDirector);
			swapChainedValues(oldRightEntity, newRightEntity, rightScoreDirector);
		}

//		leftEntityList = getOrderedList(leftScoreDirector);
//		rightEntityList = getOrderedList(rightScoreDirector);
//
//		//TODO can it be possible that their not same size?
//		entitySize = leftEntityList.size();
//		if (rightEntityList.size() != entitySize) {
//			throw new IllegalStateException("Trying to perform crossover with individuals which have a different" +
//					" amount of entities. This is not possible, report bug.");
//		}
//
//		originalLeftIds = new ArrayList<Long>();
//		originalRightIds = new ArrayList<Long>();
//
//		for (int j = 0; j < entitySize; j++) {
//			originalLeftIds.add(leftParent.getEntityId(leftEntityList.get(j)));
//			originalRightIds.add(rightParent.getEntityId(rightEntityList.get(j)));
//		}
//
//		System.out.println(originalLeftIds);
//		System.out.println(originalRightIds);
//		System.exit(0);
	}
}
