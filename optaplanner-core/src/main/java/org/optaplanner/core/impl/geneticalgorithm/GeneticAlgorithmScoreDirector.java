//package org.optaplanner.core.impl.geneticalgorithm;
//
//import org.optaplanner.core.impl.score.director.AbstractScoreDirector;
//import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreCalculator;
//import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreDirector;
//import org.optaplanner.core.impl.score.director.incremental.IncrementalScoreDirectorFactory;
//import org.optaplanner.core.impl.solution.Solution;
//
//public class GeneticAlgorithmScoreDirector extends IncrementalScoreDirector {
//
//	public GeneticAlgorithmScoreDirector(
//			IncrementalScoreDirectorFactory scoreDirectorFactory,
//			IncrementalScoreCalculator incrementalScoreCalculator) {
//		super(scoreDirectorFactory, incrementalScoreCalculator);
//	}
//
//	public GeneticAlgorithmScoreDirector(IncrementalScoreDirector scoreDirector) {
//		super(scoreDirector.getScoreDirectorFactory(),
//				scoreDirector.getIncrementalScoreCalculator().cloneScoreCalculator());
//	}
//
//	@Override
//	public AbstractScoreDirector clone() {
//		IncrementalScoreDirector clone = scoreDirectorFactory.buildScoreDirector(this);
//		clone.setWorkingSolution(cloneWorkingSolution());
//		return clone;
//	}
//
//	@Override
//	public void setWorkingSolution(Solution workingSolution) {
//		this.workingSolution = workingSolution;
//	}
//}
