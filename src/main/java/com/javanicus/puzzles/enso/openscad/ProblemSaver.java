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
import java.util.Map;

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Shape;

public class ProblemSaver {
    private Path outputFolderPath;

    public ProblemSaver(Path outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }

    public void save(String comment, Map<String,Shape> problemShapes, int problemCounter, UserOptions userOptions) throws FileNotFoundException {
        // output burr plate
        String outputFileName = userOptions.getOutputName() + "-all-P" + problemCounter + ".scad";
        Path outputFilePath = outputFolderPath.resolve(outputFileName);
        OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(new FileOutputStream(outputFilePath.toFile()), StandardCharsets.UTF_8);
        PrintWriter printWriter1 = new PrintWriter(outputStreamWriter1);
        
        printWriter1.println("// " + userOptions.getOutputName() + " : P" + problemCounter);
        printWriter1.println("// generated by enso (Copyright 2019 Jeremy Rayner, All rights reserved)");
        if (userOptions.getOriginalArgs() != null) {
            StringBuilder sb = new StringBuilder();
            for (String arg:userOptions.getOriginalArgs()) {
                sb.append(arg);
                sb.append(" ");
            }
            printWriter1.println("// created with args: " + sb.toString());
        }
        printWriter1.println("");
        printWriter1.println("include <puzzlecad.scad> // puzzlecad created by Aaron Siegel, download from https://www.thingiverse.com/thing:3198014");
        printWriter1.println("");
        printWriter1.println("burr_plate([");
        int shapeCounter = 0;
        for (Map.Entry<String, Shape> entry : problemShapes.entrySet()) {
            shapeCounter++;
            StringBuilder output = new StringBuilder();
            output.append(entry.getValue().getScadCode());
            if (shapeCounter < problemShapes.size()) {
                output.append(",");
            } else {
                output.append(" ");
            }
            output.append(" // ").append(entry.getKey());
            printWriter1.println(output.toString());
        }
        printWriter1.print("]");
        printWriter1.print(", $burr_scale = " + userOptions.getScale());
        printWriter1.print(", $burr_inset = " + userOptions.getInset());
        printWriter1.print(", $burr_bevel = " + userOptions.getBevel());
        printWriter1.println(");");
        if (comment != null && !comment.trim().isEmpty()) {
            printWriter1.println("");
            printWriter1.println("// Original Comment:");
            printWriter1.println("// --------------------------");
            BufferedReader bufReader = new BufferedReader(new StringReader(comment));
            String line=null;
            try {
                while( (line=bufReader.readLine()) != null) {
                    printWriter1.println("// " + line);
                }
            } catch (IOException ignoreException) {}
        }
        printWriter1.close();
        System.out.println("written: " + outputFileName);

    }
}
