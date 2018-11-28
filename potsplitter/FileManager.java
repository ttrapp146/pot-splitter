package com.ttrapp14622.potsplitter;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FileManager {

    //saves, loads, and checks if potsplit save file exists
    static final String LIST_FILE_NAME = "SPLITLIST";


    public static boolean check(Context context) {
        if (context.getFileStreamPath(LIST_FILE_NAME).exists())
            return true;
        else
            return false;
    }


    public static void save(ArrayList<String> arrayList, Context context) {
        int i = 0;
        String stringList = "";
        FileOutputStream fos = null;

        while (i < arrayList.size()) {
            stringList = stringList.concat(arrayList.get(i) + "\n");
            i++;
        }

        try {
            fos = context.openFileOutput(LIST_FILE_NAME, MODE_PRIVATE);
            fos.write(stringList.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static ArrayList<String> load(Context context) {
        FileInputStream fis = null;
        ArrayList<String> potList = new ArrayList<>();

        try {
            fis = context.openFileInput(LIST_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String stringList;

            while ((stringList = br.readLine()) != null) {
                sb.append(stringList).append("\n");
            }

            stringList = sb.toString();

            int index;

            while (stringList.contains("\n")) {
                index = stringList.indexOf("\n");
                potList.add(stringList.substring(0, index));
                stringList = stringList.substring(index + 1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return potList;
    }
}
