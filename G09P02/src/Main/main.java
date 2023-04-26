package Main;

import Chromosomes.BinaryTree;
import Chromosomes.ChromosomeP3;
import CrossAlgorithms.P3.TreeCross;
import View.MainView;

public class main {
	public static void main(String args[]) {
		
//		for(int i = 0; i < 30; i++) {
//			ChromosomeP3 a = new ChromosomeP3(10, 3, "COMPLETA");
//			ChromosomeP3 b = new ChromosomeP3(10, 3, "COMPLETA");
//			BinaryTree a_tree = a.getTree();
//			BinaryTree b_tree = b.getTree();
//			
//			System.out.println(a_tree.toArray());
//			System.out.println(b_tree.toArray());
//			
//			
//			TreeCross cross = new TreeCross();
//			
//			cross.cross2(a, b);
//			
//			a_tree = a.getTree();
//		    b_tree = b.getTree();
//			System.out.println("----------------");
//			System.out.println(a_tree.toArray());
//			System.out.println(b_tree.toArray());
//		}
//		
		
		
		new MainView();
	}
}
