<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <benchmarkDirectory>local/data/cloudbalancingga/template</benchmarkDirectory>
  <parallelBenchmarkCount>2</parallelBenchmarkCount>
  <!--<warmUpSecondsSpend>4</warmUpSecondsSpend>-->

  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <xstreamAnnotatedClass>org.optaplanner.examples.cloudbalancingga.domain.CloudBalance</xstreamAnnotatedClass>
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/cb-0002comp-0006proc.xml</inputSolutionFile>-->
      <!--<inputSolutionFile>data/cloudbalancingga/unsolved/cb-0003comp-0009proc.xml</inputSolutionFile>-->
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/cb-0004comp-0012proc.xml</inputSolutionFile>-->
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/cb-0100comp-0300proc.xml</inputSolutionFile>  -->
      <!--<inputSolutionFile>data/cloudbalancing/unsolved/cb-0200comp-0600proc.xml</inputSolutionFile>  -->
      <inputSolutionFile>data/cloudbalancing/unsolved/cb-0400comp-1200proc.xml</inputSolutionFile>
      <inputSolutionFile>data/cloudbalancing/unsolved/cb-0800comp-2400proc.xml</inputSolutionFile>
      <problemStatisticType>BEST_SOLUTION_CHANGED</problemStatisticType>
    </problemBenchmarks>
    <solver>
      <solutionClass>org.optaplanner.examples.cloudbalancingga.domain.CloudBalance</solutionClass>
      <planningEntityClass>org.optaplanner.examples.cloudbalancingga.domain.CloudProcess</planningEntityClass>
      <scoreDirectorFactory>
        <scoreDefinitionType>HARD_SOFT</scoreDefinitionType>
        <incrementalScoreCalculatorClass>org.optaplanner.examples.cloudbalancingga.solver.score.CloudBalancingIncrementalScoreCalculator</incrementalScoreCalculatorClass>
      </scoreDirectorFactory>
      <termination>
        <maximumMinutesSpend>5</maximumMinutesSpend>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

  <#list [20, 40, 80] as popsize>
  <solverBenchmark>
    <name>Genetic Algorithm with stochasticUniversalSampling popsize ${popsize}</name>
    <solver>
      <!--<constructionHeuristic>-->
      <!--<constructionHeuristicType>FIRST_FIT_DECREASING</constructionHeuristicType>-->
      <!--<constructionHeuristicPickEarlyType>FIRST_LAST_STEP_SCORE_EQUAL_OR_IMPROVING</constructionHeuristicPickEarlyType>-->
      <!--</constructionHeuristic>-->
      <geneticAlgorithm>
        <populationParameters>
          <populationSize>${popsize}</populationSize>
          <elitistSize>0</elitistSize>
        </populationParameters>
        <rankBasedSelector/>
        <mutationOperator>
          <unionMoveSelector>
            <swapMoveSelector/>
            <changeMoveSelector/>
          </unionMoveSelector>
        </mutationOperator>
        <uniformCrossoverOperator/>
        <replacementStrategyType>KEEP_NEW</replacementStrategyType>
      </geneticAlgorithm>
    </solver>
  </solverBenchmark>
</#list>
    </plannerBenchmark>
