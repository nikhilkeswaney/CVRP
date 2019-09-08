import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CVRP {


    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            usage();
            return;
        }

        String dataSet = args[0];
        readFile(dataSet);
    }

    public static void readFile(String dataSet) throws IOException {
        String currentLine;
        BufferedReader objReader = new BufferedReader(new FileReader(dataSet));
        while ((currentLine = objReader.readLine()) != null){
            String[] currentLineSeperated = currentLine.split(":");
            String tag = currentLineSeperated[0].strip();
            switch (tag){
                case "COMMENT":{

                }
            }
        }

    }

    private static void usage() {
        System.out.println("Usage: java CVRP <dataset>");
        System.out.println("1. <dataset> The dataset you want to use");
    }
}
