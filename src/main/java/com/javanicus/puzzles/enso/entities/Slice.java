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

import java.util.Arrays;

public class Slice {
    private int xMax;
    private int yMax;
    char[][] layer;
    boolean overhang = false;
    private int overhangCounter = 0;
    
    public Slice(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        layer = new char[xMax][yMax];

        // default each cell to be empty
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                layer[x][y] = '_';
            }
        }
    }
    public Slice(Slice that) {
        this.xMax = that.xMax;
        this.yMax = that.yMax;
        layer = new char[xMax][yMax];
        this.overhang = that.overhang;

        // copy each cell from the other slice
        for (int y = 0; y < that.yMax; y++) {
            for (int x = 0; x < that.xMax; x++) {
                char value = that.peek(x, y);
                this.poke(x, y, value);
            }
        }

    }

    public int getXMax() {
        return xMax;
    }
    public int getYMax() {
        return yMax;
    }
    
    public void poke(int x, int y, char value) {
        layer[x][y] = value;
    }
    public char peek(int x, int y) {
        return layer[x][y];
    }
    
    public boolean isOverhang() {
        return overhang;
    }

    public void setOverhang(boolean overhang) {
        this.overhang = overhang;
    }

    public Slice getOverhangSlice(Slice previousSlice) {
        Slice overhangSlice = new Slice(this.getXMax(), this.getYMax());
        if (previousSlice == null) {
            return overhangSlice;
        }
        for (int y = 0; y < this.getYMax(); y++) {
            for (int x = 0; x < this.getXMax(); x++) {
                if ((this.peek(x, y) == '#' || this.peek(x, y) == '%'|| isMarkedAsConnector(this.peek(x,y))) && previousSlice.peek(x, y) == '_') {
                    overhangSlice.poke(x, y, '+');
                    overhangSlice.setOverhang(true);
                    overhangSlice.incrementOverhangCounter();
                }
                if (this.peek(x, y) == '#' && previousSlice.peek(x, y) == '#') {
                    overhangSlice.poke(x, y, '#');
                }
            }
        }        
        return overhangSlice;
    }

    
    private void incrementOverhangCounter() {
        overhangCounter++;
    }

    public int getOverhangCounter() {
        return overhangCounter;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < yMax; y++) {
            for (int x = 0; x < xMax; x++) {
                sb.append(peek(x,y));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(layer);
        result = prime * result + xMax;
        result = prime * result + yMax;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slice other = (Slice) obj;
        if (!Arrays.deepEquals(layer, other.layer))
            return false;
        if (xMax != other.xMax)
            return false;
        if (yMax != other.yMax)
            return false;
        return true;
    }
    private boolean isMarkedAsConnector(char peek) {
        return (peek >= 'a' && peek <= 'z');
    }


    
}