# Proposal: Selectable Sudoku Difficulty + Difficulty Points

## Target vision

1. **Select difficulty during puzzle creation** (e.g., VERY_EASY to
   VERY_HARD).
2. **Rate required solving techniques** instead of classifying only by the
   number of empty cells.
3. **Stable generation flow**: generator creates a puzzle, solver analyzes the
   required techniques, then the puzzle is assigned to the target class.

## Already implemented (baseline)

- New creation API with difficulty:
  - `Creator.createRiddle(fullMatrix, Difficulty)`
- New `Difficulty` enum with schema-dependent guidance values for the number
  of cells to clear.

This already allows callers to request a desired difficulty level when
creating puzzles.

## Proposed points model

Difficulty is calculated as the sum of steps needed by the solver.

### Basic techniques

- **Naked Single**: 1 point
- **Hidden Single in row/column/block ("1 out of 9")**: 2 points

### Intermediate techniques

- **Naked Pair/Triple**: 4 / 6 points
- **Pointing Pair / Box-Line Reduction**: 5 points

### Advanced techniques

- **X-Wing**: 12 points
- **Swordfish**: 18 points
- **XY-Wing**: 15 points

### Optional (expert)

- **Coloring / Chains**: 20+ points
- **Backtracking hint**: high penalty (e.g., +100) so such puzzles land in
  "Expert/Very Hard".

## Suggested classes for 9x9

- **VERY_EASY**: 0–40 points
- **EASY**: 41–90 points
- **MEDIUM**: 91–160 points
- **HARD**: 161–260 points
- **VERY_HARD**: 261+

> Thresholds should be calibrated using a sample set (e.g., 1,000 puzzles per
> class).

## Technical implementation approach

1. **Instrument the solver**
   - Log each applied technique as a `SolveStep` with type, position,
     candidates, and points.
2. **Aggregate score**
   - `DifficultyScore = sum(step.points)`
   - plus secondary metrics (step count, maximum technique level).
3. **Generator with feedback loop**
   - Generate puzzle → analyze → if it misses the target class, continue
     mutating (clear more/fewer cells, optionally reshape structures).
4. **API extension**
   - `CreationResult { Riddle riddle; DifficultyScore score;
     List<SolveStep> path; }`

## Benefits

- Better player expectations: "hard" then reflects harder deduction instead of
  only fewer givens.
- Explainability: for each puzzle you can show **which** techniques are
  required.
- Strong foundation for hint systems, learning mode, and progression.
