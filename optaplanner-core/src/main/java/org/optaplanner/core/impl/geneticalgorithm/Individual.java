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

public interface Individual<S extends Score> extends Solution<S> {

	public Object getEntityByClassAndId(Class clazz, Long id);

	public long getEntityId(Object entity);

	int getEntitySize(Class<?> entityClass);
}
