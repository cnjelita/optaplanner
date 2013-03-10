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

package org.optaplanner.core.geneticalgorithm;

import org.optaplanner.core.geneticalgorithm.event.GeneticAlgorithmSolverPhaseLifeCycleListener;
import org.optaplanner.core.geneticalgorithm.initializer.PopulationInitializer;
import org.optaplanner.core.geneticalgorithm.operator.crossover.CrossoverOperator;
import org.optaplanner.core.geneticalgorithm.operator.mutation.MutationOperator;
import org.optaplanner.core.geneticalgorithm.operator.selector.SolutionSelector;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmSolverPhaseScope;
import org.optaplanner.core.geneticalgorithm.scope.GeneticAlgorithmStepScope;
import org.optaplanner.core.phase.AbstractSolverPhase;
import org.optaplanner.core.solver.scope.DefaultSolverScope;

public class GeneticAlgorithmSolverPhase extends AbstractSolverPhase
        implements GeneticAlgorithmSolverPhaseLifeCycleListener {

    private int populationSize;

    private boolean assertStepScoreIsUncorrupted;

    private SolutionSelector solutionSelector;
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;
    private PopulationInitializer populationInitializer;

    public void setSolutionSelector(SolutionSelector solutionSelector) {
        this.solutionSelector = solutionSelector;
    }

    public SolutionSelector getSolutionSelector() {
        return solutionSelector;
    }

    public void setCrossoverOperator(CrossoverOperator crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    public CrossoverOperator getCrossoverOperator() {
        return crossoverOperator;
    }

    public void setMutationOperator(MutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public MutationOperator getMutationOperator() {
        return mutationOperator;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setAssertStepScoreIsUncorrupted(boolean assertStepScoreIsUncorrupted) {
        this.assertStepScoreIsUncorrupted = assertStepScoreIsUncorrupted;
    }

    public boolean isAssertStepScoreIsUncorrupted() {
        return assertStepScoreIsUncorrupted;
    }

    public void setPopulationInitializer(PopulationInitializer populationInitializer) {
        this.populationInitializer = populationInitializer;
    }

    public PopulationInitializer getPopulationInitializer() {
        return populationInitializer;
    }

    @Override
    public void solve(DefaultSolverScope solverScope) {
        GeneticAlgorithmSolverPhaseScope phaseScope = new GeneticAlgorithmSolverPhaseScope(solverScope);
        phaseStarted(phaseScope);

        populationInitializer.initializePopulation(phaseScope);

        GeneticAlgorithmStepScope stepScope = createNextStepScope(phaseScope, null);

        while (!termination.isPhaseTerminated(phaseScope)) {
            stepStarted(stepScope);

            solutionSelector.selectParents(stepScope);
            //TODO perform crossover on parents to form children
            //TODO perform mutation on children


             /*
             * TODO perform replacement of individuals from new population and old one (done in stepEnded?)
             * where to do assessment? During replacement or before? Where to do score assertion?
             * stepEnded(StepScope)
             * if (assertStepScoreIsUncorrupted) {
             *   phaseScope.assertWorkingScoreFromScratch(stepScope.getScore());
             *   phaseScope.assertExpectedWorkingScore(stepScope.getScore());
             * }
             */
            stepScope = createNextStepScope(phaseScope, stepScope);
        }

        phaseEnded(phaseScope);
    }

    //TODO do genetic algorithms really use step index, or can this method be removed?
    private GeneticAlgorithmStepScope createNextStepScope(GeneticAlgorithmSolverPhaseScope phaseScope,
            GeneticAlgorithmStepScope completedStepScope) {
        GeneticAlgorithmStepScope stepScope = new GeneticAlgorithmStepScope(phaseScope);
        if (completedStepScope == null) {
            stepScope.setStepIndex(0);
        } else {
            stepScope.setStepIndex(1);
        }
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void solvingStarted(DefaultSolverScope solverScope) {
        super.solvingStarted(solverScope);
        //TODO implement
    }

    @Override
    public void solvingEnded(DefaultSolverScope solverScope) {
        super.solvingEnded(solverScope);
        //TODO implement
    }

    @Override
    public void phaseStarted(GeneticAlgorithmSolverPhaseScope phaseScope) {
        //TODO Should something else be done when phase starts?
        super.phaseStarted(phaseScope);
        mutationOperator.phaseStarted(phaseScope);
        crossoverOperator.phaseStarted(phaseScope);
        solutionSelector.phaseStarted(phaseScope);
        populationInitializer.phaseStarted(phaseScope);
    }

    @Override
    public void phaseEnded(GeneticAlgorithmSolverPhaseScope phaseScope) {
        //TODO Should something else be done when phase ends?
        super.phaseEnded(phaseScope);
        mutationOperator.phaseEnded(phaseScope);
        crossoverOperator.phaseEnded(phaseScope);
        solutionSelector.phaseEnded(phaseScope);
        populationInitializer.phaseEnded(phaseScope);

        logger.info("Phase ({}) geneticAlgorithm ended: step total ({}), time spend ({}), best score ({}).",
                phaseIndex,
                phaseScope.getLastCompletedStepScope().getStepIndex() + 1,
                phaseScope.calculateSolverTimeMillisSpend(),
                phaseScope.getBestScore());
    }

    @Override
    public void stepStarted(GeneticAlgorithmStepScope stepScope) {
        //TODO Should something else be done when step starts?
        super.stepStarted(stepScope);
        mutationOperator.stepStarted(stepScope);
        crossoverOperator.stepStarted(stepScope);
        //TODO solution selector should calculate fitness of individuals in generation during stepStarted
        solutionSelector.stepStarted(stepScope);
    }

    @Override
    public void stepEnded(GeneticAlgorithmStepScope stepScope) {
        //TODO Should something else be done when step ends?
        super.stepEnded(stepScope);
        mutationOperator.stepEnded(stepScope);
        crossoverOperator.stepEnded(stepScope);
        solutionSelector.stepEnded(stepScope);

        GeneticAlgorithmSolverPhaseScope phaseScope = stepScope.getPhaseScope();
        if (assertStepScoreIsUncorrupted) {
            stepScope.getPhaseScope().getSolverScope().getScoreDirector()
                    .setWorkingSolution(stepScope.createOrGetClonedSolution());
        }
        if (logger.isDebugEnabled()) {
            long timeMillisSpend = phaseScope.calculateSolverTimeMillisSpend();
            logger.debug("    Step index ({}), time spend ({}), score ({}), {} best score ({})",
                    new Object[]{stepScope.getStepIndex(), timeMillisSpend,
                            stepScope.getScore(),
                            (stepScope.getBestScoreImproved() ? "new" : "   "),
                            phaseScope.getBestScore()
                    });
        }
    }

}
