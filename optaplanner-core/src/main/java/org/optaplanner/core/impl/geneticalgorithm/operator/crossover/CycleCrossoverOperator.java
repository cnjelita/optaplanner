package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class CycleCrossoverOperator extends AbstractChainingCrossoverOperator {

	@Override
	protected void performCrossover(ScoreDirector leftScoreDirector, ScoreDirector rightScoreDirector) {
		Individual leftParent = (Individual) leftScoreDirector.getWorkingSolution();
		Individual rightParent = (Individual) rightScoreDirector.getWorkingSolution();

		List<Object> leftEntityList = getOrderedList(leftScoreDirector);
		List<Object> rightEntityList = getOrderedList(rightScoreDirector);

		//TODO can it be possible that their not same size?
		int entitySize = leftEntityList.size();
		if (rightEntityList.size() != entitySize) {
			throw new IllegalStateException("Both lists should be same size!");
		}

		List<Long> originalLeftIds = new ArrayList<Long>();
		List<Long> originalRightIds = new ArrayList<Long>();

		for (int j = 0; j < entitySize; j++) {
			originalLeftIds.add(leftParent.getEntityId(leftEntityList.get(j)));
			originalRightIds.add(rightParent.getEntityId(rightEntityList.get(j)));
		}

//		System.out.println("Original left id's \t \t" + originalLeftIds);
//		System.out.println("Original right id's \t" + originalRightIds);

		//TODO this only works if all planning values are used!
		Set<Integer> unchangedIndicesSet = new HashSet<Integer>();
		int currentIndex = workingRandom.nextInt(entitySize);
		unchangedIndicesSet.add(currentIndex);
		long firstId = originalLeftIds.get(currentIndex);

		boolean cycle = false;
		while (!cycle) {
			long nextLeftElementId = originalRightIds.get(currentIndex);
			if (nextLeftElementId == firstId) {
				cycle = true;
			} else {
				unchangedIndicesSet.add(currentIndex);
				boolean nextLeftElementFound = false;
				while (!nextLeftElementFound) {
					currentIndex += 1;
					currentIndex %= entitySize;
					if (nextLeftElementId == originalLeftIds.get(currentIndex)) {
						nextLeftElementFound = true;
					}
				}
			}
		}

		List<Long> newLeftIds = new ArrayList<Long>(originalLeftIds);
		List<Long> newRightIds = new ArrayList<Long>(originalRightIds);

		for (int j = 0; j < entitySize; j++) {
			//TODO workaround because id's (not pseudo) can be anything and don't have to correspond to indices
			if (!unchangedIndicesSet.contains(j)) {
				long leftId = newLeftIds.get(j);
				long originalRightId = originalRightIds.get(j);
				long rightId = newRightIds.get(j);
				long originalLeftId = originalLeftIds.get(j);

				Object oldLeftEntity = leftEntityList.get(j);
				Object oldRightEntity = rightEntityList.get(j);

				Object newLeftEntity = null;
				for (int i = 0; i < entitySize; i++) {
					if (newLeftIds.get(i) == originalRightId) {
						newLeftEntity = leftEntityList.get(i);
						leftEntityList.set(i, oldLeftEntity);
						newLeftIds.set(i, leftId);
					}
				}
				leftEntityList.set(j, newLeftEntity);
				newLeftIds.set(j, rightId);

				Object newRightEntity = null;
				for (int i = 0; i < entitySize; i++) {
					if (newRightIds.get(i) == originalLeftId) {
						newRightEntity = rightEntityList.get(i);
						rightEntityList.set(i, oldRightEntity);
						newRightIds.set(i, rightId);
					}
				}
				rightEntityList.set(j, newRightEntity);
				newRightIds.set(j, leftId);

				swapChainedValues(oldLeftEntity, newLeftEntity, leftScoreDirector);
				swapChainedValues(oldRightEntity, newRightEntity, rightScoreDirector);

			}
		}
	}

}
