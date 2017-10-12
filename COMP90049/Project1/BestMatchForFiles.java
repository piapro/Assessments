/**
 * @Author: Aoheng MA 888788 The University of Melbourne
 * @Verison: 1.0.0
 */

import java.io.*;
import java.util.ArrayList;

public class BestMatchForFiles {

    /**
     * Split tweets into tokens
     */
    public static void splitTweets() {
        try {
            FileWriter fw = new FileWriter(new File("ProcessedTweets.txt"), false);
            BufferedReader br = new BufferedReader(new FileReader("labelled-tweets.txt"));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] subStrTweets = line.split(" ");
                for (int i = 0; i <= subStrTweets.length - 1; i++) {
                    if (subStrTweets[i].equals("!") || subStrTweets[i].equals(":") || subStrTweets[i].equals("?") ||
                            subStrTweets[i].equals(".") || subStrTweets[i].equals("...") || subStrTweets[i].equals("(") ||
                            subStrTweets[i].equals(")") || subStrTweets[i].equals("=") || subStrTweets[i].equals("<") ||
                            subStrTweets[i].equals("_") || subStrTweets[i].equals(">") || subStrTweets[i].equals(",") ||
                            subStrTweets[i].equals("\"")) {
                        ;
                    } else {
                        fw.write(subStrTweets[i] + "\n");
                    }
                }

            }
            br.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate global edit editDistance from given Strings. and find the best match with dict.txt
     * @param s1
     * @param s2
     * @return Global Edit Distance
     */
    public static int editDistance(String s1, String s2) {
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
     * Calculate global edit editDistance from files,and found best matches.
     */
    public static void compareEditDistance() {
        try {
            FileWriter fw = new FileWriter(new File("editCompareResult.txt"), false);
            BufferedReader br1 = new BufferedReader(new FileReader("ProcessedTweets.txt"));
            String lineToken = null;
            System.out.println("<Token_in_Twitter> <Match_in_Dictionary> <Edit_Distance>");
            while ((lineToken = br1.readLine()) != null) {
                String lineDic = null;
                int best_val = 999999999;
                BufferedReader br2 = new BufferedReader(new FileReader("dict.txt"));

                String best_str = "";
                while ((lineDic = br2.readLine()) != null) {
                    int val = editDistance(lineToken, lineDic);
                    if (val < best_val) {
                        best_val = val;
                        best_str = lineDic;
                    }
                }
                System.out.println(best_val);
                fw.write(lineToken + " " + best_str + " " + best_val + "\n");
                br2.close();
            }
            br1.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To find all match for Global Edit Distance
     */
    public static void editCounter(){
        try {
            int attemptCounter = 0;
            int tokenCount = 0;
            int accCount = 0;
            ArrayList list;
            FileWriter fw = new FileWriter(new File("editResult.txt"), false);
            BufferedReader br1 = new BufferedReader(new FileReader("editCompareResult.txt"));
            String lineToken = null;
            while ((lineToken = br1.readLine()) != null) {
                list = new ArrayList();
                String [] temp = lineToken.split(" ");
                String token = temp[0];
                String edit = temp[2];
                int editVal = Integer.parseInt(edit);
                String lineDic = null;
                //System.out.println(editVal);
                int best_val = editVal;

                BufferedReader br2 = new BufferedReader(new FileReader("dict.txt"));
                //String best_str = "";
                while ((lineDic = br2.readLine()) != null) {
                    tokenCount++;
                    int val = editDistance(token, lineDic);
                    if (val == best_val) {
                        list.add(lineDic);
                        System.out.println(lineDic);
                        attemptCounter++;
                    }
                }

                fw.write(lineToken + " ");
                for (int i = 0; i < list.size(); i++) {
                    System.out.print(list.get(i) + ",");
                    fw.write(list.get(i) + ",");
                }
                System.out.println();
                fw.write("\n");
                br2.close();

            }
            br1.close();
            fw.close();
            System.out.println("Total Matching Attempts:"+attemptCounter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate soundex code from given Strings. and find the best match with dict.txt
     * @param s
     */
    public static String soundex(String s) {
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                case 'F':
                case 'P':
                case 'V':
                    x[i] = '1';
                    break;

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z':
                    x[i] = '2';
                    break;

                case 'D':
                case 'T':
                    x[i] = '3';
                    break;

                case 'L':
                    x[i] = '4';
                    break;

                case 'M':
                case 'N':
                    x[i] = '5';
                    break;

                case 'R':
                    x[i] = '6';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i - 1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    /**
     * Calculate Soundex from files,and found best matches.
     */
    public static void compareSoundex() {

        try {
            int counter = 0;
            FileWriter fw = new FileWriter(new File("soundexCompareResult.txt"), false);
            BufferedReader br1 = new BufferedReader(new FileReader("ProcessedTweets.txt"));
            String lineToken = null;
            System.out.println("<Token_in_Twitter> <Soundex_Token> <Matched?>");
            String matchedSoundex = null;
            while ((lineToken = br1.readLine()) != null) {
                String soundex1 = soundex(lineToken);
                BufferedReader br2 = new BufferedReader(new FileReader("dict.txt"));
                String lineDic = null;
                ArrayList<String> list = new ArrayList<String>();
                while ((lineDic = br2.readLine()) != null) {
                    String soundex2 = soundex(lineDic);
                    if (soundex1.equals(soundex2)) {
                        matchedSoundex = lineDic;
                        list.add(matchedSoundex);
                        counter++;
                    }
                }

                System.out.print(lineToken + " " + soundex1 + " ");
                fw.write(lineToken + " " + soundex1 + " ");
                for (int i = 0; i < list.size(); i++) {

                    System.out.print(list.get(i) + ",");
                    fw.write(list.get(i) + ",");
                }
                System.out.println();
                fw.write("\n");
                br2.close();
            }
            br1.close();
            fw.close();
        System.out.println("Attempts: "+counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Comparing the best match  with it`s canonical form in "labelled-tokens.txt", and Count the correct rate.
     */
    public static void statisticsSoundex() {
        int tokenCount = 0;
        int correctCount = 0;
        try {
            FileWriter fw = new FileWriter(new File("MatchingStatisticsSoundex.txt"), false);
            fw.write("<Token> <Canonical_Form> <label> <MatchSoundex>" + "\n");
            BufferedReader br1 = new BufferedReader(new FileReader("soundexCompareResult.txt")); //comparing each tokens
            String lineResult = null;
            String lable = null;
            String result = null;
            String canoForm = null;
            String matchedSoundex = null;
            while ((lineResult = br1.readLine()) != null) {
                tokenCount++;
                String[] temp1 = lineResult.split(" ");
                //new N000 n,na,naa...
                String token = temp1[0];
                String matchDict = temp1[temp1.length - 1];
                //System.out.println(matchDict);
                String[] splitMatch = matchDict.split(",");
                BufferedReader br2 = new BufferedReader(new FileReader("labelled-tokens.txt"));//compared with canonical form
                String lineToken = null;
                while ((lineToken = br2.readLine()) != null) {
                    String[] temp2 = lineToken.split("\t");
                    //new	IV	new
                    String matchedToken = temp2[0];
                    lable = temp2[1];
                    canoForm = temp2[2];

                    if (token.equals(matchedToken)) {
                        for (int i = 0; i < splitMatch.length; i++) {
                            if (canoForm.equals(splitMatch[i])) {
                                result = "True";
                                correctCount++;
                                matchedSoundex = splitMatch[i];
                                System.out.println(token + " " + splitMatch[i]);
                                break;
                            }else{
                                matchedSoundex = null;
                                result = "False";
                            }
                        }
                        break;
                    }

                }

                fw.write(token + " " + canoForm + " " + lable + " " + result +" "+matchedSoundex+ "\n");
                br2.close();
            }
            br1.close();

            System.out.println("Correct: " + correctCount);
            System.out.println("Total Token: " + tokenCount);
            double rate = (correctCount * 0.1d) / (tokenCount * 0.1d);
            System.out.println("Recall: " + rate);


            fw.write("Correct: " + correctCount + " Total Tokens: " + tokenCount + " Recall: " + rate + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Comparing the best match  with it`s canonical form in "labelled-tokens.txt", and Count the correct rate.
     */
    public static void statisticsEdit() {
        int tokenCount = 0;
        int correctCount = 0;
        try {
            FileWriter fw = new FileWriter(new File("MatchingStatistics.txt"), false);
            fw.write("<Token> <Matches_in_dictionary> <Canonical_Form> <Correct>" + "\n");
            BufferedReader br1 = new BufferedReader(new FileReader("editCompareResult.txt")); //comparing each tokens
            String lineResult = null;
            String result = null;
            String canoForm = null;
            while ((lineResult = br1.readLine()) != null) {
                tokenCount++;
                String[] temp1 = lineResult.split(" ");
                String token = temp1[0];
                String matchDict = temp1[1];
                BufferedReader br2 = new BufferedReader(new FileReader("labelled-tokens.txt"));//compared with canonical form
                String lineToken = null;
                while ((lineToken = br2.readLine()) != null) {
                    String[] temp2 = lineToken.split("\t");
                    String matchedToken = temp2[0];
                    canoForm = temp2[2];

                    if (token.equals(matchedToken)) {
                        if (matchDict.equals(canoForm)) {
                            result = "True";
                            correctCount++;
                        } else {
                            result = "False";
                        }

                        break;
                    }

                }

                fw.write(token + " " + matchDict + " " + canoForm + " " + result + "\n");
                br2.close();
            }
            br1.close();

            System.out.println("Correct: " + correctCount);
            System.out.println("Total Token: " + tokenCount);
            double rate = (correctCount * 0.1d) / (tokenCount * 0.1d);
            System.out.println("Recall: " + rate);


            fw.write("Correct: " + correctCount + " Total Tokens: " + tokenCount + " Recall: " + rate + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args){

        System.out.println("COMP90049 Knowledge Technology Text Processing System");
        System.out.println("Processing...");
        splitTweets();
        compareEditDistance();
        compareSoundex();
        statisticsEdit();
        statisticsSoundex();
        //editCounter();
        System.out.println("Finished !");

    }
}
