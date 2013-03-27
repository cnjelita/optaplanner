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

package org.optaplanner.core.impl.geneticalgorithm.replacementstrategy;

import org.optaplanner.core.impl.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListener;
import org.optaplanner.core.impl.geneticalgorithm.scope.GeneticAlgorithmStepScope;

//TODO should be in operator package?
public interface ReplacementStrategy extends GeneticAlgorithmSolverPhaseLifeCycleListener {

	/**
	 * Use individuals in current generation in phaseScope
	 * and intermediate population in stepScope to form a new
	 * generation. Scores need to be calculated before starting
	 * this process.
	 * @param stepScope
	 */
	//TODO maybe rename this method?
	void createNewGeneration(GeneticAlgorithmStepScope stepScope);
}
