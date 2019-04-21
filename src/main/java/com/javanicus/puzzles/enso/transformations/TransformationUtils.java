// enso - A program to manipulate abstract puzzle designs into physical puzzles.
//    Copyright (C) 2019  Jeremy Rayner
//
// This file is part of enso
//
// enso is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// enso is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with enso.  If not, see <https://www.gnu.org/licenses/>.
//
package com.javanicus.puzzles.enso.transformations;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.javanicus.puzzles.enso.entities.Problem;
import com.javanicus.puzzles.enso.entities.Shape;

public class TransformationUtils {
    public void visitShapes(List<Transformation> transformations, List<Problem> problems) {
        if (transformations != null && !transformations.isEmpty()) {
            for (Problem problem:problems) {

                // transform unique shapes
                for (Transformation transformation:transformations) {
                    Map<String, Shape> transformedUniqueShapes = new TreeMap<>();
                    for (Map.Entry<String, Shape> namedShape: problem.getUniqueShapes().entrySet()) {
                        Map<String, Shape> transformedNamedShapes = transformation.transform(namedShape);
                        transformedUniqueShapes.putAll(transformedNamedShapes);
                    }
                    problem.setUniqueShapes(transformedUniqueShapes);
                }

                // transform all shapes
                for (Transformation transformation:transformations) {
                    Map<String, Shape> transformedAllShapes = new TreeMap<>();
                    for (Map.Entry<String, Shape> namedShape: problem.getAllShapes().entrySet()) {
                        Map<String, Shape> transformedNamedShapes = transformation.transform(namedShape);
                        transformedAllShapes.putAll(transformedNamedShapes);
                    }
                    problem.setAllShapes(transformedAllShapes);
                }
            }
        }
    }
}
