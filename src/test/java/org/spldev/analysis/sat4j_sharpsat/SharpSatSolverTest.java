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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.spldev.analysis.sat4j.*;
import org.spldev.analysis.sat4j.twise.*;
import org.spldev.clauses.*;
import org.spldev.clauses.solutions.*;
import org.spldev.configuration.list.DistributionMetrics.*;
import org.spldev.formula.*;
import org.spldev.util.extension.*;

public class SharpSatSolverTest {

	private static final Path modelDirectory = Paths.get("src/test/resources/testFeatureModels");

	private static final List<String> modelNames = Arrays.asList( //
		"basic", //
		"simple", //
		"car", //
		"gpl_medium_model", //
		"500-100");

	private static ModelRepresentation load(final Path modelFile) {
		return ModelRepresentation.load(modelFile) //
			.orElseThrow(p -> new IllegalArgumentException(p.isEmpty() ? null : p.get(0).getError().get()));
	}

	static {
		ExtensionLoader.load();
	}

	@Test
	public void distribution() {
		for (final String modelName : modelNames.subList(0, 4)) {
			final ModelRepresentation rep = load(modelDirectory.resolve(modelName + ".xml"));
			final RatioDiffFunction ratioDiffFunction = new RatioDiffFunction(rep);

			final SolutionList sample = new AllConfigurationGenerator().getResult(rep).orElseThrow();
			final List<ClauseList> expressions = TWiseConfigurationGenerator.convertLiterals(Clauses.getLiterals(
				rep.getVariables())).get(0);

			for (final ClauseList expression : expressions) {
				final double diff = ratioDiffFunction.compute(sample, expression);
				assertEquals(diff, 0, 0.000_000_000_000_1, modelName + " | " + String.valueOf(expression));
			}
		}
	}

}
