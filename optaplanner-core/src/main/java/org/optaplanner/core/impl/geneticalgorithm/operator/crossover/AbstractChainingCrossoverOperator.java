package org.optaplanner.core.impl.geneticalgorithm.operator.crossover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.optaplanner.core.impl.domain.entity.PlanningEntityDescriptor;
import org.optaplanner.core.impl.domain.variable.PlanningVariableDescriptor;
import org.optaplanner.core.impl.heuristic.selector.move.generic.chained.ChainedSwapMove;
import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public abstract class AbstractChainingCrossoverOperator extends
		AbstractCrossoverOperator {

	private PlanningVariableDescriptor chainedPlanningVariableDescriptor;

	protected List<Object> getOrderedList(ScoreDirector scoreDirector) {
		Object entity = solutionDescriptor.getPlanningEntityDescriptor(entityClass).extractEntities(
				scoreDirector.getWorkingSolution()).get(0);
		List<Object> orderedList = new ArrayList<Object>();
		PlanningEntityDescriptor entityDescriptor = chainedPlanningVariableDescriptor.getPlanningEntityDescriptor();
		while (scoreDirector.getTrailingEntity(chainedPlanningVariableDescriptor, entity) != null) {
			entity = scoreDirector.getTrailingEntity(chainedPlanningVariableDescriptor, entity);
		}
		while (entity != null && entityDescriptor.appliesToPlanningEntity(entity)) {
			orderedList.add(entity);
			entity = chainedPlanningVariableDescriptor.getValue(entity);
		}
		Collections.reverse(orderedList);
		return orderedList;
	}

	public void setChainedPlanningVariableDescriptor(PlanningVariableDescriptor planningVariableDescriptor) {
		this.chainedPlanningVariableDescriptor = planningVariableDescriptor;
	}

	protected void performMove(ScoreDirector leftScoreDirector, Object fromObject, Object toObject) {
		Move move = new ChainedSwapMove(planningVariableDescriptors, fromObject, toObject);
		move.doMove(leftScoreDirector);
	}
}
