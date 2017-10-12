import java.io.*;
import java.util.Scanner;

public class BestMatchForAToken {

    /**
     * Calculate global edit distance from given Strings. and find the best match with dict.txt
     * @param s1
     * @param s2
     * @return Global Edit Distance
     */
    public static int distance(String s1, String s2) {
        int edits[][] = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
            edits[i][0] = i;
        for (int j = 1; j <= s2.length(); j++)
            edits[0][j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int u = (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1);
                edits[i][j] = Math.min(
                        edits[i - 1][j] + 1,
                        Math.min(
                                edits[i][j - 1] + 1,
                                edits[i - 1][j - 1] + u
                        )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }

    /**
     * Calculate global edit distance from files,and found best matches.
     */
    public static void compare() {
        try {
                System.out.println("Input a token to find the best match: ");
                Scanner input= new Scanner(System.in);
                String str =input.nextLine();
                BufferedReader br = new BufferedReader(new FileReader("dict.txt"));
                int bestValue = 999999999;
                String bestMatch = "";
                String line = null;
                while ((line = br.readLine()) != null) {
                    int value = distance(str, line);
                    if (value < bestValue) {
                        bestValue = value;
                        bestMatch = line;
                    }
                }
                System.out.println("The best match for token: "+str+" is "+bestMatch+" "+bestValue);
                br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        compare();
    }

}