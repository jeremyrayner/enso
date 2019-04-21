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
package com.javanicus.puzzles.enso.entities;

import java.util.Map;
import java.util.TreeMap;

public class Problem {
    private String comment;
    private Map<String, Shape> uniqueShapes = new TreeMap<>();
    private Map<String, Shape> allShapes = new TreeMap<>();

    public Problem(String comment) {
        super();
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, Shape> getUniqueShapes() {
        return uniqueShapes;
    }

    public void setUniqueShapes(Map<String, Shape> uniqueShapes) {
        this.uniqueShapes = uniqueShapes;
    }

    public Map<String, Shape> getAllShapes() {
        return allShapes;
    }

    public void setAllShapes(Map<String, Shape> allShapes) {
        this.allShapes = allShapes;
    }

    public String toString() {
        int uniqueCount = 0;
        String uniqueNames = "";
        if (uniqueShapes != null) {
            uniqueCount = uniqueShapes.size();
            uniqueNames = uniqueShapes.keySet().toString();
        }
        int allCount = 0;
        if (allShapes != null) {
            allCount = allShapes.size();
        }
        return "Problem with " + allCount + " total shapes of which " + uniqueCount + " are unique: " + uniqueNames; 
    }
}
