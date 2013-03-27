package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.impl.heuristic.selector.move.generic.chained.ChainedSwapMove;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class CycleCrossoverOperator extends AbstractChainingCrossoverOperator {

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
//					System.out.println("b");
				} else {
//					System.out.println("a");
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
					Object fromObject = leftEntityList.get(j);
					Object toObject = leftParent.getEntityByClassAndId(entityClass, originalRightIds.get(j));
					Move move = new ChainedSwapMove(variableDescriptors, fromObject, toObject);
					move.doMove(leftScoreDirector);
//					System.out.println(j);
				}
			}
		}
	}
}
