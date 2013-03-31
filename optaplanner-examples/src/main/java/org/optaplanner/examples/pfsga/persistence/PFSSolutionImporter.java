package org.optaplanner.examples.pfsga.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionImporter;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.pfsga.model.Job;
import org.optaplanner.examples.pfsga.model.MachineTimeSequence;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;
import org.optaplanner.examples.pfsga.model.StartingJob;

public class PFSSolutionImporter extends AbstractTxtSolutionImporter {

	private static final String INPUT_FILE_SUFFIX = ".txt";

	public static void main(String[] args) {
		new PFSSolutionImporter().convertAll();
	}

	public PFSSolutionImporter() {
		super(new PFSDaoImpl());
	}

	protected PFSSolutionImporter(SolutionDao solutionDao) {
		super(solutionDao);
	}

	@Override
	public TxtInputBuilder createTxtInputBuilder() {
		return new PFSSolutionInputBuilder();
	}

	private class PFSSolutionInputBuilder extends TxtInputBuilder {

		@Override
		public Solution readSolution() throws IOException {
			PermutationFlowShop solution = new PermutationFlowShop();
			solution.setId(0L);
			Scanner sc = new Scanner(bufferedReader);
			sc.nextLine();
			int numberOfJobs = sc.nextInt();
			solution.setNumberOfJobs(numberOfJobs);
			int numberOfMachines = sc.nextInt();
			solution.setNumberOfMachines(numberOfMachines);
			sc.nextLine();
			sc.nextLine();
			long id = 0L;
			List<MachineTimeSequence> sequenceList = new ArrayList<MachineTimeSequence>();
			for (int i = 0; i < numberOfJobs; i++) {
				MachineTimeSequence sequence = new MachineTimeSequence();
				List<Integer> machineTimes = new ArrayList<Integer>();
				sequence.setMachineTimes(machineTimes);
				sequence.setId(id);
				sequenceList.add(sequence);
				for (int j = 0; j < numberOfMachines; j++) {
					machineTimes.add(sc.nextInt());
				}
				sc.nextLine();
				id++;
			}

			id = 0L;
			List<StartingJob> startingJobs = new ArrayList<StartingJob>();
			StartingJob startingJob = new StartingJob();
			startingJob.setId(id);
			startingJobs.add(startingJob);
			solution.setStartingJobs(startingJobs);
			id++;

			List<Job> jobList = new ArrayList<Job>();
			for (MachineTimeSequence sequence : sequenceList) {
				Job job = new Job();
				job.setId(id);
				job.setMachineTimeSequence(sequence);
				jobList.add(job);
				id++;
			}

			solution.setMachineTimeSequences(sequenceList);
			solution.setJobs(jobList);

			return solution;
		}
	}
}
