/* -----------------------------------------------------------------------------
 * Formula-Analysis-Sat4J-SharpSat Lib - Library to analyze propositional formulas with Sat4J and SharpSAT.
 * Copyright (C) 2021  Sebastian Krieter
 * 
 * This file is part of Formula-Analysis-Sat4J-SharpSat Lib.
 * 
 * Formula-Analysis-Sat4J-SharpSat Lib is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Formula-Analysis-Sat4J-SharpSat Lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Formula-Analysis-Sat4J-SharpSat Lib.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * See <https://github.com/skrieter/formula-analysis-sat4j-sharpsat> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.analysis.sat4j_sharpsat;

import java.math.*;
import java.util.*;

import org.spldev.analysis.sat4j.solver.*;
import org.spldev.analysis.sharpsat.solver.*;

/**
 * Uses a sample of configurations to achieve a phase selection that corresponds
 * to a uniform distribution of configurations in the configuration space.
 *
 * @author Sebastian Krieter
 */
public class VariableDistribution extends LiteralDistribution {

	private final byte[] model;
	private BigDecimal totalCount;
	private SharpSatSolver solver;

	public VariableDistribution(SharpSatSolver solver, int size) {
		this.solver = solver;
		model = new byte[size];
		totalCount = new BigDecimal(solver.countSolutions());
	}

	@Override
	public void reset() {
		Arrays.fill(model, (byte) 0);
		solver.getAssumptions().unsetAll();
	}

	@Override
	public void unset(int var) {
		final int index = var - 1;
		final byte sign = model[index];
		if (sign != 0) {
			model[index] = 0;
			solver.getAssumptions().unset(index + 1);
		}
	}

	@Override
	public void set(int literal) {
		final int index = Math.abs(literal) - 1;
		if (model[index] == 0) {
			final boolean positive = literal > 0;
			model[index] = (byte) (positive ? 1 : -1);
			solver.getAssumptions().set(index + 1, positive);
		}
	}

	@Override
	public int getRandomLiteral(int var) {
		final int index = Math.abs(var) - 1;
		final byte sign = model[index];
		if (sign != 0) {
			return sign > 0 ? var : -var;
		} else {
			final int varIndex = Math.abs(var);
			solver.getAssumptions().set(varIndex, true);
			final BigDecimal positiveCount = new BigDecimal(solver.countSolutions());
			solver.getAssumptions().unset(varIndex);
			final double ratio = positiveCount.divide(totalCount, MathContext.DECIMAL32).doubleValue();
			final double randomValue = random.nextDouble();
			return randomValue < ratio
				? var
				: -var;
		}

	}

}
