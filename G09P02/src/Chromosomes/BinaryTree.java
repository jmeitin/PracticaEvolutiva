package Chromosomes;

import java.util.ArrayList;
import java.util.Random;

public class BinaryTree {
	Random rand = new Random();
	private String[] node_functions = {"add", "sub", "mul", ""};
	private String[] leaf_terminals = {"x", "-2", "-1", "0", "1", "2"};
	
	protected String root = null;
	protected BinaryTree left_child = null;
	protected BinaryTree right_child = null;
	
	private int num_nodes = 0;
	private int max_depth = 0;
	private int depth;
	private boolean useIF;
	private boolean is_leaf = false;
	private boolean is_root = false;
	
	// GETS====================================================
	public int getDepth() {	return depth;	}
	
	// METODOS PUBLICOS =======================================
	BinaryTree(int n, boolean r){
		is_root = r;
		initializeRandomTree(n);
	}
	
	
	// Método que inicializa el árbol con n nodos aleatorios y equilibrados
    public void initializeRandomTree(int n) {
        // Creamos la raíz del árbol
       
    	int index = 0;
    	 this.num_nodes = 1;
         this.max_depth = 1;
         
        // LEAF
        if (n == 1) {
        	is_leaf = true;
        	index = (int)(rand.nextDouble() * (leaf_terminals.length - 1));
        	this.root = leaf_terminals[index];
        	
            return;
        }
        
        index = (int)(rand.nextDouble() * (node_functions.length - 1));
    	this.root = node_functions[index];
        
        
        // Dividimos el número de nodos restantes en dos sub-árboles
        int num_nodes_left = n / 2;
        int num_nodes_right = n - num_nodes_left - 1;
        
        // Generate 2 sub-trees
        BinaryTree left_child = new BinaryTree(num_nodes_left, false); //isn't root
        BinaryTree right_child = new BinaryTree(num_nodes_right, false); //isn't root
        
        // Asignamos los sub-árboles generados a la raíz
        this.left_child = left_child;
        this.right_child = right_child;
        
        // Actualizamos el número de nodos y la profundidad del árbol
        this.num_nodes += num_nodes_left + num_nodes_right;
        this.max_depth = 1 + Math.max(left_child.max_depth, right_child.max_depth);
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
