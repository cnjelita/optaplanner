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

		//TODO this only works if all planning values are used!
		Set<Long> unchangedIndicesSet = new HashSet<Long>();
		int currentIndex = workingRandom.nextInt(entitySize);
		long firstId = originalLeftIds.get(currentIndex);
		unchangedIndicesSet.add(firstId);

		boolean cycle = false;
		while (!cycle) {
			long nextLeftElementId = originalRightIds.get(currentIndex);
			if (nextLeftElementId == firstId) {
				cycle = true;
			} else {
				unchangedIndicesSet.add(nextLeftElementId);
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
		for (int j = 0; j < entitySize; j++) {
			//TODO workaround because id's (not pseudo) can be anything and don't have to correspond to indices
			if (!unchangedIndicesSet.contains(leftEntityList.get(j))) {
				Object fromLeftObject = leftEntityList.get(j);
				Object toLeftObject = leftParent.getEntityByClassAndId(entityClass, originalRightIds.get(j));

				Object fromRightEntity = rightEntityList.get(j);
				Object toRightEntity = rightParent.getEntityByClassAndId(entityClass, originalLeftIds.get(j));

				performMove(leftScoreDirector, fromLeftObject, toLeftObject);
				performMove(rightScoreDirector, fromRightEntity, toRightEntity);
			}
		}
	}

}
