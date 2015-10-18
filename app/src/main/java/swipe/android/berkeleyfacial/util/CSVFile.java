package swipe.android.berkeleyfacial.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import swipe.android.berkeleyfacial.model.Data;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(){
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {

                csvLine = csvLine.replaceAll("'","");
                csvLine = csvLine.replaceAll(" ","");
                String[] row = csvLine.split(",");
                for(String s : row)
                    resultList.add(s);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
//male like assume
    public List<Data> read2(){
        List<Data> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {

                csvLine = csvLine.replaceAll("'","");
                csvLine = csvLine.replaceAll(" ","");
                String[] row = csvLine.split(",");
                for(String s : row) {
                   String[] f2 = s.split("@");
                    if(f2.length > 1 && f2[1].equals("b"))
                        resultList.add(new Data(f2[0],f2[0], true));
                    else
                        resultList.add(new Data(f2[0],f2[0], false));

                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}