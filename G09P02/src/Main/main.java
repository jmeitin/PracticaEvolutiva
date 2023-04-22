package Main;
import java.util.ArrayList;
import java.util.function.Function;

import Chromosomes.BinaryTree;
import View.MainView;

public class main {
	public static void main(String args[]) {
		
		BinaryTree tree = new BinaryTree(true);
		tree.setRoot("add");
		BinaryTree auxLeft = new BinaryTree(false);
		auxLeft.setRoot("mul");
		BinaryTree auxRight = new BinaryTree(false);
		auxRight.setRoot("1");
		BinaryTree auxLeftLeft = new BinaryTree(false);
		auxLeftLeft.setRoot("2");
		BinaryTree auxLeftRight = new BinaryTree(false);
		auxLeftRight.setRoot("x");
		auxLeft.setLeftChild(auxLeftLeft);
		auxLeft.setRightChild(auxLeftRight);
		tree.setLeftChild(auxLeft);
		tree.setRightChild(auxRight);
		
		ArrayList<String> a = tree.toArray();
		
		System.out.println(a);
		
		// 2x + 1
		Function<Double, Double> function = tree.getFunction();
		System.out.println(function.apply(1d)); // 3
		System.out.println(function.apply(0.5)); // 2
		System.out.println(function.apply(2d)); // 5
		//new MainView();
	}
}
