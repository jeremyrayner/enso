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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Shape;
import com.javanicus.puzzles.enso.transformations.Transformation;

public class RotateToMinimiseOverhangsForSnapJoints implements Transformation {
    private UserOptions userOptions;
    
    public RotateToMinimiseOverhangsForSnapJoints(UserOptions userOptions) {
        this.userOptions = userOptions;
    }


    @Override
    public Map<String, Shape> transform(Map.Entry<String, Shape> namedShape) {
        Shape shape = namedShape.getValue();
        Shape transformedShape = shape;
        
        if (shape.isPrintableWithoutSupports() // after all rotations have been tried
                || shouldNotRotateShape(namedShape)) { // or we do not want to rotate this shape
            // just return without doing anything else
            Map<String, Shape> transformedNamedShape = new TreeMap<>();
            transformedNamedShape.put(namedShape.getKey(), shape);
            return transformedNamedShape;
        }

        // otherwise, we need to generate some snap joints
        //System.out.println("considering: " + namedShape.getKey());
        Map<String, Shape> orientations = new HashMap<>();
        orientations.put("x+", shape);
        orientations.put("y-", orientations.get("x+").rotateY90());
        orientations.put("x-", orientations.get("y-").rotateY90());
        orientations.put("y+", orientations.get("x-").rotateY90());
        orientations.put("z+", orientations.get("x+").rotateX90());
        orientations.put("z-", orientations.get("z+").rotateX90().rotateX90());

        int smallestOverhangCount = Integer.MAX_VALUE;
        Map.Entry<String, Shape> smallestEntry = null;
        for (Map.Entry<String, Shape> entry:orientations.entrySet()) {
            int entryOverhangCount = entry.getValue().countTotalOverhangVoxels();
            //System.out.println(entry.getKey() + ":" + entryOverhangCount);
            if (entryOverhangCount < smallestOverhangCount) {
                smallestOverhangCount = entryOverhangCount;
                smallestEntry = entry;
            }
        }
        
        
        if (smallestEntry != null) {
            //System.out.println("using " + smallestEntry.getKey());
            transformedShape = smallestEntry.getValue();
        }                    
        // return
        Map<String, Shape> transformedNamedShape = new TreeMap<>();
        transformedNamedShape.put(namedShape.getKey(), transformedShape);
        return transformedNamedShape;
    }


    private boolean shouldNotRotateShape(Map.Entry<String, Shape> namedShape) {
        List<String> whichShapesToPreventRotations = userOptions.getWhichShapesToPreventRotations();
        if (whichShapesToPreventRotations == null) {
            return false;
        }
        return whichShapesToPreventRotations.contains(namedShape.getKey());
    }
}
