package cz.cvut.fel.omo.model.simulation.report.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileWriterUtil {
    /**
     * This method creates a new txt file or replaces an older one.
     * @param subfolder the subfolder within the output folder to put the file into.
     * @param filename the name of the file (without a file extension)
     * @param content the contents to put into the file
     */
    public static void writeFile(String subfolder, String filename, String content) {
        try {
            // create a dir path
            String dirPath = "output/"+subfolder;
            // create the actual directories, if they don't exist already
            Files.createDirectories(Paths.get(dirPath));

            // create a file path
            String filePath = dirPath+"/"+filename+".txt";

            //new File("output/"+subfolder).mkdirs();
            java.io.FileWriter myObj = new java.io.FileWriter(filePath);
            myObj.write(content);
            myObj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
