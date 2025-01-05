// Andrea Marquez Tavera
import java.util.*;

public class tm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder output = new StringBuilder();

        int n = scanner.nextInt();// number of turing machines
        scanner.nextLine();

        for (int k = 1; k <= n; k++) {
            int q = scanner.nextInt();// number of states 
            int r = scanner.nextInt();// number of rules for the machine, 0 to q-1
            scanner.nextLine();
            // 0 - START
            // 1 - ACCEPT
            // 2 - REJECT

            Map<String, Rule> rules = new HashMap<>();// storing r rules on list
            for (int i = 0; i < r; i++) {
                int inputState = scanner.nextInt();
                char readChar = scanner.next().charAt(0);
                int outputState = scanner.nextInt();
                char writeChar = scanner.next().charAt(0);
                char dir = scanner.next().charAt(0);
                scanner.nextLine();
                
                String key = inputState + " " + readChar;// {a, b}
                rules.put(key, new Rule(inputState, readChar, outputState, writeChar, dir));
            }

            int n2 = scanner.nextInt();// number of input strings to test this TM
            int s = scanner.nextInt();// max num of steps to run each simulation
            scanner.nextLine();

            //System.out.println("Machine #" + k + ":");
            output.append("Machine #").append(k).append(":\n");
            for (int i = 0; i < n2; i++) {
                String inputString = scanner.nextLine();
                String result = simulateTuringMachine(inputString, q, rules, s);
                //System.out.println(inputString + ": " + result);
                output.append(inputString).append(": ").append(result).append("\n");
            }
            //System.out.println();
            output.append("\n");
        } 
        System.out.print(output.toString());// print output
        scanner.close();
    }

    static class Rule {
        int inputState;
        char readChar;
        int outputState;
        char writeChar;
        char dir;

        Rule(int inputState, char readChar, int outputState, char writeChar, char dir) {
            this.inputState = inputState;
            this.readChar = readChar;
            this.outputState = outputState;
            this.writeChar = writeChar;
            this.dir = dir;
        }
    }

    private static String simulateTuringMachine(String inputString, int q, Map<String, Rule> rules, int s) {
        List<Character> tape = new ArrayList<>();// simulate a tape 
        for (char c : inputString.toCharArray()) {// run input string on TM by each character
            tape.add(c);
        }
        int head = 0;
        int state = 0;// start
        int steps = 0;

        while (steps < s) {
            // replace with blank
            char readChar;
            if (head < tape.size()) {
                readChar = tape.get(head);
            } else { 
                readChar = 'B';
            }
            String key = state + " " + readChar;

            if (!rules.containsKey(key)) {
                return "NO";
            }

            Rule rule = rules.get(key);
            state = rule.outputState;
            if (head < tape.size()) {
                tape.set(head, rule.writeChar);
            } else {// if ran out of tape, use blanks
                tape.add('B');
                tape.set(head, rule.writeChar);
            }

            if (rule.dir == 'L') {
                if (head > 0) {
                    head--;
                } else {
                    head = 0;
                }
            } else {
                head++;
            }

            steps++;
            if (state == 1) return "YES"; // Accepted
            if (state == 2) return "NO";  // Rejected
        }
        return "DOES NOT HALT IN " + s + " STEPS";
    }
}