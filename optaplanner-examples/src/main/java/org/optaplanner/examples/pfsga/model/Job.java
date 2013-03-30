package org.optaplanner.examples.pfsga.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.value.ValueRange;
import org.optaplanner.core.api.domain.value.ValueRangeType;
import org.optaplanner.core.api.domain.value.ValueRanges;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@PlanningEntity
@XStreamAlias("Job")
public class Job extends AbstractPersistable implements UnitOfWork {

	private MachineTimeSequence machineTimes;
	private UnitOfWork previousUnitOfWork;

	@Override
	public MachineTimeSequence getMachineTimeSequence() {
		return machineTimes;
	}

	public void setMachineTimeSequence(MachineTimeSequence machineTimes) {
		this.machineTimes = machineTimes;
	}

	@PlanningVariable(chained = true)
	@ValueRanges({
			@ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty = "startingJobs"),
			@ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty = "jobs",
					excludeUninitializedPlanningEntity = true)})
	public UnitOfWork getPreviousUnitOfWork() {
		return previousUnitOfWork;
	}

	public void setPreviousUnitOfWork(UnitOfWork previousUnitOfWork) {
		this.previousUnitOfWork = previousUnitOfWork;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Job cloneJob() {
		Job clone = new Job();
		clone.id = id;
		clone.machineTimes = machineTimes;
		return clone;
	}

}