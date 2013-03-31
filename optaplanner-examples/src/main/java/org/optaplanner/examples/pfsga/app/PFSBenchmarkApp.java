package org.optaplanner.examples.pfsga.app;

import org.optaplanner.examples.common.app.CommonBenchmarkApp;

public class PFSBenchmarkApp extends CommonBenchmarkApp {

	public static final String DEFAULT_BENCHMARK_CONFIG
			= "/org/optaplanner/examples/pfsga/benchmark/PFSGABenchmarkConfig.xml";

	public static void main(String[] args) {
		String benchmarkConfig;
		if (args.length > 0) {
			if (args[0].equals("default")) {
				benchmarkConfig = DEFAULT_BENCHMARK_CONFIG;
			} else {
				throw new IllegalArgumentException("The program argument (" + args[0] + ") is not supported.");
			}
		} else {
			benchmarkConfig = DEFAULT_BENCHMARK_CONFIG;
		}
		new PFSBenchmarkApp().buildAndBenchmark(benchmarkConfig);
	}
}
