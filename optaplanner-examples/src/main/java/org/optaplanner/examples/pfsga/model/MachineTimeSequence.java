package org.optaplanner.examples.pfsga.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@XStreamAlias("MachineTimeSequence")
public class MachineTimeSequence extends AbstractPersistable {

	private List<Integer> machineTimes;

	public List<Integer> getMachineTimes() {
		return machineTimes;
	}

	public void setMachineTimes(List<Integer> machineTimes) {
		this.machineTimes = machineTimes;
	}
}
