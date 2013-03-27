/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.cloudbalancingga.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.cloner.PlanningCloneable;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.buildin.hardsoft.HardSoftScoreDefinition;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.persistence.xstream.XStreamScoreConverter;

@PlanningSolution
@XStreamAlias("CloudBalance")
public class CloudBalance extends AbstractPersistable implements Individual<HardSoftScore>,
		PlanningCloneable<CloudBalance> {

	private List<CloudComputer> computerList;

	private List<CloudProcess> processList;

	@XStreamConverter(value = XStreamScoreConverter.class, types = {HardSoftScoreDefinition.class})
	private HardSoftScore score;

	private Map<CloudProcess, Long> processToIdMap;
	private Map<Long, CloudProcess> idToProcessMap;
	private int entitySize;

	public List<CloudComputer> getComputerList() {
		return computerList;
	}

	public void setComputerList(List<CloudComputer> computerList) {
		this.computerList = computerList;
	}

	@PlanningEntityCollectionProperty
	public List<CloudProcess> getProcessList() {
		return processList;
	}

	public void setProcessList(List<CloudProcess> processList) {
		this.processList = processList;
	}

	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	// ************************************************************************
	// Complex methods
	// ************************************************************************

	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(computerList);
		// Do not add the planning entity's (processList) because that will be done automatically
		return facts;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (id == null || !(o instanceof CloudBalance)) {
			return false;
		} else {
			CloudBalance other = (CloudBalance) o;
			if (processList.size() != other.processList.size()) {
				return false;
			}
			for (Iterator<CloudProcess> it = processList.iterator(), otherIt = other.processList.iterator(); it.hasNext(); ) {
				CloudProcess process = it.next();
				CloudProcess otherProcess = otherIt.next();
				// Notice: we don't use equals()
				if (!process.solutionEquals(otherProcess)) {
					return false;
				}
			}
			return true;
		}
	}

	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		for (CloudProcess process : processList) {
			// Notice: we don't use hashCode()
			hashCodeBuilder.append(process.solutionHashCode());
		}
		return hashCodeBuilder.toHashCode();
	}

	@Override
	public Object getEntityByClassAndId(Class clazz, Long id) {
		return idToProcessMap.get(id);
	}

	@Override
	public long getEntityId(Object entity) {
		return processToIdMap.get(entity);
	}

	//TODO Solution should be cloned in GA before anything happens, otherwise there are no id maps
	public void generateIdMaps() {
		processToIdMap = new HashMap<CloudProcess, Long>(processList.size());
		idToProcessMap = new HashMap<Long, CloudProcess>(processList.size());
		for (CloudProcess process : processList) {
			processToIdMap.put(process, process.getId());
			idToProcessMap.put(process.getId(), process);
		}
		this.entitySize = processToIdMap.size();
	}

	@Override
	public int getEntitySize(Class<?> entityClass) {
		return entitySize;
	}

	@Override
	public CloudBalance planningClone() {
		CloudBalance clone = new CloudBalance();
		clone.id = id;
		clone.computerList = computerList;
		List<CloudProcess> clonedProcessList = new ArrayList<CloudProcess>(
				processList.size());
		for (CloudProcess process : processList) {
			CloudProcess clonedProcess = process.cloneProcess();
			clonedProcessList.add(clonedProcess);
		}
		clone.processList = clonedProcessList;
		clone.generateIdMaps();
		clone.score = score;
		return clone;
	}
}
