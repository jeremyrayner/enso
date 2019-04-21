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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.javanicus.puzzles.enso.entities.Problem;
import com.javanicus.puzzles.enso.file.FileUtils;
import com.javanicus.puzzles.enso.openscad.FileSaver;
import com.javanicus.puzzles.enso.openscad.RotateToMinimiseOverhangsForSnapJoints;
import com.javanicus.puzzles.enso.openscad.SplitUnprintableShapes;
import com.javanicus.puzzles.enso.openscad.OpenScadTransformation;
import com.javanicus.puzzles.enso.parsers.PuzzleParser;
import com.javanicus.puzzles.enso.transformations.TransformationUtils;
import com.javanicus.puzzles.enso.transformations.TrimShape;
import com.javanicus.puzzles.enso.transformations.AttemptToRotateUntilPrintableWithoutSupports;
import com.javanicus.puzzles.enso.transformations.Transformation;

public class EnsoClient {

    private FileUtils fileUtils = new FileUtils();
    private PuzzleParser puzzleParser = new PuzzleParser();
    private FileSaver fileOutput = new FileSaver();
 
    public String processUserOptions(UserOptions userOptions)
            throws IOException, ParserConfigurationException, SAXException {

        if (userOptions.getInputFilePath() != null) {

            // --- process input file ---
            File userDir = new File(System.getProperty("user.dir"));
            Path puzzleFilePath = userDir.toPath().resolve(userOptions.getInputFilePath());
            String puzzleFilename = puzzleFilePath.toFile().getName();
            if (userOptions.getOutputName() == null) {
                String outputFilePrefix = puzzleFilename;
                if (outputFilePrefix.contains(".")) {
                    userOptions.setOutputName(outputFilePrefix.substring(0, outputFilePrefix.indexOf('.')));
                }
            }
            String puzzleXml = fileUtils.gunzip(puzzleFilePath);
       
            // --- build data structure of problems/shapes from file contents ---
            List<Problem> problems = puzzleParser.parse(puzzleXml);
       
            // --- transform file contents ---
            TransformationUtils transformationUtils = new TransformationUtils();
            List<Transformation> transformations = new ArrayList<>();
            transformations.add(new TrimShape());
            if (!userOptions.isPreventRotationOfShapes()) {
                transformations.add(new AttemptToRotateUntilPrintableWithoutSupports(userOptions));
                transformations.add(new RotateToMinimiseOverhangsForSnapJoints(userOptions));
            }
            if (!userOptions.isPreventCreationOfSnapJoints()) {
                transformations.add(new SplitUnprintableShapes(userOptions));
            }
            transformations.add(new OpenScadTransformation());
            transformationUtils.visitShapes(transformations, problems);
            
            // --- output files ---
            Path outputFolderPath = userDir.toPath(); // default to outputting in current folder
            if (userOptions.getOutputFolderPath() != null) {
                outputFolderPath = userDir.toPath().resolve(userOptions.getOutputFolderPath());
            }
            fileOutput.save(userOptions, outputFolderPath, problems);
        } else {
            // other ideas?
        }
        return "success";
    }
}
