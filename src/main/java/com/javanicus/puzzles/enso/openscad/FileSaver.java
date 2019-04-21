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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.javanicus.puzzles.enso.client.UserOptions;
import com.javanicus.puzzles.enso.entities.Problem;
import com.javanicus.puzzles.enso.entities.Shape;

public class FileSaver {
    public void save(UserOptions userOptions, Path outputFolderPath, List<Problem> problems) {
        ProblemSaver problemSaver = new ProblemSaver(outputFolderPath);
        ShapeSaver shapeSaver = new ShapeSaver(outputFolderPath);
        try {
            if (problems.size() > 0) {
                System.out.println("saving to folder: " + outputFolderPath.toFile().getCanonicalPath());
            }
            int problemCounter = 0;
            for (Problem problem:problems) {
                problemCounter++;
                if (userOptions.getWhichPuzzlesToOutput() == null // i.e. not specified - so default to all
                        || userOptions.getWhichPuzzlesToOutput().contains(problemCounter)) { 
                    Map<String, Shape> problemShapes = problem.getAllShapes();
                    if (userOptions.isOnlyOutputUniqueShapes()) {
                        problemShapes = problem.getUniqueShapes();
                    }
                    if (userOptions.isOutputEachProblemInSingleFile()) {
                        // output all at once
                        problemSaver.save(problem.getComment(), problemShapes, problemCounter, userOptions);
                    } else {
                        // output individual burr pieces
                        for (Map.Entry<String, Shape> entry : problemShapes.entrySet()) {
                            shapeSaver.save(problem.getComment(), entry.getKey(), entry.getValue(), problemCounter, userOptions);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
