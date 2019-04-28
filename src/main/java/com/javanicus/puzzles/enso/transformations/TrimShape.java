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

import java.util.Map;
import java.util.TreeMap;

import com.javanicus.puzzles.enso.entities.Shape;
import com.javanicus.puzzles.enso.entities.Voxel;
import com.javanicus.puzzles.enso.transformations.Transformation;

public class TrimShape implements Transformation {

    @Override
    public Map<String, Shape> transform(Map.Entry<String, Shape> namedShape) {
        Shape shape = namedShape.getValue();
        
        Shape trimmedShape = trim(shape);

        // return
        Map<String, Shape> transformedNamedShape = new TreeMap<>();
        transformedNamedShape.put(namedShape.getKey(), trimmedShape);
        return transformedNamedShape;
    }

    public Shape trim(Shape shape) {
        StringBuilder trimmedShapeText = new StringBuilder();
        Voxel voxel = shape.getVoxel();
        // figure out the bounding box
        int firstX = voxel.getXMax();
        int lastX = 0;
        int firstY = voxel.getYMax();
        int lastY = 0;
        int firstZ = voxel.getZMax();
        int lastZ = 0;
        for (int z = 0; z < voxel.getZMax(); z++) {
            for (int y = (voxel.getYMax() - 1); y >= 0; y--) {
                for (int x = 0; x < voxel.getXMax(); x++) {
                    if ('#' == voxel.peek(x, y, z) // a regular voxel
                            || ('%' == voxel.peek(x, y, z)) // a dual connector voxel
                            || ('+' == voxel.peek(x, y, z)) // an overhanging voxel
                            || (voxel.peek(x, y, z) >= 'a' && voxel.peek(x, y, z) <= 'z') // a female connector voxel
                            || (voxel.peek(x, y, z) >= 'A' && voxel.peek(x, y, z) <= 'Z') // a male connector voxel
                       ) {
                        firstX = Math.min(firstX, x);
                        lastX = Math.max(x,lastX);
                        firstY = Math.min(firstY, y);
                        lastY = Math.max(y,lastY);
                        firstZ = Math.min(firstZ, z);
                        lastZ = Math.max(z,lastZ);
                    }
                }
            }
        }

        for (int z = firstZ; z <= lastZ; z++) {
            for (int y = firstY; y <= lastY; y++) {
                for (int x = firstX; x <= lastX; x++) {
                    trimmedShapeText.append(voxel.peek(x, y, z));
                }
            }
        }
        Voxel trimmedVoxel = new Voxel();
        trimmedVoxel.setXMax(lastX - (firstX - 1));
        trimmedVoxel.setYMax(lastY - (firstY - 1));
        trimmedVoxel.setZMax(lastZ - (firstZ - 1));
        trimmedVoxel.setName(voxel.getName());
        trimmedVoxel.setText(trimmedShapeText.toString());

        Shape trimmedShape = new Shape();
        trimmedShape.setVoxel(trimmedVoxel);
        trimmedShape.setFlippedOver(shape.isFlippedOver());
        return trimmedShape;
    }
}
