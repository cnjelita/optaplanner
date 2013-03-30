package org.optaplanner.examples.pfsga.app;

import java.io.File;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.solver.XmlSolverFactory;
import org.optaplanner.examples.pfsga.model.PermutationFlowShop;
import org.optaplanner.examples.pfsga.persistence.PFSSolutionImporter;

public class PFSHelloWorldGA {

	public static final String SOLVER_CONFIG
			= "/org/optaplanner/examples/pfsga/solver/pfsgaSolverConfig.xml";

	public static void main(String[] args) {

		// Build the Solver
		XmlSolverFactory solverFactory = new XmlSolverFactory();
		Solver solver = solverFactory.configure(SOLVER_CONFIG).buildSolver();

		File f = new File("optaplanner-examples/data/pfsga/input/reC41.txt");
		PermutationFlowShop instance = (PermutationFlowShop) new PFSSolutionImporter().readSolution(f);
		solver.setPlanningProblem(instance);
		solver.solve();
	}
}
