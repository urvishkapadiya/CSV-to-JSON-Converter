import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.*;

/**
 * This class shows how given CSV file is converted into JSON file with by using InvalidExceptions class exception
 * @author Urvishkumar Kapadiya
 * @version 0.1
 * @see <a href="https://docs.oracle.com/en/java/javase/14/docs/api/index.html" target="_blank">Java Docs</a>
 */

public class CsvToJson {
    static boolean created = true;
    // public static String path = "D:\\University\\COMP_6481_PPS\\Assignments\\Assignment2\\Ass2\\src\\";
    public static String path = "E:\\Master's\\PPS\\Java_Practice\\Assignment\\Ass2\\src\\";

    /**
     *
     * @param logger logger object to print the logs
     * @param headers list of headers
     * @param fileName fileName which is missing the headers
     * This method prints the exception log in log file
     */

    // method responsible to create missingHeader log
    public static void misssingHeaders(Logger logger, String headers[], String fileName) {

        String attributeString = "";
        int missing = 0;                //counting total missing value

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals("")) {
                attributeString += "***,";
                missing++;
            } else {
                attributeString += headers[i] + ",";
            }
        }
        //adding log in log file
        logger.log(Level.WARNING, "\nFile " + fileName + " is invalid.\nMissing field: " + (headers.length - missing)
                + " detected, " + missing + " missing\n" + attributeString.substring(0, attributeString.length() - 1));
    }

    //method responsible to create missingData log
    /**
     *
     * @param logger     logger object to print the logs
     * @param headers    list of headers
     * @param data       list of values for row
     * @param fileName   fileName which is missing the values in row
     * @param lineNumber linenumber which has missing values
     * This method prints the exception log in log file
     */
    public static void missingData(Logger logger, String headers[], String data[], String fileName, int lineNumber) {

        String dataString = "";
        String missingDataString = "";
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals("")) {
                dataString += "*** ";
                missingDataString += headers[i] + ",";
            } else {
                dataString += data[i] + " ";
            }
        }
        //adding log in log file
        logger.log(Level.WARNING, "\nIn file " + fileName + " line " + lineNumber + "\n" + dataString + "\nMissing: "
                + missingDataString.substring(0, missingDataString.length() - 1));
    }

    //method responsible to process the txtfile and converte it into json file
    /**
     *
     * @param file           File object
     * @param sc             Scanner object to read the input file
     * @param outputFileName name of the new generate output file
     */
    public static void processFilesForValidation(File file, Scanner sc, String outputFileName)
    {
        String str = sc.nextLine();
        String[] s = str.split(",");     //spliting line with semicolon
        PrintWriter jsonPrintWriter=null;
        Logger logger = Logger.getLogger(CsvToJson.class.getName());   //creating logger
        FileHandler fh = null;

        try{
            // This block configure the logger with handler and formatter
            fh = new FileHandler(path + "ExceptionLogs.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            for (int i = 0; i < s.length; i++) {
                if (s[i].equals("")) {
                    // logic to generate log file
                    misssingHeaders(logger, s, file.getName());
                    created=false;
                    throw new CSVFileInvalidException(file.getName());  //throwing exception of invalidCSVFile when missing colum derected
                }
            }

            jsonPrintWriter = new PrintWriter(new File(path + outputFileName));   //creating printwriter to write json file            

        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(CSVFileInvalidException e) {
            System.out.println(e.getMessage());
            return;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        jsonPrintWriter.append("[\n");

        int lineCount = 0;
        while (sc.hasNextLine()) {
            String outputStr = sc.nextLine();                        
            
            String[] columData = outputStr.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            lineCount++;
            boolean check = false;  //to detect missing value
            

            try {
                for (int j = 0; j < columData.length; j++) // checking for missing data
                {
                    //cheking whether any data is missing or not
                    if (columData[j].equals("")) {
                        check = true;
                        missingData(logger, s, columData, file.getName(), lineCount); //creating log entry while missing value detected
                        throw new CSVDataMissing(outputFileName, lineCount);  //throwing where missing value found
                    }
                }
            }
            catch (CSVDataMissing e) {
                System.out.println(e.getMessage());
                continue; //continuing even after encountering exception
            }

            if (!check) {
                //writing row data into json file
                jsonPrintWriter.append("\t{\n\t\t");
                for (int i = 0; i < s.length; i++) {
                    if (i + 1 != s.length){
                        if (isNumeric(columData[i]))
                            jsonPrintWriter.append('"' + s[i] + '"' + " : " + columData[i] + ",\n\t\t");
                        else if (columData[i].startsWith("\""))
                            jsonPrintWriter.append('"' + s[i] + '"' + " : " + columData[i] + ",\n\t\t");                        
                        else
                            jsonPrintWriter.append('"' + s[i] + '"' + " : " + '"' + columData[i] + '"' + ",\n\t\t");
                    }
                    else if (isNumeric(columData[i]))
                        jsonPrintWriter.append('"' + s[i] + '"' + " : " + columData[i] + "\n\t\t");
                    else if (columData[i].startsWith("\""))
                        jsonPrintWriter.append('"' + s[i] + '"' + " : " + columData[i] + "\n\t\t");                    
                    else
                        jsonPrintWriter.append('"' + s[i] + '"' + " : " + '"' + columData[i] + '"' + "\n\t\t");
                }

                if (sc.hasNextLine())
                    jsonPrintWriter.append("},\n");
                else
                    jsonPrintWriter.append("}\n");
            }
        }
        jsonPrintWriter.append("]\n");

        //closing all the open file
        jsonPrintWriter.close();
        sc.close();
    }
    /**
     *
     * @param strNum This is the string to validate
     * @return true if given arg is numeric otherwise false
     */
    public static boolean isNumeric(String strNum) {
        // return strNum.chars().allMatch(Character::isDigit);
        try {
            Double.parseDouble(strNum);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * @param args These are arguments supplied to the command line
     * @throws InvalidException Excption will be thrown when any exceptions encountered
     */
    public static void main(String[] args) throws InvalidException {

        System.out.println("Enter the number of files you wants to convert: ");
        Scanner numberSc = new Scanner(System.in);

        int n = numberSc.nextInt();
        String[] fileName = new String[n];
        String[] outputFileName= new String[n];
        numberSc.nextLine();
        for(int i=0; i<n; i++)
        {
            fileName[i] = numberSc.nextLine();
            outputFileName[i] = fileName[i].substring(0,fileName[i].length()-4)+".json";
        }

        File file = null;
        Scanner sc = null;

        //opening file and giving it to processForValidation
        for (int i = 0; i < fileName.length; i++) {
            try {
                file = new File(path + fileName[i]);
                // if the given file is not exits, throwing Own defined fileNotFoundException
                if (!file.exists()) {

                    throw new FileNotFoundException(file.getName());
                } else {
                    sc = new Scanner(file);
                    processFilesForValidation(file, sc, outputFileName[i]);
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                sc.close();
                if (!created) {// delete logic ,if any of the file not able to create json file, it will delete
                    // all the cerated json file
                    File outputFile = null;
                    for (int j = 0; j < outputFileName.length; j++) {
                        outputFile = new File(path + outputFileName[j]);
                        if (outputFile.exists())
                            outputFile.delete();
                    }
                }
            }
        }

        if(created){
        System.out.print("Enter file name you want to open: ");
        Scanner kb = new Scanner(System.in);
        String userfileName = kb.nextLine();

        File userFile = null;
        try {
            userFile = new File(path + userfileName);
            if (!userFile.exists()) {
                throw new FileNotFoundException(userFile.getName());
            } else {
                // printing logic to add in switch case
                BufferedReader bf = new BufferedReader(new FileReader(path + outputFileName[0]));
                String reading;
                System.out.println("PRINTING OUTPUT FILE : "+userfileName);
                while ((reading = bf.readLine()) != null) {
                    System.out.println(reading);
                }
                bf.close();
            }
        } catch (FileNotFoundException e) {

            //if the given input name from user is not valid this block will give the another chance to open file
            System.out.println("Second Chance!! Enter file name you want to open: ");
            userfileName = kb.nextLine();
            try {
                userFile = new File(path + userfileName);
                if (!userFile.exists()) {
                    throw new FileNotFoundException(userfileName);
                } else {
                    // printing logic to add in switch case
                    BufferedReader bf = new BufferedReader(new FileReader(path + outputFileName[0]));
                    String reading;
                    System.out.println("PRINTING OUTPUT FILE: "+userfileName);
                    while ((reading = bf.readLine()) != null) {
                        System.out.println(reading);
                    }
                    bf.close();
                }
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            } catch (IOException er) {
                throw new RuntimeException(er);
            }
        } catch (IOException er) {
            throw new RuntimeException(er);
        }
    }
    }
}
