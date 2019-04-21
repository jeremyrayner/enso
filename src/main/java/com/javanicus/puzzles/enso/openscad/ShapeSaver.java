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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Shape;

public class ShapeSaver {
    private Path outputFolderPath;

    public ShapeSaver(Path outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }

    public void save(String comment, String shapeName, Shape shape, int problemCounter, UserOptions userOptions) throws FileNotFoundException {
        String outputFileName2 = userOptions.getOutputName() + "-P" + problemCounter + "-" + shapeName + ".scad";
        Path outputFilePath2 = outputFolderPath.resolve(outputFileName2);
        OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(new FileOutputStream(outputFilePath2.toFile()), StandardCharsets.UTF_8);
        PrintWriter printWriter2 = new PrintWriter(outputStreamWriter2);
        printWriter2.println("// " + outputFileName2);
        printWriter2.println("// generated by enso (Copyright 2019 Jeremy Rayner, All rights reserved)");
        if (userOptions.getOriginalArgs() != null) {
            StringBuilder sb = new StringBuilder();
            for (String arg:userOptions.getOriginalArgs()) {
                sb.append(arg);
                sb.append(" ");
            }
            printWriter2.println("// created with args: " + sb.toString());
        }
        if (!shape.isPrintableWithoutSupports()) {
            printWriter2.println("");
            printWriter2.println("// needs support material to print");
        }
        printWriter2.println("");
        printWriter2.println("include <puzzlecad.scad> // puzzlecad created by Aaron Siegel, download from https://www.thingiverse.com/thing:3198014");
        printWriter2.println("");
        printWriter2.println("burr_piece(");
        StringBuilder output = new StringBuilder();
        output.append(shape.getScadCode());
        output.append(" // ").append(shapeName);
        printWriter2.println(output.toString());
        printWriter2.print(", $burr_scale = " + userOptions.getScale());
        printWriter2.print(", $burr_inset = " + userOptions.getInset());
        printWriter2.print(", $burr_bevel = " + userOptions.getBevel());
        printWriter2.println(");");
        if (comment != null && !comment.trim().isEmpty()) {
            printWriter2.println("");
            printWriter2.println("// Original Comment:");
            printWriter2.println("// --------------------------");
            BufferedReader bufReader = new BufferedReader(new StringReader(comment));
            String line=null;
            try {
                while( (line=bufReader.readLine()) != null) {
                    printWriter2.println("// " + line);
                }
            } catch (IOException ignoreException) {}
        }
        printWriter2.close();
        if (!shape.isPrintableWithoutSupports()) {
            String plural = "s";
            if (shape.countTotalOverhangVoxels() == 1) {
                plural = "";
            }
            if (shape.isRotated()) {
                System.out.println("written: " + outputFileName2 + " with " + shape.countTotalOverhangVoxels() + " overhanging voxel" + plural + " (rotated to minimise supports suggested)");
            } else {
                System.out.println("written: " + outputFileName2 + " with " + shape.countTotalOverhangVoxels() + " overhanging voxel" + plural + " (supports suggested)");            
            }
        } else if (shape.isRotated()) {
            System.out.println("written: " + outputFileName2 + " (rotated so printable without support)");
        } else {
            System.out.println("written: " + outputFileName2);
        }
        if (shape.getScadCode().contains("connect=mz+,connect=fz-")) {
            System.out.println("");
            System.out.println("INFO: -------");
            System.out.println(shapeName + " has a voxel which needs two connectors, this is not supported at the moment.");
            String mainShapeName = shapeName.substring(0,shapeName.indexOf('-'));
            String mainShapeName2 = mainShapeName.replaceAll("S", "");
            int mainShapeNum = Integer.parseInt(mainShapeName2);
            System.out.println("Suggest calling this program with the additional option '-r" + mainShapeNum + "' to prevent " + mainShapeName + " being rotated, you might be luckier.");
            System.out.println("-------");
            System.out.println("");
        }

    }
}