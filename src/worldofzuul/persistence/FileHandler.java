package worldofzuul.persistence;

import interfaces.CsvParsable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import worldofzuul.Highscore;

public class FileHandler implements CsvParsable {

    String path;

    public FileHandler(String path) {
        this.path = path;
    }

    public ArrayList<Highscore> loadScores() {
        ArrayList<Highscore> scores;
        scores = new ArrayList<>();

        for (String line : this.getCsvList()) {
            String[] lineArr = line.split(",");
            scores.add(new Highscore(Integer.valueOf(lineArr[0]), lineArr[1]));
        }
        
        return scores;
    }

    public void saveScores(ArrayList<Highscore> scores) {

        ArrayList<String> strings;
        strings = new ArrayList<>();

        for (Highscore hs : scores) {
            strings.add(hs.getScore() + "," + hs.getName());
        }

        writeCsvList(strings);

    }

    public ArrayList<String> getCsvList() {

        ArrayList<String> strings;
        strings = null;

        try {
            Scanner sc = new Scanner(new FileReader(path));
            strings = new ArrayList<>();

            while (sc.hasNextLine()) {
                strings.add(sc.nextLine());
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Error reading file!");
            System.out.println(ex.getMessage());
        }

        return strings;
    }

    public void writeCsvList(ArrayList<String> strings) {
        StringBuilder sb = new StringBuilder();
        PrintWriter pw;
        pw = null;

        try {
            pw = new PrintWriter(new File(path));
            for (String string : strings) {
                sb.append(string);
                sb.append("\n");
            }

            pw.write(sb.toString());

        } catch (IOException ex) {
            System.out.println("Error reading file!");
            System.out.println(ex.getMessage());
        } finally {
            pw.close();
        }
    }
}
