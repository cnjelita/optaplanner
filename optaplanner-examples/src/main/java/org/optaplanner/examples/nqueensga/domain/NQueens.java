/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.nqueensga.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.geneticalgorithm.Individual;
import org.optaplanner.core.impl.score.buildin.simple.SimpleScoreDefinition;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.persistence.xstream.XStreamScoreConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@PlanningSolution(solutionCloner = NQueens.class)
@XStreamAlias("NQueens")
public class NQueens extends AbstractPersistable implements Individual<SimpleScore>, SolutionCloner<NQueens> {

    private int n;

    // Problem facts
    private List<Column> columnList;
    private List<Row> rowList;

    private List<Queen> queenList;

    @XStreamConverter(value = XStreamScoreConverter.class, types = {SimpleScoreDefinition.class})
    private SimpleScore score;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public List<Row> getRowList() {
        return rowList;
    }

    public void setRowList(List<Row> rowList) {
        this.rowList = rowList;
    }

    @PlanningEntityCollectionProperty
    public List<Queen> getQueenList() {
        return queenList;
    }

    public void setQueenList(List<Queen> queenList) {
        this.queenList = queenList;
    }

    public SimpleScore getScore() {
        return score;
    }

    public void setScore(SimpleScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.addAll(columnList);
        facts.addAll(rowList);
        // Do not add the planning entity's (queenList) because that will be done automatically
        return facts;
    }

    @Override
    public Object getEntityByClassAndId(Class clazz, Long id) {
        return queenList.get(id.intValue());
    }

    @Override
    public long getEntityId(Object entity) {
        return ((Queen) entity).getId();
    }

    @Override
    public int getEntitySize(Class<?> entityClass) {
        return n;
    }

    @Override
    public NQueens cloneSolution(NQueens original) {
        NQueens clone = new NQueens();
        clone.rowList = original.rowList;
        clone.columnList = original.columnList;
        List<Queen> newQueenList = new ArrayList<Queen>();
        for (Queen q : original.queenList) {
            newQueenList.add(q.cloneQueen());
        }
        clone.queenList = newQueenList;
        clone.id = original.id;
        clone.score = original.score;
        clone.n = original.n;
        return clone;
    }
}
