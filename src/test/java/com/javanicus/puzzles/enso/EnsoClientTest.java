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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.javanicus.puzzles.enso.client.EnsoClient;
import com.javanicus.puzzles.enso.client.UserOptions;

public class EnsoClientTest {
  @Test
  public void evaluatesExpression() throws Exception {
    EnsoClient enso = new EnsoClient();
    UserOptions userOptions = new UserOptions();
    assertEquals("success", enso.processUserOptions(userOptions));
  }
}
