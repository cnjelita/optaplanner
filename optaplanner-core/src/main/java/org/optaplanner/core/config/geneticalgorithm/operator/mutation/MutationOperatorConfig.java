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

package org.optaplanner.core.config.geneticalgorithm.operator.mutation;

import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.collections.CollectionUtils;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.move.MoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.composite.UnionMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.SwapMoveSelectorConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.mutation.MutationOperator;
import org.optaplanner.core.impl.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.impl.heuristic.selector.move.MoveSelector;

@XStreamAlias("mutationOperator")
public class MutationOperatorConfig {

	// TODO This is a List due to XStream limitations. With JAXB it could be just a MoveSelectorConfig instead.
	@XStreamImplicit()
	private List<MoveSelectorConfig> moveSelectorConfigList = null;
	//TODO maybe add something like mutationRate?

	public MutationOperator buildMutationOperator(EnvironmentMode environmentMode,
			SolutionDescriptor solutionDescriptor, SelectionCacheType defaultCacheType,
			SelectionOrder defaultSelectionOrder) {

		MutationOperator mutationOperator = new MutationOperator();
		mutationOperator.setMoveSelector(buildMoveSelector(environmentMode, solutionDescriptor, defaultCacheType,
				defaultSelectionOrder));
		return mutationOperator;
	}

	private MoveSelector buildMoveSelector(EnvironmentMode environmentMode, SolutionDescriptor solutionDescriptor,
			SelectionCacheType cacheType, SelectionOrder selectionOrder) {
		MoveSelector moveSelector;
		SelectionCacheType defaultCacheType = cacheType;
		SelectionOrder defaultSelectionOrder = selectionOrder;
		if (CollectionUtils.isEmpty(moveSelectorConfigList)) {
			// Default to changeMoveSelector and swapMoveSelector
			UnionMoveSelectorConfig unionMoveSelectorConfig = new UnionMoveSelectorConfig();
			unionMoveSelectorConfig.setMoveSelectorConfigList(Arrays.asList(
					new ChangeMoveSelectorConfig(), new SwapMoveSelectorConfig()));
			moveSelector = unionMoveSelectorConfig.buildMoveSelector(environmentMode, solutionDescriptor,
					defaultCacheType, defaultSelectionOrder);
		} else if (moveSelectorConfigList.size() == 1) {
			moveSelector = moveSelectorConfigList.get(0).buildMoveSelector(
					environmentMode, solutionDescriptor, defaultCacheType, defaultSelectionOrder);
			//  TODO FAIL FAST if cacheType is something other than jit? Can be updated?
//			System.out.println(moveSelector.getCacheType());
			if (moveSelector.getCacheType() != null &&
					moveSelector.getCacheType() != defaultCacheType) {
				throw new IllegalArgumentException("Only JIT move selectors are allowed for mutation operator in " +
						"genetic algorithms.");
			}
		} else {
			throw new IllegalArgumentException("The moveSelectorConfigList (" + moveSelectorConfigList
					+ ") must a singleton or empty. Use a single " + UnionMoveSelectorConfig.class
					+ " element to nest multiple MoveSelectors.");
		}
		return moveSelector;
	}

}
