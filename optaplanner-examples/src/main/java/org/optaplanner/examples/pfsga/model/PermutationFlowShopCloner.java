package org.optaplanner.examples.pfsga.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;

public class PermutationFlowShopCloner implements SolutionCloner<PermutationFlowShop> {

	@Override
	public PermutationFlowShop cloneSolution(PermutationFlowShop original) {
		PermutationFlowShop clone = new PermutationFlowShop();

		List<StartingJob> clonedStartingJobList = new ArrayList<StartingJob>();

		Map<Long, UnitOfWork> idToUnitOfWorkMap = new HashMap<Long, UnitOfWork>();
		for (StartingJob startingJob : original.getStartingJobs()) {
			StartingJob clonedStartingJob = startingJob.cloneStartingJob();
			idToUnitOfWorkMap.put(clonedStartingJob.getId(), clonedStartingJob);
			clonedStartingJobList.add(clonedStartingJob);
		}

		Map<Long, Long> unitOfWorkToPreviousUnitOfWorkMap = new HashMap<Long, Long>();
		List<Job> clonedJobList = new ArrayList<Job>();
		for (Job job : original.getJobs()) {
			Job clonedJob = job.cloneJob();
			if (job.getPreviousUnitOfWork() != null) {
				unitOfWorkToPreviousUnitOfWorkMap.put(clonedJob.getId(), job.getPreviousUnitOfWork().getId());
			}
			idToUnitOfWorkMap.put(clonedJob.getId(), clonedJob);
			clonedJobList.add(clonedJob);
		}

		for (Job job : clonedJobList) {
			job.setPreviousUnitOfWork(idToUnitOfWorkMap.get(unitOfWorkToPreviousUnitOfWorkMap.get(job.getId())));
		}

		clone.setJobs(clonedJobList);
		clone.setNumberOfMachines(original.getNumberOfMachines());
		clone.setNumberOfJobs(original.getNumberOfJobs());
		clone.setScore(original.getScore());
		clone.setStartingJobs(clonedStartingJobList);
		clone.setMachineTimeSequences(original.getMachineTimeSequences());

		return clone;
	}
}
