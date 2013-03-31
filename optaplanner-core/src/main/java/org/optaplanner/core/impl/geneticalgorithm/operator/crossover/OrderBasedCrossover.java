package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class OrderBasedCrossover extends AbstractChainingCrossoverOperator {

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

		int leftCrossoverPoint = workingRandom.nextInt(entitySize);
		int rightCrossoverPoint;
		do {
			rightCrossoverPoint = workingRandom.nextInt(entitySize);
		} while (leftCrossoverPoint == rightCrossoverPoint);

		int temp = Math.max(leftCrossoverPoint, rightCrossoverPoint);
		leftCrossoverPoint = Math.min(leftCrossoverPoint, rightCrossoverPoint);
		rightCrossoverPoint = temp;

		Set<Long> usedLeftIds = new HashSet<Long>();
		Set<Long> usedRightIds = new HashSet<Long>();
		for (int i = leftCrossoverPoint; i < rightCrossoverPoint; i++) {
			usedLeftIds.add(originalLeftIds.get(i));
			usedRightIds.add(originalRightIds.get(i));
		}

		int currentLeftIndex = rightCrossoverPoint;
		int currentRightIndex = rightCrossoverPoint;

		List<Long> newLeftIds = new ArrayList<Long>(originalLeftIds);
		List<Long> newRightIds = new ArrayList<Long>(originalRightIds);

		for (int i = 0; i < entitySize; i++) {
			if (!usedLeftIds.contains(originalRightIds.get(i))) {
				long fromLeftId = newLeftIds.get(currentLeftIndex);
				Object fromLeftEntity = leftEntityList.get(currentLeftIndex);
				long toLeftId = originalRightIds.get(i);
				Object toLeftEntity = null;
				for (int j = 0; j < entitySize; j++) {
					if (newLeftIds.get(j) == toLeftId) {
						toLeftEntity = leftEntityList.get(j);
						leftEntityList.set(j, fromLeftEntity);
						newLeftIds.set(j, fromLeftId);
					}
				}
				leftEntityList.set(currentLeftIndex, toLeftEntity);
				newLeftIds.set(currentLeftIndex, toLeftId);
				swapChainedValues(fromLeftEntity, toLeftEntity, leftScoreDirector);
				currentLeftIndex++;
				currentLeftIndex %= entitySize;
			}
			if (!usedRightIds.contains(originalLeftIds.get(i))) {
				long fromLeftId = newRightIds.get(currentRightIndex);
				Object fromRightEntity = rightEntityList.get(currentRightIndex);
				long toRightId = originalLeftIds.get(i);
				Object toRightEntity = null;
				for (int j = 0; j < entitySize; j++) {
					if (newRightIds.get(j) == toRightId) {
						toRightEntity = rightEntityList.get(j);
						rightEntityList.set(j, fromRightEntity);
						newRightIds.set(j, fromLeftId);
					}
				}
				rightEntityList.set(currentRightIndex, toRightEntity);
				newRightIds.set(currentRightIndex, toRightId);
				swapChainedValues(fromRightEntity, toRightEntity, rightScoreDirector);
				currentRightIndex++;
				currentRightIndex %= entitySize;
			}
		}
	}
}
