package Main;
import java.util.ArrayList;

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
		auxLeftRight.setRoot("1");
		auxLeft.setLeftChild(auxLeftLeft);
		auxLeft.setRightChild(auxLeftRight);
		tree.setLeftChild(auxLeft);
		tree.setRightChild(auxRight);
		
		ArrayList<String> a = tree.toArray();
		
		System.out.println(a);
		System.out.println("HOLAAAA");
		new MainView();
	}
}
