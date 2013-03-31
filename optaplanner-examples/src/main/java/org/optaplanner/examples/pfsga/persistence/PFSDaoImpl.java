package org.optaplanner.examples.pfsga.persistence;

import org.optaplanner.examples.common.persistence.XStreamSolutionDaoImpl;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;

public class PFSDaoImpl extends XStreamSolutionDaoImpl {

	public PFSDaoImpl() {
		super("pfsga", PermutationFlowShop.class);
	}
}
