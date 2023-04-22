package Chromosomes;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class BinaryTree {
	Random rand = new Random();
	private String[] node_functions = {"add", "sub", "mul"};
	private String[] leaf_terminals = {"x", "-2", "-1", "0", "1", "2"};
	
	protected String root = null;
	protected BinaryTree left_child = null;
	protected BinaryTree right_child = null;
	
	private int num_nodes = 0;
	private int depth = 0;
	private boolean useIF;
	private boolean is_leaf = false;
	private boolean is_root = false;
	
	// GETS====================================================
	public int getDepth() {	return depth;	}
	
	// METODOS PUBLICOS =======================================
	public BinaryTree(boolean r){ //true if you are creating tree from scratch
		is_root = r;
	}	

    public void FullInitalization(int profundidad) {   
    	if (profundidad > 0) {
    		int index = 0;
        	this.num_nodes = (int)Math.pow(2,  profundidad) - 1;
            this.depth = profundidad;
             
            // IS LEAF/TERMINAL
            if (profundidad == 1) {
            	is_leaf = true;
            	index = (int)(rand.nextDouble() * (leaf_terminals.length - 1));
            	this.root = leaf_terminals[index];
            	
                return;
            }
            
            // IS FUNCTION/TERMINAL
            index = (int)(rand.nextDouble() * (node_functions.length - 1));
        	this.root = node_functions[index];
            
            // CREATE LEFT CHILD & RIGHT CHILD
            left_child = new BinaryTree(false); //isn't root
            right_child = new BinaryTree(false); //isn't root
            left_child.FullInitalization(profundidad - 1);
            right_child.FullInitalization(profundidad - 1);
    	}
    }
    
    public void GrowInitalization(int profundidad) {   
    	if (profundidad > 0) {
    		int index = 0;
        	this.num_nodes = 1;
            this.depth = 1;
             
            // IS LEAF/TERMINAL
            if (profundidad == 1) {
            	is_leaf = true;
            	index = (int)(rand.nextDouble() * (leaf_terminals.length - 1));
            	this.root = leaf_terminals[index];
            	
                return;
            }
            
            // IS FUNCTION/TERMINAL
            index = (int)(rand.nextDouble() * (node_functions.length - 1));
        	this.root = node_functions[index];
        	
        	//CALCULATE DEPTHS:
        	// one of the 2 sides must have depth == [profundidad - 1]
        	// the other one doesn't matter ?
        	int left_depth = profundidad - 1;
        	int right_depth = profundidad - 1;
        	//choose side
        	boolean select_left = rand.nextBoolean();
        	if(select_left)
        		left_depth = 1 + (int)(rand.nextDouble() * (profundidad - 2)); // [1, profundiad - 1]
        	else right_depth = 1 + (int)(rand.nextDouble() * (profundidad - 2)); // [1, profundiad - 1]
            
            // CREATE LEFT CHILD & RIGHT CHILD
            left_child = new BinaryTree(false); //isn't root
            right_child = new BinaryTree(false); //isn't root
            left_child.FullInitalization(left_depth);
            right_child.FullInitalization(right_depth);
            
            // Update num_nodes and max_depth
            this.num_nodes += left_child.num_nodes + right_child.num_nodes;
            this.depth = 1 + Math.max(left_child.depth, right_child.depth);
    	}
    }
    
    //SET=============================================
    public void setRoot(String r) {
    	root = r;
    }
    public boolean setLeftChild(BinaryTree left) {
    	if(root != null) {
    		left_child = left;
    		return true;
    	}
    	return false;
    }
    public boolean setRightChild(BinaryTree right) {
    	if(root != null) {
    		right_child = right;
    		return true;
    	}
    	return false;
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
	
	// Obtener función del arbol
	public Function<Double, Double> getFunction() {
		ArrayList<String> treeArray = toArray(); // obtener el árbol en formato de array en inorden
	    Function<Double, Double> result = getFunctionHelper(treeArray, new int[]{0}); // llamar al helper con un índice inicial de 0
	    return result;
	}
	
	private boolean isLeaf(String node) {
	    for (String terminal : leaf_terminals) {
	        if (node.equals(terminal)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private Function<Double, Double> getFunctionHelper(ArrayList<String> tree, int[] index) {
	    String current = tree.get(index[0]);
	    index[0]++; // incrementar el índice para apuntar al siguiente elemento en el árbol
	    
	    if (isLeaf(current)) { // si el nodo actual es una hoja, devolver su valor numérico
	        double val = Double.parseDouble(current);
	        return x -> val;
	    } else { // si el nodo actual es una función, crear una función que llame a sus hijos
	        Function<Double, Double> left = getFunctionHelper(tree, index); // obtener la función del hijo izquierdo
	        Function<Double, Double> right = getFunctionHelper(tree, index); // obtener la función del hijo derecho
	        
	        switch (current) {
	            case "add":
	                return x -> left.apply(x) + right.apply(x);
	            case "sub":
	                return x -> left.apply(x) - right.apply(x);
	            case "mul":
	                return x -> left.apply(x) * right.apply(x);
	            default:
	                throw new IllegalArgumentException("Función desconocida: " + current);
	        }
	    }
	}

}
