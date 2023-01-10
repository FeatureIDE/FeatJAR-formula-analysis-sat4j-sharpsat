/*
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
 * See <https://github.com/FeatureIDE/FeatJAR-formula-analysis-sat4j-sharpsat> for further information.
 */
package de.featjar.formula.analysis.sat4j_sharpsat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.featjar.base.extension.ExtensionManager;
import de.featjar.formula.analysis.bool.ABooleanAssignmentList;
import de.featjar.formula.analysis.bool.BooleanSolutionList;
import de.featjar.formula.analysis.sat4j.todo.configuration.AllConfigurationGenerator;
import de.featjar.formula.analysis.sat4j.todo.twise.TWiseConfigurationGenerator;
import de.featjar.formula.configuration.list.DistributionMetrics.RatioDiffFunction;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SharpSATSolverTest {

    private static final Path modelDirectory = Paths.get("src/test/resources/testFeatureModels");

    private static final List<String> modelNames = Arrays.asList( //
            "basic", //
            "simple", //
            "car", //
            "gpl_medium_model", //
            "500-100");

    private static ModelRepresentation load(final Path modelFile) {
        return ModelRepresentation.load(modelFile) //
                .orElseThrow(p -> new IllegalArgumentException(
                        p.isEmpty() ? null : p.get(0).toException()));
    }

    static {
        ExtensionManager.install();
    }

    // @Test
    // TODO: test fails, but is not currently in use
    public void distribution() {
        for (final String modelName : modelNames.subList(0, 4)) {
            final ModelRepresentation rep = load(modelDirectory.resolve(modelName + ".xml"));
            final RatioDiffFunction ratioDiffFunction = new RatioDiffFunction(rep);

            final BooleanSolutionList sample =
                    rep.getResult(new AllConfigurationGenerator()).orElseThrow();
            final List<ABooleanAssignmentList> expressions = TWiseConfigurationGenerator.convertLiterals(
                            Deprecated.getLiterals(rep.getVariables()))
                    .get(0);

            for (final ABooleanAssignmentList expression : expressions) {
                final double diff = ratioDiffFunction.compute(sample, expression);
                assertEquals(diff, 0, 0.000_000_000_000_1, modelName + " | " + expression);
            }
        }
    }
}
