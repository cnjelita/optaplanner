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

package org.optaplanner.core.geneticalgorithm.event;

import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.solver.event.SolverLifecycleListenerAdapter;

public class GeneticAlgorithmSolverPhaseLifeCycleListenerAdapter extends SolverLifecycleListenerAdapter
        implements GeneticAlgorithmSolverPhaseLifeCycleListener {

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope solverPhaseScope) {
        // Hook method
    }

    @Override
    public void phaseEnded(GeneticAlgorithmSolverPhaseScope solverPhaseScope) {
        // Hook method
    }

    @Override
    public void stepStarted(GeneticAlgorithmStepScope stepScope) {
        // Hook method
    }

    @Override
    public void stepEnded(GeneticAlgorithmStepScope stepScope) {
        // Hook method
    }
}
