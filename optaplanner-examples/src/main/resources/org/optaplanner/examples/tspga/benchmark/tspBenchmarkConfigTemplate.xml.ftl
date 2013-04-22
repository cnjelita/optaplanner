<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <benchmarkDirectory>local/data/tspga/params</benchmarkDirectory>
  <parallelBenchmarkCount>2</parallelBenchmarkCount>
  <!--<warmUpSecondsSpend>4</warmUpSecondsSpend>-->

  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <xstreamAnnotatedClass>org.optaplanner.examples.tspga.domain.TravelingSalesmanTour</xstreamAnnotatedClass>
      <inputSolutionFile>data/tsp/unsolved/dj38.xml</inputSolutionFile>
      <inputSolutionFile>data/tsp/unsolved/eil51.xml</inputSolutionFile>
      <!--<inputSolutionFile>data/tsp/unsolved/st70.xml</inputSolutionFile> -->
     <!-- <inputSolutionFile>data/tsp/unsolved/eil76.xml</inputSolutionFile>       -->
     <!-- <inputSolutionFile>data/tsp/unsolved/eil101.xml</inputSolutionFile>    -->
     <inputSolutionFile>data/tsp/unsolved/kroA100.xml</inputSolutionFile>
       <inputSolutionFile>data/tsp/unsolved/pcb442.xml</inputSolutionFile>
      <!--<inputSolutionFile>data/tsp/unsolved/lin318.xml</inputSolutionFile>  -->
       <inputSolutionFile>data/tsp/unsolved/lu980.xml</inputSolutionFile>
      <problemStatisticType>BEST_SOLUTION_CHANGED</problemStatisticType>
    </problemBenchmarks>
   <solver>
         <solutionClass>org.optaplanner.examples.tspga.domain.TravelingSalesmanTour</solutionClass>
         <planningEntityClass>org.optaplanner.examples.tspga.domain.Visit</planningEntityClass>
         <scoreDirectorFactory>
           <scoreDefinitionType>SIMPLE</scoreDefinitionType>
            <incrementalScoreCalculatorClass>org.optaplanner.examples.tspga.score.TspIncrementalScoreCalculator</incrementalScoreCalculatorClass>
         </scoreDirectorFactory>
       </solver>
  </inheritedSolverBenchmark>

  <#list [100, 200, 400, 600, 800, 1000] as popsize>
  <#list [0,1,2] as elitistSize>
  <solverBenchmark>
    <name>Genetic Algorithm with RBS elitistSize ${elitistSize} and popsize ${popsize}</name>
    <solver>
        <constructionHeuristic>
            <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
          </constructionHeuristic>
        <geneticAlgorithm>
          <termination>
            <!--<maximumStepCount>10000</maximumStepCount>      -->
           <maximumSecondsSpend>30</maximumSecondsSpend>
          </termination>
          <populationParameters>
            <populationSize>${popsize}</populationSize>
            <elitistSize>${elitistSize}</elitistSize>
          </populationParameters>
          <!--<stochasticUniversalSamplingSelector/>-->
          <rankBasedSelector/>
          <!--<orderBasedCrossoverOperator/>-->
          <unionCrossoverOperator>
            <cycleCrossoverOperator/>
            <!--<orderBasedCrossoverOperator/>-->
              <!--<uniformOrderCrossoverOperator/>-->
          </unionCrossoverOperator>
          <mutationOperator>
            <unionMoveSelector>
              <swapMoveSelector/>
              <changeMoveSelector/>
              <subChainChangeMoveSelector>
                <selectReversingMoveToo>true</selectReversingMoveToo>
              </subChainChangeMoveSelector>
                <!--<subChainSwapMoveSelector>-->
                    <!--<selectReversingMoveToo>true</selectReversingMoveToo>-->
                <!--</subChainSwapMoveSelector>-->
            </unionMoveSelector>
          </mutationOperator>
            <replacementStrategy>
                <replacementStrategyType>KEEP_NEW</replacementStrategyType>
            </replacementStrategy>
        </geneticAlgorithm>
    </solver>
  </solverBenchmark>
</#list>
</#list>
    </plannerBenchmark>
