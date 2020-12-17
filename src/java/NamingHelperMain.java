import java.io.*;
import java.util.*;

public class NamingHelperMain {
  
  private static List<FullName> fullNamesList = new ArrayList<>();
  private static List<String> fileNames = new ArrayList<>();
  
  public static void main(String[] args) {
    // create List of names from namesList.csv
    List<Name> namesList = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("../resources/namesList.csv")))) {
      String line = "";
      String csvBreakBy = ",";
      int count = 0;
      
      while ((line = bufferedReader.readLine()) != null && count < 50) {
        String[] nameProperties = line.split(csvBreakBy);
        
        while (!("true".equals(nameProperties[3]) || "false".equals(nameProperties[3]))) {
          nameProperties[2] = nameProperties[2] + ", " + nameProperties[3];
          int index = 4;
          while (index < nameProperties.length) {
            nameProperties[index-1] = nameProperties[index];
            index++;
          }
        }
        
        boolean onlyLast = false;
        if ("true".equals(nameProperties[3])) {
          onlyLast = true;
        }
        namesList.add(new Name(nameProperties[0], nameProperties[1], nameProperties[2], onlyLast));
        count++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    int numNames = 0;
           
    // 3 name combinations
    System.out.println("Beginning 3 name combinations...");
    for (int i = 0; i < namesList.size(); i++) {
      Name firstName = namesList.get(i);
      if (!firstName.onlyLast) {
        for (int j = 0; j < namesList.size(); j++) {
          if (i != j) {
            Name middleName = namesList.get(j);
            if (!middleName.onlyLast) {
              for (int k = 0; k < namesList.size(); k++) {
                if (i != k && j != k) {
                  Name lastName = namesList.get(k);
                  fullNamesList.add(new FullName(firstName, middleName, lastName));
                  numNames++;
                }
              }
            }
          }
        }
      }
    }
    fillOutputFile("output3.txt");
    
    // 4 name combinations
    System.out.println("Done with 3, beginning 4 name combinations...");
    int num = 1;
    int fileNum = 0;
    for (int i = 0; i < namesList.size(); i++) {
      Name firstName = namesList.get(i);
      if (!firstName.onlyLast) {
        for (int j = 0; j < namesList.size(); j++) {
          if (i != j) {
            Name middleName1 = namesList.get(j);
            if (!middleName1.onlyLast) {
              for (int k = 0; k < namesList.size(); k++) {
                if (i != k && j != k) {
                  Name middleName2 = namesList.get(k);
                  if (!middleName2.onlyLast) {
                    for (int l = 0; l < namesList.size(); l++) {
                      if (i != l && j != l && k != l) {
                        Name lastName = namesList.get(k);
                        fullNamesList.add(new FullName(firstName, middleName1, middleName2, lastName));
                        if ((num % 1000000) == 0) {
                          fillOutputFile("output4-" + fileNum + ".txt");
                          fileNum++;
                        }
                        num++;
                        numNames++;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    fillOutputFile("output4-" + fileNum + ".txt");
    System.out.println("Made " + numNames + " names in " + fileNames.size() + " files.");
    
    mergeFilesIntoOne(numFiles);
  }
  
  private static void fillOutputFile(String outputFileName) {
    Collections.shuffle(fullNamesList);
            
    File output = new File("../resources/" + outputFileName);
    try {
      if (output.exists()) {
        output.delete();
      }
      output.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
         
    try (FileWriter fileWriter = new FileWriter(output)) {
      for (FullName fullName : fullNamesList) {
        fileWriter.write(fullName.toString() + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    fullNamesList.clear();
    System.out.println(outputFileName + " completed!");
  }
  
  private static void mergeFilesIntoOne(int numFiles) {
    try {
      List<BufferedReader> bufferedReaders = new ArrayList<>();
      for (String fileName : fileNames) {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("../resources/" + fileName)));
      }
      
      File output = new File("../resources/FullOutput.txt");
      if (output.exists()) {
        output.delete();
      }
      output.createNewFile();
      FileWriter fileWriter = new FileWriter(output);
      
      int numNulls = 0;
      while (numNulls < numFiles) {
        numNulls = 0;
        for (BufferedReader br : bufferedReaders) {
          String line = br.readLine();
          if (line != null) {
            fileWriter.write(line + "\n");
          } else {
            numNulls++;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
