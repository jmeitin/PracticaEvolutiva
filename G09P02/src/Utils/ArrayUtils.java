package Utils;

import java.util.ArrayList;

public class ArrayUtils {
	public static int indexOf(int[] array, int element) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element) {
				return i;
			}
		}
		// Si el elemento no se encuentra en el array, devuelve -1
		return -1;
	}

	public static String arrayToMathExpression(ArrayList<String> arr) {
		ArrayList<String> result;
		result = tokensToMathExpression(arr);
		result = preProcessMathExpressionTokens(result);

		return String.join(" ", result);
	}

	private static ArrayList<String> combineMultiplicationTerms(ArrayList<String> tokens) {
	    ArrayList<String> result = new ArrayList<>();
	    final int lastIndex = tokens.size() -1;
	    
	    for (int i = 0; i < tokens.size(); i++) {
	        String current = tokens.get(i);
	        String next = i != lastIndex ? tokens.get(i + 1) : "";

	        while (next.contains("*")) {
	            current += next;
	            i++;
	            if (i + 1 < tokens.size()) {
	                next = tokens.get(i + 1);
	            } else {
	                break;
	            }
	        }

	        result.add(current);
	    }
	    
	    return result;
	}


	private static ArrayList<String> preProcessMathExpressionTokens(ArrayList<String> tokens) {
		ArrayList<String> expressionTokens = new ArrayList<>();
		expressionTokens.add(tokens.get(0));

		// Agrupar funciones y terminales
		for (int i = 1; i < tokens.size(); i += 2) {
			String first = tokens.get(i);
			String second = tokens.get(i + 1);
			String union = first + second;

			if (second.length() == 2) // es -1 o -2
			{
				if (first == "+")
					expressionTokens.add(second); // omito el +
				else if (first == "-")
					expressionTokens.add(second.replace('-', '+')); // el - se convierte en +
				else // es "*"
					expressionTokens.add(first + second); // el - se convierte en +
			} else if (!(union.equals("-0") || union.equals("+0")))
				expressionTokens.add(first + second);
		}

		ArrayList<String> mulCombined = combineMultiplicationTerms(expressionTokens);
		expressionTokens = new ArrayList<>();
		
		// Eliminar términos que sean 0 * algo
		for(String str : mulCombined)
		{
			if(!str.contains("0*"))
			{
				// Eliminar *1, falta ver que hacer si es *-1, porque habria que invertir la expresion
				expressionTokens.add(str.replace("*1", ""));
			}
		}
		
		return expressionTokens;
	}

	private static ArrayList<String> tokensToMathExpression(ArrayList<String> tokens) {
		ArrayList<String> expressionTokens = new ArrayList<>();

		for (String token : tokens) {
			switch (token) {
			case "add":
				expressionTokens.add("+");
				break;
			case "sub":
				expressionTokens.add("-");
				break;
			case "mul":
				expressionTokens.add("*");
				break;
			default:
				expressionTokens.add(token);
				break;
			}
		}

		return expressionTokens;
	}

}
