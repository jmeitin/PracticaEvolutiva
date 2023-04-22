package Main;
import java.util.ArrayList;

import Chromosomes.BinaryTree;
import View.MainView;

public class main {
	public static void main(String args[]) {
		
		BinaryTree tree = new BinaryTree(true);
		tree.setRoot("add", true, false); //root
		BinaryTree auxLeft = new BinaryTree(false);
		auxLeft.setRoot("mul", false, false);
		BinaryTree auxRight = new BinaryTree(false);
		auxRight.setRoot("x", false, false);
		BinaryTree auxLeftLeft = new BinaryTree(false);
		auxLeftLeft.setRoot("2", false, true); //leaf
		BinaryTree auxLeftRight = new BinaryTree(false);
		auxLeftRight.setRoot("1", false, true); //leaf
		auxLeft.setLeftChild(auxLeftLeft, false, false);
		auxLeft.setRightChild(auxLeftRight, false, false);
		tree.setLeftChild(auxLeft, false, false); 
		tree.setRightChild(auxRight, false, false); 
		
		ArrayList<String> a = tree.toArray();		
		System.out.println(a);
		
		BinaryTree t = tree.getCopy();
		t.setRoot("mull", true, false);
		a = t.toArray();		
		System.out.println(a);
		
		
		a = tree.toArray();		
		System.out.println(a);
		
		System.out.println("HOLAAAA");
		new MainView();
	}
}
