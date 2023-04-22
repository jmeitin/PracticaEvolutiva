package Main;

import java.util.ArrayList;
import java.util.function.Function;

import Chromosomes.BinaryTree;
import View.MainView;

public class main {
	public static void main(String args[]) {

		BinaryTree tree = new BinaryTree(true);
        tree.setRoot("add", true, false); //root
        BinaryTree auxLeft = new BinaryTree(false);
        auxLeft.setRoot("mul", false, false);
        BinaryTree auxRight = new BinaryTree(false);
        auxRight.setRoot("1", false, true);
        BinaryTree auxLeftLeft = new BinaryTree(false);
        auxLeftLeft.setRoot("2", false, true); //leaf
        BinaryTree auxLeftRight = new BinaryTree(false);
        auxLeftRight.setRoot("x", false, true); //leaf
        auxLeft.setLeftChild(auxLeftLeft, false, false);
        auxLeft.setRightChild(auxLeftRight, false, false);
        tree.setLeftChild(auxLeft, false, false); 
        tree.setRightChild(auxRight, false, false); 

//		Function<Double, Double> function = tree.getFunction();
//		System.out.println(function.apply(1d)); // 3
//		System.out.println(function.apply(0.5)); // 2
//		System.out.println(function.apply(2d)); // 5
		
        ArrayList<String> a = tree.toArray();
        System.out.println(a);
		
		tree.MutateSubTree(tree, 0.6);
		a = tree.toArray();		
		System.out.println(a);
		
		System.out.println("HOLAAAA");
		new MainView();
	}
}
