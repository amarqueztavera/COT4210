/*
Andrea Marquez Tavera

COT 4210 Program #5: How Many Strings are Accepted?
*/

import java.util.*;

public class dfacount {
    static final long LENGTH = 1000000007;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // number of DFAs in the input (n <= 25)
        int n = in.nextInt();

        // for final result
        List<Long> result = new ArrayList<>();

        for (int idx = 0; idx < n; idx++) {
            // Read DFA description
            int s = in.nextInt();// number of states, 0 to s-1
            int v = in.nextInt();// size of the input alphabet
            int k = in.nextInt();// length of strings to consider
            int a = in.nextInt();// Read accept states

            //Set<Integer> acceptStates = new HashSet<>();
            boolean[] confirmed = new boolean[s];
            for (int i = 0; i < a; i++) {
                int acceptState = in.nextInt();
               // acceptStates.add(sc.nextInt());
                confirmed[acceptState] = true;
            }

            int[][] transition = new int[s][v];
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < v; j++) {
                    transition[i][j] = in.nextInt();
                }
            }

            // from hint 1, use a 2d array to track the num of strings of length x ending in state y 
            long[][] arr = new long[k + 1][s];
            arr[0][0] = 1; // in case of empty string
            for (int len = 0; len < k; len++) {
                for (int state = 0; state < s; state++) {
                    if (arr[len][state] > 0) {
                        for (int letter = 0; letter < v; letter++) {
                            int nextState = transition[state][letter];
                            arr[len + 1][nextState] = (arr[len + 1][nextState] + arr[len][state]) % LENGTH;
                        }
                    }
                }
            }

            // calculate total number of strings of length k that are accepted by the DFA
            long total = 0;
            for (int s_idx = 0; s_idx < s; s_idx++) {
                if (confirmed[s_idx]) {
                    total = (total + arr[k][s_idx]) % LENGTH;
                }
            }
            result.add(total);
        }
        in.close();

        // Print all results at the very end
        for (long r : result) {
            System.out.println(r);
        }
    }
}

/* 
 * thank you for a great semester :D
 */