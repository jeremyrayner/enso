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
package com.javanicus.puzzles.enso.openscad;

import java.util.Map;
import java.util.TreeMap;

import com.javanicus.puzzles.enso.entities.Shape;
import com.javanicus.puzzles.enso.entities.Voxel;
import com.javanicus.puzzles.enso.transformations.Transformation;

public class OpenScadTransformation implements Transformation {

    @Override
    public Map<String, Shape> transform(Map.Entry<String, Shape> namedShape) {
        Shape shape = namedShape.getValue();
        StringBuilder openscadText = new StringBuilder();
        openscadText.append("[");

        Voxel voxel = shape.getVoxel();

        // convert voxel to OpenSCAD code
        for (int z = 0; z < voxel.getZMax(); z++) {
            if (z == 0) {
                openscadText.append("\"");
            } else {
                openscadText.append("\",\"");
            }
            for (int y = 0; y < voxel.getYMax(); y++) {
                if (y != 0) {
                    openscadText.append("|");
                }
                for (int x = 0; x < voxel.getXMax(); x++) {
                    char peekedVoxel = voxel.peek(x, y, z);
                    if ('#' == peekedVoxel) {
                        openscadText.append("x");
                    } else if ('%' == peekedVoxel) {
                        openscadText.append("x{connect=mz+,connect=fz-}"); // can you have two connectors?
                    } else if (peekedVoxel >= 'A' && peekedVoxel <= 'Z') {
                        openscadText.append("x{connect=mz+,clabel=");
                        openscadText.append(peekedVoxel);
                        openscadText.append("y-}");
                    } else if (peekedVoxel >= 'a' && peekedVoxel <= 'z') {
                        if (shape.isFlippedOver()) {
                            openscadText.append("x{connect=fz+,clabel=");
                            openscadText.append(("" + peekedVoxel).toUpperCase());
                            openscadText.append("y+}");
                        } else {
                            openscadText.append("x{connect=fz-,clabel=");
                            openscadText.append(("" + peekedVoxel).toUpperCase());
                            openscadText.append("y-}");                            
                        }
                    } else {
                        openscadText.append(".");
                    }
                }
            }
        }
        openscadText.append("\"]");
        shape.setScadCode(openscadText.toString());

        // return
        Map<String, Shape> transformedNamedShape = new TreeMap<>();
        transformedNamedShape.put(namedShape.getKey(), shape);
        return transformedNamedShape;
    }
}
