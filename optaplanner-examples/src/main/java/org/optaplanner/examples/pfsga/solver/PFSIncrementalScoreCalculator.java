package org.optaplanner.examples.pfsga.solver;

import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.score.director.incremental.AbstractIncrementalScoreCalculator;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;
import org.optaplanner.examples.pfsga.model.StartingJob;

public class PFSIncrementalScoreCalculator extends AbstractIncrementalScoreCalculator<PermutationFlowShop> {

	private int score;
	private StartingJob startingJob;
	private Map<Long, List<Integer>> usedTimesPerMachineAfterJob;

	@Override
	public void resetWorkingSolution(PermutationFlowShop workingSolution) {
		startingJob = workingSolution.getStartingJobs().get(0);
	}

	@Override
	public void beforeEntityAdded(Object entity) {
		//TODO implement
	}

	@Override
	public void afterEntityAdded(Object entity) {
		//TODO implement
	}

	@Override
	public void beforeAllVariablesChanged(Object entity) {
		//TODO implement
	}

	@Override
	public void afterAllVariablesChanged(Object entity) {
		//TODO implement
	}

	@Override
	public void beforeVariableChanged(Object entity, String variableName) {
		//TODO implement
	}

	@Override
	public void afterVariableChanged(Object entity, String variableName) {
		//TODO implement
	}

	@Override
	public void beforeEntityRemoved(Object entity) {
		//TODO implement
	}

	@Override
	public void afterEntityRemoved(Object entity) {
		//TODO implement
	}

	@Override
	public Score calculateScore() {
		return SimpleScore.valueOf(score);
	}

	@Override
	public boolean isCloneable() {
		return true;
	}

//	@Override
//	public AbstractIncrementalScoreCalculator clone() {
//		PFSIncrementalScoreCalculator clone = new PFSIncrementalScoreCalculator();
//		clone.inverseChain = inverseChain;
//		clone.score = score;
//		clone.startingJob = startingJob;
//
//	}
}
