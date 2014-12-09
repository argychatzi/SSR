package com.kth.ssr.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by georgios.savvidis on 03/11/14.
 */
public class FileUtils {
    public static final byte[] toByteArray(File f){

        FileInputStream inputStream = null;
        byte fileContent[] = null;

        try {

            inputStream = new FileInputStream(f);
            fileContent = new byte[(int)f.length()];

            inputStream.read(fileContent);

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);

        } catch (IOException e) {
            System.out.println("Exception while reading file " + e);

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error while closing stream: " + e);
            }
        }

        return fileContent;
    }
}
