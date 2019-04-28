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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Shape;
import com.javanicus.puzzles.enso.entities.Slice;
import com.javanicus.puzzles.enso.entities.Voxel;
import com.javanicus.puzzles.enso.transformations.Transformation;
import com.javanicus.puzzles.enso.transformations.TrimShape;

public class SplitUnprintableShapes implements Transformation {
    private char currentLabel = (char)('a' - 1);
    private UserOptions userOptions;
    
    public SplitUnprintableShapes(UserOptions userOptions) {
        this.userOptions = userOptions;
    }

    @Override
    public Map<String, Shape> transform(Map.Entry<String, Shape> namedShape) {
        Shape shape = namedShape.getValue();
        
        if (shape.isPrintableWithoutSupports() // after all rotations have been tried
                || shouldNotMakeSnapJointsForThisShape(namedShape)) {
            // just return without doing anything else
            Map<String, Shape> transformedNamedShape = new TreeMap<>();
            transformedNamedShape.put(namedShape.getKey(), shape);
            return transformedNamedShape;
        }

        
        List<Shape> splitShapes = this.splitShapeAtEachOverhangRegion(shape);
        
        
        Map<String, Shape> transformedNamedShape = new TreeMap<>();
        for (int i = 0; i < splitShapes.size() ; i++) {
            transformedNamedShape.put(namedShape.getKey() + "-L" + (i + 1), splitShapes.get(i));
        }
        return transformedNamedShape;
    }

    public List<Shape> splitShapeAtEachOverhangRegion(Shape shape) {
        List<Shape> splitShapes = new ArrayList<>();
        if (shape.isPrintableWithoutSupports()) {
            splitShapes.add(shape);
            return splitShapes;
        }
        Voxel voxel = shape.getVoxel();
        List<Integer> overhangLayers = new ArrayList<>();
        //List<Slice> potentialOverhangSlicesByZ = new ArrayList<>();
        List<Slice> expandedOverhangSlicesByZ = new ArrayList<>();
        List<Slice> rationalisedOverhangSlicesByZ = new ArrayList<>();
        Slice previousSlice = null;
        for (int z = 0; z < voxel.getZMax(); z++) {
            Slice currentSlice = new Slice(voxel.getXMax(), voxel.getYMax());
            for (int y = 0; y < voxel.getYMax(); y++) {
                for (int x = 0; x < voxel.getXMax(); x++) {
                    char value = voxel.peek(x, y, z);
                    currentSlice.poke(x, y, value);
                }
            }
            Slice potentialOverhangSlice = currentSlice.getOverhangSlice(previousSlice);
            //potentialOverhangSlicesByZ.add(potentialOverhangSlice);
            if (potentialOverhangSlice.isOverhang()) {
                currentLabel = (char) (currentLabel + 1);
                if (currentLabel > 'z') {
                    currentLabel = 'a'; // wrap around if more than 26 regions
                }
            }
            Slice expandedSlice = expandOverhangRegions(potentialOverhangSlice, userOptions.getCounterWeight());
            expandedOverhangSlicesByZ.add(expandedSlice);
            Slice rationalisedSlice = rationaliseConnectors(expandedSlice);
            rationalisedOverhangSlicesByZ.add(rationalisedSlice);
            if (potentialOverhangSlice.isOverhang()) {
                overhangLayers.add(z);
            }
            previousSlice = currentSlice;
        }

        StringBuilder[] shapeTexts = new StringBuilder[voxel.getZMax()];
        for (int z = 0; z < voxel.getZMax(); z++) {
            shapeTexts[z] = new StringBuilder();
        }
        int[][] whichShape = new int[voxel.getXMax()][voxel.getYMax()];
        int maxShapeIndex = 0;
        int latestShapeIndex = 0;
        for (int z = 0; z < voxel.getZMax(); z++) {
            if (overhangLayers.contains(z)) {
                latestShapeIndex++;
                maxShapeIndex = Math.max(maxShapeIndex, latestShapeIndex);
            }
            for (int y = 0; y < voxel.getYMax(); y++) {
                for (int x = 0; x < voxel.getXMax(); x++) {
                    if (expandedOverhangSlicesByZ.get(z).peek(x, y) == '+' || isMarkedAsConnector(expandedOverhangSlicesByZ.get(z).peek(x, y))) {
                        whichShape[x][y] = latestShapeIndex;
                    }
                    for (int shapeIndex = 0; shapeIndex < voxel.getZMax(); shapeIndex++) {
                        if (shapeIndex == whichShape[x][y]) {
                            // active shape
                            char actual = voxel.peek(x, y, z);
                            char toAppend = voxel.peek(x, y, z);
                            if (overhangLayers.contains(z+1)) {
                                char expandedOverhangJustAbove = rationalisedOverhangSlicesByZ.get(z+1).peek(x, y);
                                if (actual == '#' && isMarkedAsConnector(expandedOverhangJustAbove)) {
                                    toAppend = Character.toUpperCase(expandedOverhangJustAbove);
                                }
                            }
                            if (overhangLayers.contains(z)) {
                                char expandedOverhangRightHere = rationalisedOverhangSlicesByZ.get(z).peek(x, y);
                                if (isMarkedAsConnector(expandedOverhangRightHere)) {
                                    if (toAppend >= 'A' && toAppend <= 'Z') {
                                        toAppend = '%'; // bit of both
                                    } else {
                                        toAppend = expandedOverhangRightHere;                            
                                    }
                                }
                            }
                            shapeTexts[shapeIndex].append(toAppend);
                        } else {
                            // passive shape
                            shapeTexts[shapeIndex].append("_");
                        }
                    }
                }
            }
        }
        TrimShape trimshape = new TrimShape();
        for (int z = 0; z <= maxShapeIndex; z++) {
            Voxel zVoxel = new Voxel();
            zVoxel.setXMax(voxel.getXMax());
            zVoxel.setYMax(voxel.getYMax());
            zVoxel.setZMax(voxel.getZMax());
            zVoxel.setName(voxel.getName());
            zVoxel.setText(shapeTexts[z].toString());
            Shape zShape = new Shape();
            zShape.setVoxel(zVoxel);
            
            // turn the final shape over, if it would print without support material
            if (z == maxShapeIndex) {
                Shape zShapeRotatedTwice = zShape.flipOver();
                if (zShapeRotatedTwice.isPrintableWithoutSupports()) {
                    zShape = zShapeRotatedTwice;
                }
            }
          
            zShape = trimshape.trim(zShape);
            splitShapes.add(zShape);
        }
        
        return splitShapes;
    }
    private Slice expandOverhangRegions(Slice slice, int counterWeight) {
        if (!slice.isOverhang()) {
            return slice;
        }
        counterWeight = counterWeight - 1;
        // recursive, so we stop once no more cells to expand into
        Slice expandedSlice = expand(slice);
        if (!slice.equals(expandedSlice) && counterWeight >= 0) {
            return expandOverhangRegions(expandedSlice, counterWeight);
        }
        return slice;
    }

    private Slice expand(Slice slice) {
        Slice expandedSlice = new Slice(slice);
        for (int y = 0; y < slice.getYMax(); y++) {
            for (int x = 0; x < slice.getXMax(); x++) {
                boolean hasOverhangNeighbour = false;
                if (slice.peek(x, y) == '#') {
                    if (x > 0 && (slice.peek(x - 1 , y) == '+' || isMarkedAsConnector(slice.peek(x - 1 , y)))) {hasOverhangNeighbour = true;}
                    if (x < (slice.getXMax() - 1) && (slice.peek(x + 1 , y) == '+' || isMarkedAsConnector(slice.peek(x + 1 , y)))) {hasOverhangNeighbour = true;}
                    if (y > 0 && (slice.peek(x, y - 1) == '+' || isMarkedAsConnector(slice.peek(x, y - 1)))) {hasOverhangNeighbour = true;}
                    if (y < (slice.getYMax() - 1) && (slice.peek(x , y + 1) == '+' || isMarkedAsConnector(slice.peek(x , y + 1)))) {hasOverhangNeighbour = true;}
                }
                if (hasOverhangNeighbour) {
                    expandedSlice.poke(x, y, currentLabel); // '+' = Actual overhang - 'a'-'z' = labelled region connected to an overhang
                } else {
                    expandedSlice.poke(x, y, slice.peek(x, y));
                }
            }
        }
        return expandedSlice;
    }

    private Slice rationaliseConnectors(Slice slice) {
        Slice rationalisedSlice = new Slice(slice);
        for (int y = 0; y < slice.getYMax(); y++) {
            for (int x = 0; x < slice.getXMax(); x++) {
                boolean hasTwoNeighbours = false;
                char peek = slice.peek(x, y);
                if (isMarkedAsConnector(peek)) {
                    if (x > 0 && x < (slice.getXMax() - 1) && slice.peek(x - 1 , y) == peek && slice.peek(x + 1, y) == peek) {hasTwoNeighbours = true;}
                    if (y > 0 && y < (slice.getYMax() - 1) && slice.peek(x , y - 1) == peek && slice.peek(x, y + 1) == peek) {hasTwoNeighbours = true;}
                }
                if (hasTwoNeighbours) {
                    rationalisedSlice.poke(x, y, '#');                    
                } else {
                    rationalisedSlice.poke(x, y, peek);
                }
            }
        }
        return rationalisedSlice;
    }

    private boolean shouldNotMakeSnapJointsForThisShape(Entry<String, Shape> namedShape) {
        if (userOptions.getWhichShapesToPreventSnapJoints() == null) {
            return false;
        }
        return userOptions.getWhichShapesToPreventSnapJoints().contains(namedShape.getKey());
    }


    private boolean isMarkedAsConnector(char peek) {
        return (peek >= 'a' && peek <= 'z');
    }

}
