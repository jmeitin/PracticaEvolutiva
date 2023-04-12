package Chromosomes;

import java.util.ArrayList;

public class BinaryTree {
	protected String root = null;
	protected BinaryTree left_child = null;
	protected BinaryTree right_child = null;
	
	private int num_nodes;
	private int max_depth;
	private int depth;
	private boolean useIF;
	private boolean is_leaf;
	private boolean is_root;
	
	public int getDepth() {
		return depth;
	}
	// Devuelve el arbol en forma de array
	public ArrayList<String> toArray(){
		ArrayList<String> array = new ArrayList<String>();
		inorderTraversal(array, this);
		return array;
	}
	
	private void inorderTraversal(ArrayList<String> array, BinaryTree tree) {
		if(tree != null) {
			inorderTraversal(array, tree.left_child);
			array.add(tree.root);
			inorderTraversal(array, tree.right_child);
		}
	}
	
	public void insert(String value) {
	    if (root == null) {
	        root = value;
	        depth++;
	    }
	    
	    else if (left_child == null) {
	    	left_child = new BinaryTree();
	    	left_child.insert(value);
	    	depth++;
	    }
	    else if (right_child == null) {
	    	right_child = new BinaryTree();
	    	right_child.insert(value);
	    }
	    else if(left_child.getDepth() == right_child.getDepth()) {
	    	left_child = new BinaryTree();
	    	left_child.insert(value);
	    	depth++;
	    }

	   
	}
	
	
	private void toArrayAux(ArrayList<String> array, BinaryTree a){
//		array.add(a.root);
//		for(int i = 0; i < a.hijos.size(); i++){
//			toArrayAux(array, a.hijos.get(i));
//		}
	}
	
//	public int inicializacionCompleta(int p, int nodos){
//		int n = nodos;
//		int nHijos = 2;
//		if(p < max_prof){
//			setProfundidad(p);
//			Random rnd = new Random();
//			int func = 0;
//			if(useIF){
//				func = rnd.nextInt(Cromosoma.funciones.length);
//			}
//			else{
//				func = rnd.nextInt(Cromosoma.funciones.length-1);
//			}
//			this.valor = Cromosoma.funciones[func];
//			this.setEsRaiz(true)
//		}
//	}


}
