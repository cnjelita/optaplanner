package org.optaplanner.examples.pfsga.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@XStreamAlias("StartingJob")
public class StartingJob extends AbstractPersistable implements UnitOfWork {

	@Override
	public MachineTimeSequence getMachineTimeSequence() {
		//Doing nothing, this class is a HACK
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StartingJob cloneStartingJob() {
		StartingJob clone = new StartingJob();
		clone.id = id;
		return clone;
	}
}
