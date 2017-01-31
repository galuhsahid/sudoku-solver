package t1bsc;

import java.io.*;
import java.util.*;

import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

public class Sudoku {
	private final int size;
	private final int dim;
	private final int[][] board;

	/**
	 * Create a new Sudoku object
	 * 
	 * @param dim
	 *            dimension of the sudoku
	 * @param board
	 * 			  the board of the sudoku, already pre-filled with entries
	 * @return a new Sudoku object
	 */
	public Sudoku(int dim, int[][] board) {
		this.dim = dim;
		this.size = dim*dim;
		this.board = new int[size][size];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j] == 0)
					this.board[i][j] = 0;
				else
					this.board[i][j] = board[i][j];
			}
		}
		
	}
	
	/**
	 * Read sudoku from file
	 * 
	 * @param fn
	 *            file name
	 */
	public static Sudoku readFile(String fn) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(fn)));
		int size = Integer.parseInt(in.readLine());
		int dim = (int)Math.sqrt((double)size);
		int[][] s = new int[size][size];
		String line;
		int row = 0;
		while(((line = in.readLine()) != null) && (row < size)){
			line = line.replaceAll("\\s+","");
			for (int col = 0; col < line.length(); col++) {
				switch (line.charAt(col)) {
				case '0':
					s[row][col] = 0;
					break;
				case '\n':
					break;
				default:
					s[row][col] = Integer.parseInt(String.valueOf(line.charAt(col)));
					break;
				}
			}
			row++;
		}
		in.close();
		return new Sudoku(dim, s);
	}

	/**
	 * Build knowledge base based on the constraints for the Sudoku problem
	 * 
	 * @return allClauses
	 *            a set of clauses containing constraints for the Sudoku problem
	 */
	public Set<Clause> makeKB() {
		Set<Clause> allClauses = new HashSet<Clause>();	

		allClauses = fillEntries(allClauses);
		allClauses = cellConstraint(allClauses);
		allClauses = rowConstraint(allClauses);
		allClauses = colConstraint(allClauses);
		allClauses = boxConstraint(allClauses);
        return allClauses;
	}
	
	/**
	 * Fill Sudoku with entries from the problem
	 * 
	 * @param cl
	 *            a set of clauses
	 * @return cl
	 */
	public Set<Clause> fillEntries(Set<Clause> cl) {
		for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                if (board[i-1][j-1] != 0) {
                	String var = cell2Var(i,j,board[i-1][j-1]);
                	
                	Literal lit = new Literal(new PropositionSymbol(var));
                	
                	Clause clause = new Clause(lit);
                	cl.add(clause);
                }
            }
		}

		return cl;
		
	}
	
	/**
	 * Enforce cell constraint (each number appears exactly once in each cell)
	 * 
	 * @param cl
	 *            a set of clauses
	 * @return cl
	 */
	public Set<Clause> cellConstraint(Set<Clause> cl) {
		
		for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
            	List<Literal> litList = new ArrayList<Literal>();
                for (int k1 = 1; k1 < size+1; k1++) {
                	// Minimal encoding: at least one number in each entry
                	String var = cell2Var(i,j,k1);
            		Literal lit = new Literal(new PropositionSymbol(var));
            		litList.add(lit);
            		
            		
            		// Extended encoding: at most one number in each entry
                    for (int k2 = k1 + 1; k2 < size+1; k2++) {
                    	String var1 = cell2Var(i,j,k1);
                    	String var2 = cell2Var(i,j,k2);
                    	
                    	Literal lit1 = new Literal(new PropositionSymbol(var1), false);
                    	Literal lit2 = new Literal(new PropositionSymbol(var2), false);
                       
                    	Clause clause1 = new Clause (lit1, lit2);
                    	
                        cl.add(clause1);
                        
                  
                    }
                }
                
                Clause clause = new Clause(litList);
            	cl.add(clause);
            
    
            }
        }
		
		return cl;
	}
	
	/**
	 * Enforce row constraint (each number appears exactly once in each row)
	 * 
	 * @param cl
	 *            a set of clauses
	 * @return cl
	 */
	// Row constraint: Each number appears exactly once in each row
	public Set<Clause> rowConstraint(Set<Clause> cl) {
		
		for (int i = 1; i < size+1; i++) {
            for (int k = 1; k < size+1; k++) {
            	
            	// Minimal: Each number appears at least once in each row
            	List<Literal> litList = new ArrayList<Literal>();
                for (int j = 1; j < size+1; j++) {
                	String var1 = cell2Var(i,j,k);
                	Literal lit1 = new Literal(new PropositionSymbol(var1));
                	litList.add(lit1);
                }
                
                Clause clause = new Clause(litList);
            	cl.add(clause);
            	
            	// Extended: Each number appears at most once in each row
                for (int j1 = 1; j1 < size; j1++) {
                    for (int j2 = j1 + 1; j2 < size+1; j2++) {
                    	String var2 = cell2Var(i,j1,k);
                    	String var3 = cell2Var(i,j2,k);
                    	Literal lit2 = new Literal(new PropositionSymbol(var2), false);
                    	Literal lit3 = new Literal(new PropositionSymbol(var3), false);
   
                    	Clause clause1 = new Clause(lit2, lit3);
                    	cl.add(clause1);
                    	
                    }
                } 
            }
        }
		
		return cl;
		
	}
	
	/**
	 * Enforce column constraint (each number appears exactly once in each column)
	 * 
	 * @param cl
	 *            a set of clauses
	 * @return cl
	 */
	public Set<Clause> colConstraint(Set<Clause> cl) {
		for (int j = 1; j < size+1; j++) {
            for (int k = 1; k < size+1; k++) {
            	
            	// Minimal: Each number appears at least once in each column
            	List<Literal> litList = new ArrayList<Literal>();
                for (int i = 1; i < size+1; i++) {   	
                	String var1 = cell2Var(i,j,k);
                	Literal lit1 = new Literal(new PropositionSymbol(var1));
                	litList.add(lit1); 
                }
            
            	Clause clause = new Clause(litList);
            	cl.add(clause);
                
            	// Extended: Each number appears at least once in each column
                for (int i1 = 1; i1 < size; i1++) {
                    for (int i2 = i1 + 1; i2 < size+1; i2++) {
                    	String var2 = cell2Var(i1,j,k);
                    	String var3 = cell2Var(i2,j,k);
                    	
                    	Literal lit2 = new Literal(new PropositionSymbol(var2), false);
                    	Literal lit3 = new Literal(new PropositionSymbol(var3), false);
                    	
                    	Clause clause1 = new Clause(lit2, lit3);
                    	cl.add(clause1);
                    }
                }
            }
        }
		
		return cl;
		
	}
	
	/**
	 * Enforce box constraint (each number appears exactly once in each box)
	 * 
	 * @param cl
	 *            a set of clauses
	 * @return cl
	 */
	public Set<Clause> boxConstraint(Set<Clause> cl) {
		for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                for (int k = 1; k < size+1; k++) {
                    
                	List<Literal> litList = new ArrayList<Literal>();
                	
                    for (int s = 0; s < dim; s++) {
                        for (int t = 0; t < dim; t++) {
                        	String var = cell2Var((i*dim+s)+1, (j*dim+t)+1, k);
                        	Literal lit = new Literal(new PropositionSymbol(var));
                        	litList.add(lit);
                        }
                    }
                    
                    Clause clause = new Clause(litList);
                	cl.add(clause);

                }
            }
		}
		
		return cl;
		
	}
	
	/**
	 * Convert cell coordinates to a String of variable
	 * 
	 * @param row
	 * @param col
	 * @param val         
	 * @return cl
	 */
	public String cell2Var(int row, int col, int val) {
		String calc = Integer.toString(100*row+10*col+val);
		return "v"+calc;
	}
	
	/**
	 * Format model to object Sudoku
	 * 
	 * @param model
	 * @return Sudoku
	 */
	public Sudoku formatSolution(Model m) {
		int board[][] = new int[size][size];
		
		 for (int i = 1; i < size+1; i++) {
	            for (int j = 1; j < size+1; j++) {
	                for (int k = 1; k < size+1; k++) {
	                	String var = cell2Var(i, j, k);
	                	PropositionSymbol ps = new PropositionSymbol(var);
	                    if (m.isTrue(ps)) {
	                        board[i-1][j-1] = k;
	                        break;
	                    }
	                }
	            }
	      }
		
		
        return new Sudoku((int)(Math.sqrt((double)size)), board);
	}
	
	/**
	 * Convert object Sudoku to String
	 *
	 * @return String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    sb.append('0' + " ");
                } else {
                    sb.append(board[i][j] + " ");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
	}
}
