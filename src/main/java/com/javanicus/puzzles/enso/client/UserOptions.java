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
package com.javanicus.puzzles.enso.client;

import java.math.BigDecimal;
import java.util.List;

public class UserOptions {
    private List<String> originalArgs;
    private String inputFilePath;
    private String outputFolderPath;
    private String outputName;
    
    private BigDecimal bevel = null; // e.g. 0.5
    private BigDecimal inset = null; // e.g. 0.07
    private BigDecimal scale = null; // e.g. 11.15
    
    private boolean onlyOutputUniqueShapes = false;
    private boolean outputEachProblemInSingleFile = false;
    private boolean preventRotationOfShapes = false;
    private boolean preventCreationOfSnapJoints = false;
    private int counterWeight = 1; //minimal counterweight as the default, go higher to be more invasive
    
    private List<Integer> whichPuzzlesToOutput = null;
    private List<String> whichShapesToPreventRotations = null;
    private List<String> whichShapesToPreventSnapJoints = null;

    
    public List<String> getOriginalArgs() {
        return originalArgs;
    }
    public void setOriginalArgs(List<String> originalArgs) {
        this.originalArgs = originalArgs;
    }
    public String getInputFilePath() {
        return inputFilePath;
    }
    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }    
    public String getOutputFolderPath() {
        return outputFolderPath;
    }
    public void setOutputFolderPath(String outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }
    public String getOutputName() {
        return outputName;
    }
    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }
    public BigDecimal getBevel() {
        return bevel;
    }
    public void setBevel(BigDecimal bevel) {
        this.bevel = bevel;
    }
    public BigDecimal getInset() {
        return inset;
    }
    public void setInset(BigDecimal inset) {
        this.inset = inset;
    }
    public BigDecimal getScale() {
        return scale;
    }
    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }
    public boolean isOnlyOutputUniqueShapes() {
        return onlyOutputUniqueShapes;
    }
    public void setOnlyOutputUniqueShapes(boolean onlyOutputUniqueShapes) {
        this.onlyOutputUniqueShapes = onlyOutputUniqueShapes;
    }
    public boolean isOutputEachProblemInSingleFile() {
        return outputEachProblemInSingleFile;
    }
    public void setOutputEachProblemInSingleFile(boolean outputEachProblemInSingleFile) {
        this.outputEachProblemInSingleFile = outputEachProblemInSingleFile;
    }
    public List<Integer> getWhichPuzzlesToOutput() {
        return whichPuzzlesToOutput;
    }
    public void setWhichPuzzlesToOutput(List<Integer> whichPuzzlesToOutput) {
        this.whichPuzzlesToOutput = whichPuzzlesToOutput;
    }
    public List<String> getWhichShapesToPreventRotations() {
        return whichShapesToPreventRotations;
    }
    public void setWhichShapesToPreventRotations(List<String> whichShapesToPreventRotations) {
        this.whichShapesToPreventRotations = whichShapesToPreventRotations;
    }
    public List<String> getWhichShapesToPreventSnapJoints() {
        return whichShapesToPreventSnapJoints;
    }
    public void setWhichShapesToPreventSnapJoints(List<String> whichShapesToPreventSnapJoints) {
        this.whichShapesToPreventSnapJoints = whichShapesToPreventSnapJoints;
    }
    public int getCounterWeight() {
        return counterWeight;
    }
    public void setCounterWeight(int counterWeight) {
        this.counterWeight = counterWeight;
    }
    public boolean isPreventRotationOfShapes() {
        return preventRotationOfShapes;
    }
    public void setPreventRotationOfShapes(boolean preventRotationOfShapes) {
        this.preventRotationOfShapes = preventRotationOfShapes;
    }
    public boolean isPreventCreationOfSnapJoints() {
        return preventCreationOfSnapJoints;
    }
    public void setPreventCreationOfSnapJoints(boolean preventCreationOfSnapJoints) {
        this.preventCreationOfSnapJoints = preventCreationOfSnapJoints;
    }    
}
