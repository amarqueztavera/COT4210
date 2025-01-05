// Andrea Marquez Tavera
import java.io.*;
import java.util.*;

public class chomsky {
    public static void main(String[] args) throws IOException {
        Scanner stdin = new Scanner(System.in);
        int amount = stdin.nextInt();
        StringBuilder output = new StringBuilder();
        // based on Guha's post, NOT following pdf specification of using input file 

        // loop for as many grammars
        for (int i = 1; i <= amount; i++) {
            output.append("Grammar #").append(i).append(":\n");

            int var = stdin.nextInt();
            Map<String, List<String>> GrammarRules = new HashMap<>();

            // loop for as many variables
            for (int j = 0; j < var; j++) {
                int rules = stdin.nextInt();
                String variable = stdin.next();
                GrammarRules.put(variable, new ArrayList<>());
                // loop for as many grammar rules
                for (int k = 0; k < rules; k++) {
                    GrammarRules.get(variable).add(stdin.next());
                }
            }
            int stringNum = stdin.nextInt();

            // loop as many strings to test
            for (int j = 0; j < stringNum; j++) {
                String test = stdin.next();
                boolean result = fromGrammar(test, GrammarRules);
                output.append(test).append(": ").append(result ? "YES" : "NO").append("\n");
            }
            output.append("\n");
        }
        System.out.print(output.toString());// print output
        stdin.close();
    }

    // can string be derived from grammar? Using cyk - told in class we could use
    private static boolean fromGrammar(String str, Map<String, List<String>> GrammarRules) {
        int num = str.length();
        if (num == 0) {// aka empty
            return GrammarRules.containsKey("S") && GrammarRules.get("S").contains("@");
        }

        ArrayList<ArrayList<Set<String>>> overall = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            overall.add(new ArrayList<>());
            for (int j = 0; j < num; j++) {
                overall.get(i).add(new HashSet<>());
            }
        }

        for (int i = 0; i < num; i++) {
            String prod = String.valueOf(str.charAt(i));
            for (Map.Entry<String, List<String>> entry : GrammarRules.entrySet()) {
                if (entry.getValue().contains(prod)) {
                    overall.get(i).get(i).add(entry.getKey());
                }
            }
        }

        for (int length = 2; length <= num; length++) {
            for (int i = 0; i <= num - length; i++) {
                int j = i + length - 1;
                for (int k = i; k < j; k++) {
                    for (String A : GrammarRules.keySet()) {
                        for (String production : GrammarRules.get(A)) {
                            if (production.length() == 2) {
                                String B = production.substring(0, 1);
                                String C = production.substring(1, 2);
                                if (overall.get(i).get(k).contains(B) && overall.get(k+1).get(j).contains(C)) {
                                    overall.get(i).get(j).add(A);
                                }
                            }
                        }
                    }
                }
            }
        }
        return overall.get(0).get(num-1).contains("S");
    }
}