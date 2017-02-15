package t1bsc;

import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.inference.WalkSAT;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

import java.io.*;
import java.util.*;

public class SudokuSolver {
	
	public static void main(String[] args) throws Exception {		
		String strategy = args[0];
		BufferedReader br = new BufferedReader(new FileReader(new File(args[1])));
		FileWriter writer = new FileWriter(new File(args[2]));
		
		Sudoku mySudoku = Sudoku.readFile(args[1]);
		Set<Clause> kb = mySudoku.makeKB();
		
		
		// WalkSAT
		if(strategy.equals("walksat")){
			WalkSAT walkSAT = new WalkSAT();
			Model m = walkSAT.walkSAT(kb, 0.5, Integer.MAX_VALUE);
			Sudoku solutionWalkSAT = mySudoku.formatSolution(m);
			writer.write(solutionWalkSAT.toString());
		}
		
		
		// DPLLSatisfiable
		 if(strategy.equals("dpll")){
			DPLLSatisfiable ds = new DPLLSatisfiable();
	
			List<PropositionSymbol> ps = new ArrayList<PropositionSymbol>();
			Set<PropositionSymbol> setPs = getPropSymbols(kb);
			ps.addAll(setPs);
			ds.dpll(kb, ps, new Model());
			Model finalModel = ds.getFinalModel();
			Sudoku solutionDPLL = mySudoku.formatSolution(finalModel);
			writer.write(solutionDPLL.toString());
		 }
		
			
		br.close();
		writer.flush();
		writer.close();

	}
	
	/** 
	 * @param kb
	 *            the knowledge base consisting of a set of clauses
	 * @return a set of proposition symbols contained in the kb
	 */
	public static Set<PropositionSymbol> getPropSymbols(Set<Clause> kb) {
		Set<PropositionSymbol> allPs = new HashSet<PropositionSymbol>();
		for(Clause clause: kb) {
			Set<PropositionSymbol> currentPs = clause.getSymbols();
			allPs.addAll(currentPs);
		}	
		return allPs;
	}
}
