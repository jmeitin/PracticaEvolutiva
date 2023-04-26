package Chromosomes;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

public class BinaryTree {
	static Random rand = new Random();
	static private String[] node_functions = { "add", "sub", "mul" };
	static private String[] leaf_terminals = { "x", "-2", "-1", "0", "1", "2" };

	protected String root = null;
	protected BinaryTree left_child = null;
	protected BinaryTree right_child = null;

	private int num_nodes = 0;
	private int depth = 0;
	private boolean is_leaf = false;
	private boolean is_root = false;

	// METODOS PUBLICOS =======================================
	public BinaryTree(boolean r) { // true if you are creating tree from scratch
		is_root = r;
	}

	public void FullInitalization(int profundidad) {
		if (profundidad > 0) {
			int index = 0;
			this.num_nodes = (int) Math.pow(2, profundidad) - 1;
			this.depth = profundidad;

			// IS LEAF/TERMINAL
			if (profundidad == 1) {
				is_leaf = true;
				index = rand.nextInt(leaf_terminals.length);
				this.root = leaf_terminals[index];

				return;
			}

			// IS FUNCTION/TERMINAL
			index = rand.nextInt(node_functions.length);
			this.root = node_functions[index];

			// CREATE LEFT CHILD & RIGHT CHILD
			left_child = new BinaryTree(false); // isn't root
			right_child = new BinaryTree(false); // isn't root
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
				index = rand.nextInt(leaf_terminals.length);
				this.root = leaf_terminals[index];

				return;
			}

			// IS FUNCTION/TERMINAL
			index = rand.nextInt(node_functions.length);
			this.root = node_functions[index];

			// CALCULATE DEPTHS:
			// one of the 2 sides must have depth == [profundidad - 1]
			// the other one doesn't matter ?
			int left_depth = profundidad - 1;
			int right_depth = profundidad - 1;
			// choose side
			boolean select_left = rand.nextBoolean();
			if (select_left)
				left_depth = 1 + (int) (rand.nextDouble() * (profundidad - 2)); // [1, profundiad - 1]
			else
				right_depth = 1 + (int) (rand.nextDouble() * (profundidad - 2)); // [1, profundiad - 1]

			// CREATE LEFT CHILD & RIGHT CHILD
			left_child = new BinaryTree(false); // isn't root
			right_child = new BinaryTree(false); // isn't root
			left_child.FullInitalization(left_depth);
			right_child.FullInitalization(right_depth);

			// Update num_nodes and max_depth
			this.num_nodes += left_child.num_nodes + right_child.num_nodes;
			this.depth = 1 + Math.max(left_child.depth, right_child.depth);
		}
	}

	// SET===========================================================================
	public void setRoot(String value, boolean r, boolean l) {
		root = value;
		is_root = r;
		is_leaf = l;
	}

	public boolean setLeftChild(BinaryTree left, boolean r, boolean l) {
		if (root != null) {
			left_child = left;
			is_root = r;
			is_leaf = l;
			return true;
		}
		return false;
	}

	public boolean setRightChild(BinaryTree right, boolean r, boolean l) {
		if (root != null) {
			right_child = right;
			is_root = r;
			is_leaf = l;
			return true;
		}
		return false;
	}

	// GET ======================================================================
	// Devuelve el arbol en forma de array
	public String getRoot() {
		return root;
	}
	public int getDepth() {
		return depth;
	}

	public BinaryTree getCopy() {
		BinaryTree aux = new BinaryTree(true);
		aux.getCopyAux(this);

		return aux;
	}

	private void getCopyAux(BinaryTree aux) {
		root = aux.root;
		is_leaf = aux.is_leaf;
		is_root = aux.is_root;
		depth = aux.depth;
		
		if (aux.left_child != null) {
			left_child = new BinaryTree(false);
			left_child.getCopyAux(aux.left_child);
		}
		if (aux.right_child != null) {
			right_child = new BinaryTree(false);
			right_child.getCopyAux(aux.right_child);
		} else if (aux.left_child == null && root != null) {
			is_leaf = true;
		}
	}

	// Returns Array with Tree in INORDEN
	public ArrayList<String> toArray() {
		ArrayList<String> array = new ArrayList<String>();
		inorderTraversal(array, this);
		return array;
	}

	// Aux Method for toArray
	private void inorderTraversal(ArrayList<String> array, BinaryTree tree) {
		if (tree != null) {
			inorderTraversal(array, tree.left_child);
			array.add(tree.root);
			inorderTraversal(array, tree.right_child);
		}
	}

	// Obtener función del arbol
	public Function<Double, Double> getFunction() {
		ArrayList<String> treeArray = toArray();
		Function<Double, Double> result = x -> {
			double res = x;
			if (treeArray.get(0) != "x")
				res = Double.parseDouble(treeArray.get(0));

			for (int i = 1; i < treeArray.size() - 1; i += 2) {
				String current = treeArray.get(i);
				String nextString = treeArray.get(i + 1);
				double nextValue = 0;
				if (nextString == "x")
					nextValue = x;
				else
					nextValue = Double.parseDouble(nextString);

				switch (current) {
				case "add":
					res += nextValue;
					break;
				case "sub":
					res -= nextValue;
					break;
				case "mul":
					res *= nextValue;
					break;
				default:
					throw new IllegalArgumentException("Unknown operator: " + current);
				}
			}
			return res;
		};

		return result;
	}

	// MUTATE
	// =================================================================================
	public static void MutateTerminal(BinaryTree aux) {
		if (aux != null) {
			// IS LEAF
			if (aux.left_child == null && aux.right_child == null) {
				if (aux.is_leaf) {
					int index = (int) (rand.nextDouble() * (leaf_terminals.length - 1));
					aux.root = leaf_terminals[index];
				} else {
					System.out.println("SE ENCONTRÓ Tree con left/right child == NULL && is_leaf == FALSE");
					int index = (int) (rand.nextDouble() * (leaf_terminals.length - 1));
					aux.root = leaf_terminals[index];
				}
			}
			// HAS LEFT AND RIGHT CHILDREN
			else if (aux.left_child != null && aux.right_child != null) {
				double d = rand.nextDouble();
				if (d <= 0.5)
					MutateTerminal(aux.left_child);
				else
					MutateTerminal(aux.right_child);
			}
			// LEFT
			else if (aux.left_child != null)
				MutateTerminal(aux.left_child);
			// RIGHT
			else
				MutateTerminal(aux.right_child);
		}

	}

	public static void MutateFunction(BinaryTree aux, double mutation_chance) {
		if (aux != null) {
			// IS LEAF
			if (aux.left_child == null && aux.right_child == null) {
				// NOTHING======================================0
			} else {
				double d = rand.nextDouble();
				if (d < mutation_chance) {
					int index = rand.nextInt(node_functions.length);;
					aux.root = node_functions[index];
				} else if (aux.left_child != null && aux.right_child != null) {
					d = rand.nextDouble();
					if (d <= 0.5)
						MutateFunction(aux.left_child, mutation_chance);
					else
						MutateFunction(aux.right_child, mutation_chance);
				}
				// LEFT
				else if (aux.left_child != null)
					MutateFunction(aux.left_child, mutation_chance);
				// RIGHT
				else
					MutateFunction(aux.right_child, mutation_chance);
			}
		}
	}

	public static void MutateSubTree(BinaryTree aux, double mutation_chance) {
		if (aux != null) {
			double d = rand.nextDouble();
			// MUTATE
			if (d < mutation_chance) {
				if (aux.left_child == null && aux.right_child == null) {
					aux.is_leaf = false;
					aux.GrowInitalization(2); // 2??????????????? menor que max depth?????????????????
				} else if (aux.left_child != null && aux.right_child != null) {
					d = rand.nextDouble();
					if (d <= 0.5)
						aux.left_child.GrowInitalization(2);
					else
						aux.right_child.GrowInitalization(2);
				} else if (aux.left_child != null)
					aux.left_child.GrowInitalization(2);
				else
					aux.right_child.GrowInitalization(2);
			}
			// DOESN'T MUTATE
			else {
				if (aux.left_child != null && aux.right_child != null) {
					d = rand.nextDouble();
					if (d <= 0.5)
						MutateSubTree(aux.left_child, mutation_chance);
					else
						MutateSubTree(aux.right_child, mutation_chance);
				} else if (aux.left_child != null)
					MutateSubTree(aux.left_child, mutation_chance);
				else if (aux.right_child != null)
					MutateSubTree(aux.right_child, mutation_chance);
			}
		}
	}

	public BinaryTree getRandomNode(double mutation_chance) {
		return GetRandomNodeAux(this, mutation_chance);
	}

	static public BinaryTree GetRandomNodeAux(BinaryTree aux, double mutation_chance) {
		// If tree is null or leaf return null
		if (aux == null || aux.left_child == null && aux.right_child == null)
			return null;

		double d = rand.nextDouble();
		BinaryTree aux2 = null;
		if (d < mutation_chance)
			return aux;
		else if (aux.left_child != null)
			aux2 = GetRandomNodeAux(aux.left_child, mutation_chance);
		if (aux.right_child != null && aux2 != null)
			aux2 = GetRandomNodeAux(aux.right_child, mutation_chance);

		return aux2;
	}

	public void copyTree(BinaryTree aux) {
		if(aux != null) {
			//puede que este arbol tenga padre, por lo que aunque aux sea root, this seguira sin serlo
			root = aux.root; 
			//is_root = aux.is_root;
			
			depth = aux.depth;			
			is_leaf = aux.is_leaf;

			if (aux.left_child != null) {
				left_child = new BinaryTree(false);
				left_child.copyTree(aux.left_child);				
			}
			if (aux.right_child != null) {
				right_child = new BinaryTree(false);
				right_child.copyTree(aux.right_child);
			}
		}
		
	}
	
	public void updateDepth() {
		UpdateDepthAux(this);
	}

	static private int UpdateDepthAux(BinaryTree aux) {
		if(aux == null)
			return 0;
		if(aux.left_child == null && aux.right_child == null) {
			aux.is_leaf = true;
			aux.depth = 1;
			return 1;			
		}
		int left_depth = UpdateDepthAux(aux.left_child);
		int right_depth = UpdateDepthAux(aux.right_child);
		
		aux.depth = 1 + Math.max(left_depth, right_depth);
		
		return aux.depth;			
	}
}
