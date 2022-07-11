/* -----------------------------------------------------------------------------
 * formula-analysis-sat4j-sharpsat - Analysis of propositional formulas using Sat4j and sharpSAT
 * Copyright (C) 2022 Sebastian Krieter
 * 
 * This file is part of formula-analysis-sat4j-sharpsat.
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
 * See <https://github.com/FeatJAR/formula-analysis-sat4j-sharpsat> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.analysis.sat4j_sharpsat;

import org.spldev.analysis.sat4j.*;
import org.spldev.analysis.sat4j.solver.*;
import org.spldev.analysis.sharpsat.solver.*;
import org.spldev.analysis.solver.SatSolver.*;
import org.spldev.clauses.*;
import org.spldev.clauses.solutions.*;
import org.spldev.formula.*;
import org.spldev.formula.structure.*;
import org.spldev.util.data.*;
import org.spldev.util.job.*;

/**
 * Finds certain solutions of propositional formulas.
 *
 * @author Sebastian Krieter
 */
public class UniformRandomConfigurationGenerator extends RandomConfigurationGenerator {

	public static final Identifier<SolutionList> identifier = new Identifier<>();

	@Override
	protected Identifier<SolutionList> getIdentifier() {
		return identifier;
	}

	private VariableDistribution dist;
	private final ModelRepresentation rep;
	private SharpSatSolver sharpSatSolver;

	public UniformRandomConfigurationGenerator(ModelRepresentation rep) {
		super();
		this.rep = rep;
	}

	@Override
	protected void init(InternalMonitor monitor) {
		satisfiable = findCoreFeatures(solver);
		if (!satisfiable) {
			return;
		}
		final Formula modelFormula = rep.get(FormulaProvider.CNF.fromFormula());
		sharpSatSolver = new SharpSatSolver(modelFormula);
		sharpSatSolver.getAssumptions().setAll(assumptions.getAll());
		sharpSatSolver.getDynamicFormula().push(assumedConstraints);

		dist = new VariableDistribution(sharpSatSolver, modelFormula.getVariableMap().size());
		dist.setRandom(getRandom());
		solver.setSelectionStrategy(SStrategy.uniform(dist));
	}

	@Override
	protected void forbidSolution(final LiteralList negate) {
		super.forbidSolution(negate);
		sharpSatSolver.getDynamicFormula().push(Clauses.toOrClause(negate, rep.getVariables()));
	}

	@Override
	protected void prepareSolver(Sat4JSolver solver) {
		super.prepareSolver(solver);
		solver.setTimeout(1_000_000);
	}

	@Override
	protected void reset() {
		dist.reset();
	}

	private boolean findCoreFeatures(Sat4JSolver solver) {
		final int[] fixedFeatures = solver.findSolution().getLiterals();
		if (fixedFeatures == null) {
			return false;
		}
		solver.setSelectionStrategy(SStrategy.inverse(fixedFeatures));

		// find core/dead features
		for (int i = 0; i < fixedFeatures.length; i++) {
			final int varX = fixedFeatures[i];
			if (varX != 0) {
				solver.getAssumptions().push(-varX);
				final SatResult hasSolution = solver.hasSolution();
				switch (hasSolution) {
				case FALSE:
					solver.getAssumptions().replaceLast(varX);
					break;
				case TIMEOUT:
					solver.getAssumptions().pop();
					break;
				case TRUE:
					solver.getAssumptions().pop();
					LiteralList.resetConflicts(fixedFeatures, solver.getInternalSolution());
					solver.shuffleOrder(getRandom());
					break;
				}
			}
		}
		return true;
	}

}
