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

package org.optaplanner.core.config.geneticalgorithm.operator.solutionselector;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.impl.domain.solution.SolutionDescriptor;
import org.optaplanner.core.impl.geneticalgorithm.operator.selector.SolutionSelector;
import org.optaplanner.core.impl.geneticalgorithm.operator.selector.TruncationSelector;

@XStreamAlias("truncationSelector")
public class TruncationSelectorConfig extends SolutionSelectorConfig {

    private Double threshold = null;

    @Override
    public SolutionSelector buildSolutionSelector(SolutionDescriptor solutionDescriptor) {
        if (threshold == null) {
            throw new IllegalArgumentException("Please select a threshold for the truncation selector");
        }
        if (threshold > 1 || threshold <= 0) {
            throw new IllegalArgumentException("The threshold for the truncation selector should be between" +
                    " 0 and 1");
        }
        return new TruncationSelector(threshold);

    }
}
