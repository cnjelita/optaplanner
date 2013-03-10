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

import java.util.Comparator;

import org.optaplanner.core.score.director.ScoreDirector;

public class ScoreDirectorComparator implements Comparator<ScoreDirector> {

    @Override
    public int compare(ScoreDirector o1, ScoreDirector o2) {
        return o1.getWorkingSolution().getScore().compareTo(o2.getWorkingSolution().getScore());
    }
}
