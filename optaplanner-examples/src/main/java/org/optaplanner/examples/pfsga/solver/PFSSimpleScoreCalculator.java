package org.optaplanner.examples.pfsga.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.score.director.simple.SimpleScoreCalculator;
import org.optaplanner.examples.pfsga.model.Job;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;
import org.optaplanner.examples.pfsga.model.StartingJob;
import org.optaplanner.examples.pfsga.model.UnitOfWork;

public class PFSSimpleScoreCalculator implements SimpleScoreCalculator<PermutationFlowShop> {

	@Override
	public Score calculateScore(PermutationFlowShop solution) {
		StartingJob startingJob = solution.getStartingJobs().get(0);
		Map<UnitOfWork, UnitOfWork> inverseChain = new HashMap<UnitOfWork, UnitOfWork>();
		for (Job job : solution.getJobs()) {
			inverseChain.put(job.getPreviousUnitOfWork(), job);
		}
		int numberOfMachines = solution.getNumberOfMachines();
		int[] times = new int[numberOfMachines];
		UnitOfWork currentUnitOfWork = startingJob;
//		List<Integer> timerPerMachine = currentUnitOfWork.getMachineTimeSequence().getMachineTimes();
//		times[0] = timerPerMachine.get(0);
//		for (int j = 1; j < numberOfMachines; j++) {
//			times[j] = times[j - 1] + timerPerMachine.get(j);
//		}
		List<Integer> timePerMachine;
		while ((currentUnitOfWork = inverseChain.get(currentUnitOfWork)) != null) {
			timePerMachine = currentUnitOfWork.getMachineTimeSequence().getMachineTimes();
			times[0] = times[0] + timePerMachine.get(0);
			for (int i = 1; i < numberOfMachines; i++) {
				if (times[i - 1] > times[i]) {
					times[i] = times[i - 1] + timePerMachine.get(i);
				} else {
					times[i] += timePerMachine.get(i);
				}
			}
		}
		int score = -times[times.length - 1];
		return SimpleScore.valueOf(score);
	}
}
