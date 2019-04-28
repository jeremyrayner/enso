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

public class Voxel {
    private int xMax;
    private int yMax;
    private int zMax;
    private String name;
    private String text;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        text = text.replaceAll("[0-9]",""); // remove any colour information
        this.text = text;
    }
    public char peek(int x, int y, int z) {
        int position = x + (y*xMax) + (z*yMax*xMax);
        if (text != null && position < text.length()) {
            return text.charAt(position);
        }
        return ' ';
    }

    public int getXMax() {
        return xMax;
    }
    public void setXMax(int xMax) {
        this.xMax = xMax;
    }
    public int getYMax() {
        return yMax;
    }
    public void setYMax(int yMax) {
        this.yMax = yMax;
    }
    public int getZMax() {
        return zMax;
    }
    public void setZMax(int zMax) {
        this.zMax = zMax;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return "(" + xMax + "," + yMax + "," + zMax + ") " + text;
    }
}
