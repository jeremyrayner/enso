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

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Shape;

public class AttemptToRotateUntilPrintableWithoutSupports implements Transformation {
    private UserOptions userOptions;
    
    public AttemptToRotateUntilPrintableWithoutSupports(UserOptions userOptions) {
        this.userOptions = userOptions;
    }

    @Override
    public Map<String, Shape> transform(Map.Entry<String, Shape> namedShape) {
        Shape shape = namedShape.getValue();
        Shape originalShape = shape;
        if (canRotateShape(namedShape)) {
            // V
            if (!shape.isPrintableWithoutSupports()) {
                shape = shape.rotateY90();
                // <
                if (!shape.isPrintableWithoutSupports()) {
                    shape = shape.rotateY90();
                    // ^
                    if (!shape.isPrintableWithoutSupports()) {
                        shape = shape.rotateY90();
                        // >
                        if (!shape.isPrintableWithoutSupports()) {                        
                            shape = originalShape.rotateX90();
                            // _
                            if (!shape.isPrintableWithoutSupports()) {
                                shape = shape.rotateX90();
                                shape = shape.rotateX90();
                                // |
                                if (!shape.isPrintableWithoutSupports()) {
                                    //System.out.println(namedShape.getKey() + " is not printable without supports");
                                    shape = originalShape;
                                }
                            }
                        }
                    }                
                }
            }
        }
        // return
        Map<String, Shape> transformedNamedShape = new TreeMap<>();
        transformedNamedShape.put(namedShape.getKey(), shape);
        return transformedNamedShape;
    }
    private boolean canRotateShape(Map.Entry<String, Shape> namedShape) {
        List<String> whichShapesToPreventRotations = userOptions.getWhichShapesToPreventRotations();
        if (whichShapesToPreventRotations == null) {
            return true;
        }
        return !whichShapesToPreventRotations.contains(namedShape.getKey());
    }
}
