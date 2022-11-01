/*
Sudoku - a fast Java Sudoku game creation library.
Copyright (C) 2017-2018  Stephan Fuhrmann

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
Boston, MA  02110-1301, USA.
 */
package de.sfuhrm.sudoku;

/**
 * A factory for creating game matrix objects.
 * @see GameMatrix
 * @author Stephan Fuhrmann
 */
public final class GameMatrixFactory {

    /** Creates a new game matrix factory instance. */
    public GameMatrixFactory() {
    }

    /** Creates a new 9x9 GameMatrix instance.
     * @return a GameMatrix instance with all fields initialized
     * to {@link GameSchema#getUnsetValue() unset}.
     */
    public GameMatrix newGameMatrix() {
        return newGameMatrix(GameSchemas.SCHEMA_9X9);
    }

    /** Creates a new GameMatrix instance.
     * @param gameSchema the dimensions of the game matrix to create.
     * @return a GameMatrix instance with all fields initialized
     * to {@link GameSchema#getUnsetValue() unset}.
     */
    public GameMatrix newGameMatrix(final GameSchema gameSchema) {
        return new GameMatrixImpl(gameSchema);
    }

    /** Creates a new 9x9 Riddle instance.
     * @return a Riddle instance with all fields initialized
     * to {@link GameSchema#getUnsetValue() unset}.
     */
    public Riddle newRiddle() {
        return newRiddle(GameSchemas.SCHEMA_9X9);
    }

    /** Creates a new Riddle instance.
     * @param gameSchema the dimensions of the riddle to create.
     * @return a Riddle instance with all fields initialized
     * to {@link GameSchema#getUnsetValue() unset}.
     */
    public Riddle newRiddle(final GameSchema gameSchema) {
        return new RiddleImpl(gameSchema);
    }
}
