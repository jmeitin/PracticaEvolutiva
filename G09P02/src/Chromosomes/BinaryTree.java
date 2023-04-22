package Chromosomes;

import java.util.ArrayList;
import java.util.Random;

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
    
    //SET===========================================================================
    public void setRoot(String value, boolean r, boolean l) {
    	root = value;
    	is_root = r;
    	is_leaf = l;
    }
    public boolean setLeftChild(BinaryTree left, boolean r, boolean l) {
    	if(root != null) {
    		left_child = left;
    		is_root = r;
        	is_leaf = l;
    		return true;
    	}
    	return false;
    }
    public boolean setRightChild(BinaryTree right, boolean r, boolean l) {
    	if(root != null) {
    		right_child = right;
    		is_root = r;
        	is_leaf = l;
    		return true;
    	}
    	return false;
    }
    
	// GET ======================================================================
    // Devuelve el arbol en forma de array
    public int getDepth() {	return depth;	}
    
    public BinaryTree getCopy() {
    	BinaryTree aux =new BinaryTree(true);
    	aux.getCopyAux(this);
    	
    	return aux;
    }
    
    private void getCopyAux(BinaryTree aux) {
    	root = aux.root;
    	if(aux.left_child != null) {
    		left_child = new BinaryTree(false);
    		left_child.getCopyAux(aux.left_child);
    	}
    	if(aux.right_child != null) {
    		right_child = new BinaryTree(false);
    		right_child.getCopyAux(aux.right_child);
    	}
    	else if (aux.left_child == null && root != null) {
    		is_leaf = true;
    	}
    }
    
    // Returns Array with Tree in INORDEN
	public ArrayList<String> toArray(){
		ArrayList<String> array = new ArrayList<String>();
		inorderTraversal(array, this);
		return array;
	}
	//Aux Method for toArray
	private void inorderTraversal(ArrayList<String> array, BinaryTree tree) {
		if(tree != null) {
			inorderTraversal(array, tree.left_child);
			array.add(tree.root);
			inorderTraversal(array, tree.right_child);
		}
	}
	

}
