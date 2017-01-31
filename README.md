# sudoku-solver
Implementation of the DPLL and WalkSAT algorithm to solve sudoku puzzles

## Implementation
The representation of sudoku as a SAT problem is implemented by referring to [paper][this paper]..

The implemented strategies are DPLL (dpll) and WalkSAT (walksat).
- DPLL: systematic backtracking algorithm to solve SAT problems (command line parameter: dpll)
- WalkSAT: local search algorithm to solve SAT problems (command line parameter: walksat)

## Running the program
- Clone the repository
- Run the following command:

```java
java SudokuSolver <strategy> <input_file> <output_file>
```

For example, if you're running:

```java
java SudokuSolver walksat test.sudoku test.solution
```

It means that you're looking for a sudoku solution of test.sudoku using the WalkSAT strategy, where the solution will be saved in test.solution.

[paper]: https://pdfs.semanticscholar.org/3d74/f5201b30772620015b8e13f4da68ea559dfe.pdf
