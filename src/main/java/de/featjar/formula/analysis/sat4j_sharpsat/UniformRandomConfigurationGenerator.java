/*
 * Copyright (C) 2023 Sebastian Krieter
 *
 * This file is part of FeatJAR-formula-analysis-sat4j-sharpsat.
 *
 * formula-analysis-sat4j-sharpsat is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * formula-analysis-sat4j-sharpsat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with formula-analysis-sat4j-sharpsat. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-formula-analysis-sat4j-sharpsat> for further information.
 */
package de.featjar.formula.analysis.sat4j_sharpsat;

import de.featjar.formula.analysis.sat4j.solver.ISelectionStrategy;
import de.featjar.formula.analysis.sat4j.solver.SAT4JSolutionSolver;
import de.featjar.formula.analysis.sat4j.todo.configuration.RandomConfigurationGenerator;
import de.featjar.formula.analysis.sharpsat.solver.SharpSATSolver;
import de.featjar.formula.structure.IExpression;

/**
 * Finds certain solutions of propositional formulas.
 *
 * @author Sebastian Krieter
 */
public class UniformRandomConfigurationGenerator { // extends RandomConfigurationGenerator {
//    private VariableDistribution dist;
//    private final ModelRepresentation rep;
//    private SharpSATSolver sharpSatSolver;
//
//    public UniformRandomConfigurationGenerator(ModelRepresentation rep) {
//        this.rep = rep;
//    }
//
//    @Override
//    protected void init(IMonitor monitor) {
//        satisfiable = findCoreFeatures(solver);
//        if (!satisfiable) {
//            return;
//        }
//        final IExpression modelExpression = rep.get(FormulaComputation.CNF.fromFormula());
//        sharpSatSolver = new SharpSATSolver(modelExpression);
//        sharpSatSolver.getAssignment().set(assumptions.get());
//
//        dist = new VariableDistribution(
//                sharpSatSolver,
//                modelExpression.getTermMap().map(TermMap::getVariableCount).orElse(0));
//        dist.setRandom(getRandom());
//        solver.setSelectionStrategy(ISelectionStrategy.uniform(dist));
//    }
//
//    @Override
//    protected void forbidSolution(final SortedIntegerList negate) {
//        super.forbidSolution(negate);
//        sharpSatSolver.getSolverFormula().push(Deprecated.toOrClause(negate, rep.getVariables()));
//    }
//
//    @Override
//    protected void prepareSolver(SAT4JSolutionSolver solver) {
//        super.prepareSolver(solver);
//        solver.setTimeout(1_000_000);
//    }
//
//    @Override
//    protected void reset() {
//        dist.reset();
//    }
//
//    private boolean findCoreFeatures(SAT4JSolutionSolver solver) {
//        final int[] fixedFeatures = solver.findSolution().getLiterals();
//        if (fixedFeatures == null) {
//            return false;
//        }
//        solver.setSelectionStrategy(ISelectionStrategy.inverse(fixedFeatures));
//
//        // find core/dead features
//        for (int i = 0; i < fixedFeatures.length; i++) {
//            final int varX = fixedFeatures[i];
//            if (varX != 0) {
//                solver.getAssignment().add(-varX);
//                final SATSolver.Result<Boolean> hasSolution = solver.hasSolution();
//                switch (hasSolution) {
//                    case FALSE:
//                        solver.getAssignment().replaceLast(varX);
//                        break;
//                    case TIMEOUT:
//                        solver.getAssignment().remove();
//                        break;
//                    case TRUE:
//                        solver.getAssignment().remove();
//                        SortedIntegerList.resetConflicts(fixedFeatures, solver.getInternalSolution());
//                        solver.shuffleOrder(getRandom());
//                        break;
//                }
//            }
//        }
//        return true;
//    }
}
