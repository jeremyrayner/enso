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
package com.javanicus.puzzles.enso.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.javanicus.puzzles.enso.entities.Problem;
import com.javanicus.puzzles.enso.entities.Shape;
import com.javanicus.puzzles.enso.entities.Voxel;

public class PuzzleParser {
    public List<Problem> parse(String xml) throws ParserConfigurationException, SAXException, IOException {
        List<Problem> problems = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = builder.parse(is);

        NodeList puzzleNodes = document.getElementsByTagName("puzzle");
        for (int puzzleNodeIndex = 0; puzzleNodeIndex < puzzleNodes.getLength(); puzzleNodeIndex++) {
            Element puzzleNode = (Element) puzzleNodes.item(puzzleNodeIndex);
            final String[] comment = new String[] {""};
            forEachTag(puzzleNode, "comment", (commentNode) -> {
                if (commentNode != null) {
                    String commentText = commentNode.getTextContent();
                    if (commentText != null && !commentText.trim().isEmpty()) {
                        comment[0] = commentText;
                    }
                }
            });
            
            final boolean[] supportedGridType = new boolean[] {true};
            forEachTag(puzzleNode, "gridType", (gridTypeNode) -> {
                if (!"0".equals(gridTypeNode.getAttribute("type"))) {
                    supportedGridType[0] = false;
                }
            });
            if (supportedGridType[0]) {
                List<Shape> shapes = new ArrayList<>();
                forEachTag(puzzleNode, "shapes", (shapesNode) -> {
                    forEachTag(shapesNode, "voxel", (voxelNode) -> {
                        Voxel voxel = new Voxel();
                        voxel.setXMax(Integer.parseInt(voxelNode.getAttribute("x")));
                        voxel.setYMax(Integer.parseInt(voxelNode.getAttribute("y")));
                        voxel.setZMax(Integer.parseInt(voxelNode.getAttribute("z")));
                        voxel.setText(voxelNode.getTextContent());
    
                        Shape shape = new Shape();
                        shape.setVoxel(voxel);
                        shapes.add(shape);
                    });
                });
    
                forEachTag(puzzleNode, "problems", (problemsNode) -> {
                    forEachTag(problemsNode, "problem", (problemNode) -> {
                        Problem problem = new Problem(comment[0]);
                        forEachTag(problemNode, "shapes", (problemShapesNode) -> {
                            forEachTag(problemShapesNode, "shape", (problemShapeNode) -> {
                                String idString = problemShapeNode.getAttribute("id");
                                int id = Integer.parseInt(idString);
                                String countString = problemShapeNode.getAttribute("count");
                                int count = 1;
                                if (countString != null && !countString.trim().equals("")) {
                                    count = Integer.parseInt(countString);
                                }
                                // 'max' pieces in range [min - max] will override a strict 'count'
                                String maxString = problemShapeNode.getAttribute("max");
                                if (maxString != null && !maxString.trim().equals("")) {
                                    count = Integer.parseInt(maxString);
                                }
                                String name = getShapeName(id);
                                problem.getUniqueShapes().put(name, shapes.get(id));
                                for (int c = 0; c < count; c++) {
                                    if (count > 1) {
                                        name = getShapeName(id) + "_" + (c + 1);
                                    }
                                    problem.getAllShapes().put(name, shapes.get(id));
                                }
                            });
                        });
                        problems.add(problem);
                    });
                });
            } else {
                System.out.println("P" + (puzzleNodeIndex + 1) + " is using an unsupported grid type (supported: type 0 - Brick)");
            }
        }
        return problems;
    }
    
    interface ElementFunction {
        void apply(Element e);
    }

    private void forEachTag(Element element, String name, ElementFunction elementFunction) {
        NodeList nodes = element.getElementsByTagName(name);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            elementFunction.apply(e);
        }
    }
    
    private String getShapeName(int id) {
        int actualId = id + 1;
        String leftPadding = "";
        if (actualId > 0 && actualId < 10) {
            leftPadding = "0";
        }
        return "S" + leftPadding + actualId;
    }

}

