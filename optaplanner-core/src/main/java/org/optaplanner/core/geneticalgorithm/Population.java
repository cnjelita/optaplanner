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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.optaplanner.core.score.director.ScoreDirector;

//TODO should be in other package?
public class Population implements Iterable<ScoreDirector> {

    private List<ScoreDirector> individuals;
    private int populationSize;
    private ScoreDirector bestIndividual;

    public Population(int populationSize) {
        this.populationSize = populationSize;
        individuals = new ArrayList<ScoreDirector>(populationSize);
    }

    @Override
    public Iterator<ScoreDirector> iterator() {
        return individuals.iterator();
    }

    public void addIndividual(ScoreDirector scoreDirector) {
        individuals.add(scoreDirector);
    }

    public List<ScoreDirector> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<ScoreDirector> individuals) {
        this.individuals = individuals;
    }

    public void performScoreCalculation() {
        for (ScoreDirector individual : individuals) {
            individual.calculateScore();
        }
        Collections.sort(individuals, Collections.reverseOrder(new ScoreDirectorComparator()));
//        for (ScoreDirector individual : individuals){
//            System.out.println(individual.getWorkingSolution().getScore());
//        }
        //TODO calculate other statistics
    }

    public void setBestIndividual(ScoreDirector bestIndividual) {
        this.bestIndividual = bestIndividual;
    }

    public ScoreDirector getBestIndividual() {
        return bestIndividual;
    }
}
