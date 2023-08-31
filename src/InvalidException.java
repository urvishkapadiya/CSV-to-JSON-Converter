/**
 * This class defines all the exception which being used in main class(CsvToJson.java)
 * @author Urvishkumar Kapadiya
 * @version 0.1
 * @see <a href="https://docs.oracle.com/en/java/javase/14/docs/api/index.html" target="_blank">Java Docs</a>
 */
class InvalidException extends Exception{

    public InvalidException()
    {
        System.out.println("Error: Input row cannot be parsed due to missing information");
    }
    public InvalidException(String s)
    {
        super(s);
    }
}

class CSVFileInvalidException extends InvalidException{

    public CSVFileInvalidException(String s)
    {
        super("File "+ s + " is invalid: field is missing.\nFile is not converted to JSON.");
    }
}

class CSVDataMissing extends InvalidException{

    public CSVDataMissing(String s,int number)
    {
        super("In file "+ s + " line "+ number + " not converted to JSON: missing data.");
    }
}

class FileNotFoundException1 extends InvalidException{

	public FileNotFoundException1(String s)
    {
        super("Could not open input file " + s  + " for reading. \nPlease check if file exists! Program will terminate after closing any opened files.");
    }
}