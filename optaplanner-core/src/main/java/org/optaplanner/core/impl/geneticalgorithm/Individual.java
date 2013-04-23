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

package org.optaplanner.core.impl.geneticalgorithm;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.solution.Solution;

//TODO HACK in order to be able to support crossover operation
//(helps find corresponding entities in different solutions)
public interface Individual<S extends Score> extends Solution<S> {

    /**
     * Returns an entity given an id. The id should be the
     * same for corresponding entities in different solutions.
     *
     * @param clazz - the class of the entity
     * @param id    - the id of the entity
     * @return the entity itself
     */
    public Object getEntityByClassAndId(Class clazz, Long id);

    /**
     * Returns the id of a given planning entity
     *
     * @param entity - the entity of which we want the id
     * @return
     */
    public long getEntityId(Object entity);

    int getEntitySize(Class<?> entityClass);
}
