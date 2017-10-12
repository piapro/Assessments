import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeValueProcessor {
    private FileWriter fw;
    private BufferedReader br;

    /**
     * Compute the total weighted value in a given tweet
     */
    public static void AttributeValueProcessor() {

        try {
            //FileWriter fw = new FileWriter(new File("ProcessedTweetForTrain.txt"), false);
            //FileWriter fw = new FileWriter(new File("ProcessedTweetForDev.txt"), false);
            FileWriter fw = new FileWriter(new File("ProcessedTweetForTest.txt"), false);
            //BufferedReader br = new BufferedReader(new FileReader("train.txt"));// tweet-id TAB class TAB text-of-tweet
            //BufferedReader br = new BufferedReader(new FileReader("dev.txt"));
            BufferedReader br = new BufferedReader(new FileReader("test.txt"));


            System.out.println("Processing...");
            String line = null;
            while ((line = br.readLine()) != null) {
            String [] lineTweet = line.split("\t");
            String tweet = lineTweet[lineTweet.length - 1];
            String [] token = tweet.split(" ");
            String id = lineTweet[0];
            int count =0;
            for(int i = 0 ; i < token.length - 1 ; i++){

                String temp = token[i];
                if(temp.equalsIgnoreCase("cause")||temp.equalsIgnoreCase("caused")||
                        temp.equalsIgnoreCase("causing")||
                        temp.equalsIgnoreCase("gain")||temp.equalsIgnoreCase("gained")||
                        temp.equalsIgnoreCase("bad")||temp.equalsIgnoreCase("but")||temp.equalsIgnoreCase("bad")){
                    count = count +1;
                }
                if(temp.equalsIgnoreCase("panic")||temp.equalsIgnoreCase("stone")||temp.equalsIgnoreCase("stones")||
                        temp.equalsIgnoreCase("weight")||temp.equalsIgnoreCase("sleep")) {
                    count = count + 2;
                }
                if(temp.equalsIgnoreCase("pain")||temp.equalsIgnoreCase("pains")
                        ||temp.equalsIgnoreCase("withdraw")||temp.equalsIgnoreCase("withdraws")||
                        temp.equalsIgnoreCase("pains")){
                    count = count +3;
                }
                if(temp.equalsIgnoreCase("insomnia")||temp.equalsIgnoreCase("addict")
                        ||temp.equalsIgnoreCase("allergic")||temp.equalsIgnoreCase("zombie")||
                        temp.equalsIgnoreCase("headaches")||temp.equalsIgnoreCase("headache")){
                    count = count +5;
                }

            }
                System.out.println(id + " : " +count);
                fw.write( count + "\n");

            }
            br.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Match the pre-defined pattern via Regex
     */

    public static void regexMatchAttribute(){
        String regex = "gain*[0-9a-zAz ]+weight";
        String regexReversed ="weight*[0-9a-zAz ]+gain";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Pattern patternReversed = Pattern.compile(regexReversed,Pattern.CASE_INSENSITIVE);

    try {
        //FileWriter fw1 = new FileWriter(new File("ProcessedTweetForTrain2.txt"), false);
        //FileWriter fw1 = new FileWriter(new File("ProcessedTweetForDev2.txt"), false);
        FileWriter fw1 = new FileWriter(new File("ProcessedTweetForTest2.txt"), false);
        //BufferedReader br1 = new BufferedReader(new FileReader("train.txt"));// tweet-id TAB class TAB text-of-tweet
        //BufferedReader br1 = new BufferedReader(new FileReader("dev.txt"));
        BufferedReader br1 = new BufferedReader(new FileReader("test.txt"));

        System.out.println("Processing...");
        String line = null;
        while ((line = br1.readLine()) != null) {
            String[] lineTweet = line.split("\t");
            String tweet = lineTweet[lineTweet.length - 1];
            Matcher matcher = pattern.matcher(tweet);
            boolean res = matcher.find();

            Matcher matcherReversed =  patternReversed.matcher(tweet);
            boolean resRev = matcherReversed.find();

            System.out.println(res +" "+ resRev);
            if(res==false&&resRev==false){
                fw1.write("0\n");
            }else{
                fw1.write("1\n");
            }


        }
        fw1.close();
    }catch(IOException e){
            e.printStackTrace();
        }


    }

    public static void main(String args[]){
        AttributeValueProcessor();
        regexMatchAttribute();
    }

}
