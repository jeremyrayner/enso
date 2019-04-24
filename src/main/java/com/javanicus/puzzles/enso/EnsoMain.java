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
package com.javanicus.puzzles.enso;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import com.javanicus.puzzles.enso.client.EnsoClient;
import com.javanicus.puzzles.enso.client.UserOptions;

public class EnsoMain {

    public static void main(String[] args) {
        String projectName = "enso";
        String version = "1.0.2";
        EnsoClient enso = new EnsoClient();
        CommandLine cmd = null;
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(126);
        Options options = null;
        try {
            System.out.println(projectName + " version " + version + " Copyright (C) 2019 Jeremy Rayner"); 
            System.out.println("");
            System.out.println("This program comes with ABSOLUTELY NO WARRANTY;");
            System.out.println("This is free software, and you are welcome to redistribute it");
            System.out.println("under certain conditions; see GPLv3 license for further details.");
            System.out.println("");

            UserOptions userOptions = new UserOptions();
            userOptions.setOriginalArgs(Arrays.asList(args));
            
            // read command line options
            options = new Options();
            CommandLineParser parser = new DefaultParser();
            options.addOption(Option.builder("B").longOpt("bevel").argName("mm").numberOfArgs(1).desc("how much to bevel the edges in mm").build());
            options.addOption(Option.builder("c").longOpt("counterweight").argName("depth").desc("how many counterweight voxels to attach to overhangs (default: 1)").numberOfArgs(1).build());
            options.addOption(Option.builder("d").longOpt("debug").desc("print debugging information").build());
            options.addOption(Option.builder("h").longOpt("help").desc("show help").build());
            options.addOption(Option.builder("I").longOpt("inset").argName("mm").desc("the tolerance in mm").numberOfArgs(1).build());
            options.addOption(Option.builder("J").longOpt("joints").desc("prevent creation of all snap joints").build());
            options.addOption(Option.builder("j").longOpt("shape-joints").argName("shape-nums").hasArgs().valueSeparator(',').desc("which shapes to prevent creation of snap joints (default: none) e.g -j2,5").build());
            options.addOption(Option.builder("o").longOpt("output").argName("prefix").numberOfArgs(1).desc("the output name").build());
            options.addOption(Option.builder("O").longOpt("output-folder").argName("path").numberOfArgs(1).desc("the output folder (default: current dir)").build());
            options.addOption(Option.builder("P").longOpt("puzzle").argName("puzzle-nums").hasArgs().valueSeparator(',').desc("which puzzles to output (default: all) e.g -P1,2,4").build());
            options.addOption(Option.builder("R").longOpt("rotate").desc("prevent rotation of all shapes").build());
            options.addOption(Option.builder("r").longOpt("shape-rotate").argName("shape-nums").hasArgs().valueSeparator(',').desc("which shapes to prevent rotation (default: none) e.g -r4,6").build());
            options.addOption(Option.builder("S").longOpt("scale").argName("mm").numberOfArgs(1).desc("size of a voxel in mm").build());
            options.addOption(Option.builder("s").longOpt("single").desc("output each problem in single file (default: multiple files)").build());
            options.addOption(Option.builder("u").longOpt("unique").desc("only output unique shapes (default: outputs all shapes including duplicates)").build());
            
            // parse the user options
            if (args.length == 0) {
                // todo, maybe make some kind of GUI to administer the UserOptions object
                // GUIMain gui = new GUIMain();
                // gui.showGUI(userOptions);
            } else {
                // Use command line options to build the UserOptions object
                cmd = parser.parse(options, args);
                if (cmd.hasOption("bevel")) { userOptions.setBevel(new BigDecimal(cmd.getOptionValue("bevel"))); }
                if (cmd.hasOption("inset")) { userOptions.setInset(new BigDecimal(cmd.getOptionValue("inset"))); }
                if (cmd.hasOption("scale")) { userOptions.setScale(new BigDecimal(cmd.getOptionValue("scale"))); }
    
                if (cmd.hasOption("counterweight")) {
                    int counterWeight = Integer.parseInt(cmd.getOptionValue("counterweight"));
                    if (counterWeight > 0) {
                        userOptions.setCounterWeight(counterWeight);
                    }
                }    
                if (cmd.hasOption("single")) { userOptions.setOutputEachProblemInSingleFile(true); }
                if (cmd.hasOption("unique")) { userOptions.setOnlyOutputUniqueShapes(true); }
                if (cmd.hasOption("joints")) { userOptions.setPreventCreationOfSnapJoints(true); }
                if (cmd.hasOption("rotate")) { userOptions.setPreventRotationOfShapes(true); }
                
                if (cmd.hasOption("puzzle")) {
                    String[] whichPuzzlesToOutput = cmd.getOptionValues("puzzle"); 
                    if (whichPuzzlesToOutput != null && whichPuzzlesToOutput.length > 0) {
                        List<Integer> whichPuzzlesToOutputList = new ArrayList<>();
                        for (String whichPuzzleToOutput: whichPuzzlesToOutput) {
                            if (whichPuzzleToOutput != null) {
                                whichPuzzleToOutput = whichPuzzleToOutput.replaceAll("[Pp]", "");
                                whichPuzzlesToOutputList.add(Integer.parseInt(whichPuzzleToOutput));
                            }
                        }
                        userOptions.setWhichPuzzlesToOutput(whichPuzzlesToOutputList);
                    }
                }
                if (cmd.hasOption("shape-joints")) {
                    String[] whichShapesToPreventSnapJoints = cmd.getOptionValues("shape-joints"); 
                    if (whichShapesToPreventSnapJoints != null && whichShapesToPreventSnapJoints.length > 0) {
                        List<String> whichShapesToPreventSnapJointsList = new ArrayList<>();
                        for (String whichShapeToPreventSnapJoints: whichShapesToPreventSnapJoints) {
                            if (whichShapeToPreventSnapJoints != null) {
                                whichShapeToPreventSnapJoints = whichShapeToPreventSnapJoints.replaceAll("[Ss]", "");
                                whichShapesToPreventSnapJointsList.add(getShapeName(Integer.parseInt(whichShapeToPreventSnapJoints)));
                            }
                        }
                        userOptions.setWhichShapesToPreventSnapJoints(whichShapesToPreventSnapJointsList);
                    }
                }
                if (cmd.hasOption("shape-rotate")) {
                    String[] whichShapesToPreventRotations = cmd.getOptionValues("shape-rotate"); 
                    if (whichShapesToPreventRotations != null && whichShapesToPreventRotations.length > 0) {
                        List<String> whichShapesToPreventRotationsList = new ArrayList<>();
                        for (String whichShapeToPreventRotations: whichShapesToPreventRotations) {
                            if (whichShapeToPreventRotations != null) {
                                whichShapeToPreventRotations = whichShapeToPreventRotations.replaceAll("[Ss]", "");
                                whichShapesToPreventRotationsList.add(getShapeName(Integer.parseInt(whichShapeToPreventRotations)));
                            }
                        }
                        userOptions.setWhichShapesToPreventRotations(whichShapesToPreventRotationsList);
                    }
                }
                String[] cmdArgs = cmd.getArgs();
                if (cmdArgs.length > 0) {
                    userOptions.setInputFilePath(cmdArgs[0]);
                }
                userOptions.setOutputName(cmd.getOptionValue("output"));
                userOptions.setOutputFolderPath(cmd.getOptionValue("output-folder"));
            }            
            // read input file
            if (userOptions.getInputFilePath() != null && !cmd.hasOption("help")) {
                enso.processUserOptions(userOptions);
            } else {
                printHelp(helpFormatter, projectName, options, false);
            }
        } catch (ParseException | ParserConfigurationException | SAXException | IOException e) {
            if (options != null) {
                printHelp(helpFormatter, projectName, options, false);
            }
            System.out.println("");
            if (cmd != null && cmd.hasOption("debug")) {
                e.printStackTrace();
            } else {
                System.out.print("ERROR: ");
                System.out.println(e.getMessage());
            }
        }
    }
    private static void printHelp(HelpFormatter helpFormatter, String projectName, Options options, boolean autoUsage) {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        helpFormatter.printHelp(writer, helpFormatter.getWidth(), projectName, null, options, 
                helpFormatter.getLeftPadding(), helpFormatter.getDescPadding(), null, autoUsage);
        writer.flush();
        String lineSeparator = System.getProperty("line.separator");
        String help = out.toString();
        help = help.replaceAll("(usage: " + projectName +")", "$1 [options...] <input-filename>" + lineSeparator);
        help = help + "<input-filename>                  the file to convert" + lineSeparator;
        help = help + lineSeparator;
        help = help + lineSeparator;
        help = help + "examples:" + lineSeparator;
        help = help + lineSeparator;
        help = help + projectName + " Chronos.xmpuzzle" + lineSeparator;
        help = help + "    saves the 'Chronos' puzzle file as multiple CAD files (one per shape) in the current folder with default options." + lineSeparator;
        help = help + lineSeparator;
        help = help + projectName + " -S4.0 Chronos.xmpuzzle" + lineSeparator;
        help = help + "    saves each file with small 4.0mm voxel size" + lineSeparator;
        help = help + lineSeparator;
        help = help + projectName + " --single Chronos.xmpuzzle" + lineSeparator;
        help = help + "    saves as one CAD file with all shapes in the same file" + lineSeparator;
        help = help + lineSeparator;
        help = help + projectName + " -r7,9 Chronos.xmpuzzle" + lineSeparator;
        help = help + "    saves CAD files but it does not try and rotate pieces 7 or 9, even if they would print better" + lineSeparator;
        help = help + lineSeparator;
        help = help + projectName + " -o Rayner Chronos.xmpuzzle" + lineSeparator;
        help = help + "    saves CAD files with different prefix to the source file (e.g. Rayner-P1-S02.scad)" + lineSeparator;
        System.out.println(help);
    }
    
    private static String getShapeName(int id) {
        int actualId = id;
        String leftPadding = "";
        if (actualId > 0 && actualId < 10) {
            leftPadding = "0";
        }
        return "S" + leftPadding + actualId;
    }

}

