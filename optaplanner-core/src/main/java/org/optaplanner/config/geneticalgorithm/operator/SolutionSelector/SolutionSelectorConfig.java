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

package org.optaplanner.config.geneticalgorithm.operator.SolutionSelector;

import org.optaplanner.core.domain.solution.SolutionDescriptor;
import org.optaplanner.core.geneticalgorithm.operator.selector.SolutionSelector;

//TODO add possible selection operators for XStream
//TODO rename to selection operator?
public abstract class SolutionSelectorConfig {

    public abstract SolutionSelector buildSolutionSelector(SolutionDescriptor solutionDescriptor);
}
