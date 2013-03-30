package org.optaplanner.examples.pfsga.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.buildin.simple.SimpleScoreDefinition;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.persistence.xstream.XStreamScoreConverter;

@PlanningSolution(solutionCloner = PermutationFlowShopCloner.class)
@XStreamAlias("PermutationFlowShop")
public class PermutationFlowShop extends AbstractPersistable implements Individual<SimpleScore> {

	private List<Job> jobs;
	private List<StartingJob> startingJobs;
	private List<MachineTimeSequence> machineTimeSequences;
	private int numberOfMachines;
	private int numberOfJobs;

	@XStreamConverter(value = XStreamScoreConverter.class, types = {SimpleScoreDefinition.class})
	private SimpleScore score;

	@PlanningEntityCollectionProperty
	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<StartingJob> getStartingJobs() {
		return startingJobs;
	}

	public void setStartingJobs(List<StartingJob> startingJobs) {
		this.startingJobs = startingJobs;
	}

	public List<MachineTimeSequence> getMachineTimeSequences() {
		return machineTimeSequences;
	}

	public void setMachineTimeSequences(List<MachineTimeSequence> machineTimeSequences) {
		this.machineTimeSequences = machineTimeSequences;
	}

	@Override
	public SimpleScore getScore() {
		return score;
	}

	@Override
	public void setScore(SimpleScore score) {
		this.score = score;
	}

	@Override
	public Collection<?> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(machineTimeSequences);
		facts.addAll(startingJobs);
		return facts;
	}

	public int getNumberOfMachines() {
		return numberOfMachines;
	}

	public void setNumberOfJobs(int numberOfJobs) {
		this.numberOfJobs = numberOfJobs;
	}

	public int getNumberOfJobs() {
		return numberOfJobs;
	}

	public void setNumberOfMachines(int numberOfMachines) {
		this.numberOfMachines = numberOfMachines;
	}

	@Override
	public Object getEntityByClassAndId(Class clazz, Long id) {
		return jobs.get(id.intValue()-1);
	}

	@Override
	public long getEntityId(Object entity) {
		return ((Job) entity).getId();
	}

	@Override
	public int getEntitySize(Class<?> entityClass) {
		return numberOfJobs - 1;
	}
}
