/*
    Andrea Marquez Tavera
    P2
*/

import java.util.*;

public class dfamin {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder out = new StringBuilder(); // StringBuilder to output all together

        int DFAIdx = scanner.nextInt();

        for (int idx1 = 1; idx1 <= DFAIdx; idx1++) {
            int states = scanner.nextInt();
            int S = scanner.nextInt();
            int NA = scanner.nextInt();

            boolean[] accept = new boolean[states];
            for (int i = 0; i < NA; i++) {
                accept[scanner.nextInt()] = true;
            }

            int[][] transitions = new int[states][S];
            for (int i = 0; i < states; i++) {
                for (int j = 0; j < S; j++) {
                    transitions[i][j] = scanner.nextInt();
                }
            }

            DFA dfa = new DFA(states, S, accept, transitions);
            DFA minDFA = min(dfa);

            out.append("DFA #").append(idx1).append(":\n");
            out.append(minDFA.states).append(" ").append(minDFA.S).append("\n");
            int count = 0;
            for (boolean accepted : minDFA.accept) {
                if (accepted) count++;
            }
            out.append(count);
            for (int i = 0; i < minDFA.states; i++) {
                if (minDFA.accept[i]) {
                    out.append(" ").append(i);
                }
            }
            out.append("\n");
            for (int i = 0; i < minDFA.states; i++) {
                for (int j = 0; j < minDFA.S; j++) {
                    out.append(minDFA.transitions[i][j]).append(" ");
                }
                out.append("\n");
            }
            out.append("\n"); 
        }

        scanner.close();
        System.out.print(out.toString());
    }

    static class DFA {
        int states;
        int S;
        boolean[] accept;
        int[][] transitions;

        // will be used to categorize each DFA
        public DFA(int states, int S, boolean[] accept, int[][] transitions) {
            this.states = states;
            this.S = S;
            this.accept = accept;
            this.transitions = transitions;
        }
    }

    private static boolean[] reach(DFA dfa) {
        boolean[] reachable = new boolean[dfa.states];
        // "Any graph search algorithm is fine"
        Queue<Integer> queue = new LinkedList<>();
        // starts at 0
        queue.add(0);
        reachable[0] = true;

        // while there are still more
        while (!queue.isEmpty()) {
            // retrieve / remove head of the queue
            int head = queue.poll();
            for (int i = 0; i < dfa.S; i++) {
                int next = dfa.transitions[head][i];
                if (!reachable[next]) {
                    reachable[next] = true;
                    queue.add(next);
                }
            }
        }
        return reachable;
    }

    private static int amountIdx(List<List<Integer>> amount, int state) {
        for (int i = 0; i < amount.size(); i++) {
            if (amount.get(i).contains(state)) {
                return i;
            }
        }
        return -1;
    }
    
    private static DFA min(DFA dfa) {
        boolean[] reachable = reach(dfa);

        List<List<Integer>> amount = new ArrayList<>();
        List<Integer> AP = new ArrayList<>();
        List<Integer> nonAP = new ArrayList<>();

        for (int i = 0; i < dfa.states; i++) {
            if (reachable[i]) {
                if (dfa.accept[i]) {
                    AP.add(i);
                } else {
                    nonAP.add(i);
                }
            }
        }

        amount.add(AP);
        amount.add(nonAP);

        boolean sub = true;
        while (sub) {
            sub = false;
            List<List<Integer>> NA = new ArrayList<>();

            for (List<Integer> a : amount) {
                // any graph search alogirthm is fine
                Map<List<Integer>, List<Integer>> group = new HashMap<>();

                for (int state : a) {
                    List<Integer> groupByT = new ArrayList<>();
                    for (int i = 0; i < dfa.S; i++) {
                        int next = dfa.transitions[state][i];
                        int aIdx = amountIdx(amount, next);
                        groupByT.add(aIdx);
                    }
                    
                    List<Integer> list = group.get(groupByT);
                    if (list == null) { 
                        list = new ArrayList<>();
                        group.put(groupByT, list);
                    }
                    list.add(state); 
                }
                
                NA.addAll(group.values());
                if (group.size() > 1) {
                    sub = true;
                }
            }
            amount = NA;
        }

        int[] statesM = new int[dfa.states];
        Arrays.fill(statesM, -1);
        int currentState = 0;

        amount.sort(Comparator.comparingInt(a -> a.get(0)));

        for (List<Integer> a : amount) {
            for (int state : a) {
                statesM[state] = currentState;
            }
            currentState++;
        }

        int nS = amount.size();
        int[][] nTransitions = new int[nS][dfa.S];
        boolean[] nAccept = new boolean[nS];

        for (int old = 0; old < dfa.states; old++) {
            if (statesM[old] != -1) {
                int nState = statesM[old];
                for (int i = 0; i < dfa.S; i++) {
                    nTransitions[nState][i] = statesM[dfa.transitions[old][i]];
                }
                nAccept[nState] = dfa.accept[old];
            }
        }
        // returns minimized DFA
        return new DFA(nS, dfa.S, nAccept, nTransitions);
    }
}