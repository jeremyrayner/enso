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
package com.javanicus.puzzles.enso.file;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class FileUtils {
    public String gunzip(Path inputPath) throws IOException {
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputPath.toFile()))){
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
                byte[] buffer = new byte[1024];
                int len;
                while((len = in.read(buffer)) != -1){
                    out.write(buffer, 0, len);
                }
                return out.toString("UTF-8");
            }
        }
    }
}
