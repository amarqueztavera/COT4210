import java.util.*;
import java.io.*;

public class dfa {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	StringBuilder output = new StringBuilder();// for output at the end of run
    	
    	int n = scanner.nextInt(); // Number of DFAs
		for(int i = 1; i <= n; i++) {
			int s = scanner.nextInt(); // Number of states
            int v = scanner.nextInt(); // Size of alphabet
            int m = scanner.nextInt(); // Number of strings to test for membership
            
            int a = scanner.nextInt(); // Number of accept states
            boolean [] accept = new boolean[s];
            for (int aidx = 0; aidx < a; aidx++) {
                int states = scanner.nextInt();
                accept[states] = true;
            }
            int[][] transitions = new int[s][v]; // Alphabet v goes to states s respectively (this was fun to sketch)
            for (int tidx = 0; tidx < s; tidx++) {
                for (int tjdx = 0; tjdx < v; tjdx++) {
                    transitions[tidx][tjdx] = scanner.nextInt();
                }
            }
            
            // Output starts, will append till end of input (out of first for loop)
            output.append("DFA #").append(i).append(":\n");
            
            for (int midx = 0; midx < m; midx++) { // Test acceptance per string
                String test = scanner.next();
                int position = 0; // Start at q0
                
                
                //Process each character of the string using charAt()
                for (int testidx = 0; testidx < test.length(); testidx++) { // Goes through each character
                    char c = test.charAt(testidx);
                    int map = c - 'a';// originally did switch statement, changed to eliminate class
                    position = transitions[position][map];// get q of each character
                }
                
                if (accept[position]) { // if we ended in an accept state, m is in L
                    output.append(test).append(" is in L\n");
                } else { // otherwise not in L
                    output.append(test).append(" is not in L\n");
                }
            }
            output.append("\n");
        }
		
        System.out.print(output.toString());// print output
        scanner.close();
        
    }
}
