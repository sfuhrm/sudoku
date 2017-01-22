/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.sfuhrm.sudoku;

/**
 *
 * @author fury
 */
public class GameLogic {

	private long start = 0;
	private Riddle playField;
	private Riddle solution;

	public boolean isFinished() {
		return playField.getSetCount() == GameMatrix.SIZE*GameMatrix.SIZE;
	}

	public boolean isSuccess() {
		return playField.equals(solution);
	}

	public void newGame() {
		solution = Creator.createFull();
		playField = Creator.createRiddle(solution);

		start = System.currentTimeMillis();
	}

	public long getDuration() {
		long now = System.currentTimeMillis();
		return now - start;
	}
}
