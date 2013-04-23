package org.optaplanner.examples.pfsga.persistence;

import org.optaplanner.examples.common.persistence.XStreamSolutionDao;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;

public class PFSDaoImpl extends XStreamSolutionDao {

    public PFSDaoImpl() {
        super("pfsga", PermutationFlowShop.class);
    }
}
