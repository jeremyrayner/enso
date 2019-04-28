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

public class Shape {
    private Voxel voxel;
    private String scadCode;
    private boolean rotated = false;
    private boolean flippedOver = false;
    
    public void setVoxel(Voxel voxel) {
        this.voxel = voxel;
    }
    public Voxel getVoxel() {
        return voxel;
    }

    public void setScadCode(String scadCode) {
        this.scadCode = scadCode;
    }
    public String getScadCode() {
        return scadCode;
    }
    public String toString() {
        if (scadCode == null && voxel != null) {
            return voxel.toString();
        }
        return "" + scadCode;
    }
    public boolean isRotated() {
        return rotated;
    }
    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    public boolean isPrintableWithoutSupports() {
        Voxel voxel = this.getVoxel();
        if (voxel.getZMax() == 1) {
            return true;
        }
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
            if (potentialOverhangSlice.isOverhang()) {
                return false;
            }
            previousSlice = currentSlice;
        }
        return true;
    }
    
    public int countTotalOverhangVoxels() {
        Voxel voxel = this.getVoxel();
        if (voxel.getZMax() == 1) {
            return 0;
        }
        int count = 0;
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
            count = count + potentialOverhangSlice.getOverhangCounter();
            previousSlice = currentSlice;
        }
        return count;
    }

    public Shape flipOver() {
        Shape flippedOverShape = this.rotateY90().rotateY90();
        flippedOverShape.flippedOver = true;
        return flippedOverShape;
    }

    public void setFlippedOver(boolean flippedOver) {
        this.flippedOver = flippedOver;
    }
    public boolean isFlippedOver() {
        return flippedOver;
    }
    
    public Shape rotateY90() {
        Voxel originalVoxel = this.getVoxel();
        Voxel rotatedVoxel = new Voxel();
        rotatedVoxel.setXMax(originalVoxel.getXMax());
        rotatedVoxel.setYMax(originalVoxel.getZMax());
        rotatedVoxel.setZMax(originalVoxel.getYMax());
        rotatedVoxel.setName(originalVoxel.getName());
        
        StringBuilder rotatedVoxelText = new StringBuilder();
        for (int y = originalVoxel.getYMax() - 1; y >= 0; y--) {
            for (int z = 0; z < originalVoxel.getZMax(); z++) {
                for (int x = 0; x < originalVoxel.getXMax(); x++) {
                    rotatedVoxelText.append(originalVoxel.peek(x, y, z));
                }
            }
        }

        rotatedVoxel.setText(rotatedVoxelText.toString());

        Shape rotatedShape = new Shape();
        rotatedShape.setVoxel(rotatedVoxel);
        rotatedShape.setRotated(true);
        return rotatedShape;
    }

    public Shape rotateX90() {
        Voxel originalVoxel = this.getVoxel();
        Voxel rotatedVoxel = new Voxel();
        rotatedVoxel.setXMax(originalVoxel.getZMax());
        rotatedVoxel.setYMax(originalVoxel.getYMax());
        rotatedVoxel.setZMax(originalVoxel.getXMax());
        rotatedVoxel.setName(originalVoxel.getName());
        
        StringBuilder rotatedVoxelText = new StringBuilder();
        for (int x = 0; x < originalVoxel.getXMax(); x++) {
            for (int y = 0; y < originalVoxel.getYMax(); y++) {
                for (int z = originalVoxel.getZMax() - 1; z >= 0; z--) {
                    rotatedVoxelText.append(originalVoxel.peek(x, y, z));
                }
            }
        }

        rotatedVoxel.setText(rotatedVoxelText.toString());

        Shape rotatedShape = new Shape();
        rotatedShape.setVoxel(rotatedVoxel);
        rotatedShape.setRotated(true);
        return rotatedShape;
    }
}
