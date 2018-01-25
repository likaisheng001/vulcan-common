package com.vulcan.common.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.springframework.core.io.WritableResource;

/**
 * CSV file export tool class
 * @author likaisheng
 *
 */
public class CSVUtils {
	
	/**
	 * CSV file generation method
	 * @param head
	 * @param dataList
	 * @param outputPath
	 * @param filename
	 * @return
	 */
	public static File createCSVFile(List<Object> head, List<List<Object>> dataList, String outputPath, String filename){
		File csvFile = null;
		BufferedWriter csvWriter = null;
		
		try {
			csvFile = new File(outputPath + File.separator + filename + ".csv");
			File parent = csvFile.getParentFile();
			if(parent != null && !parent.exists()){
				parent.mkdirs();
			}
			csvFile.createNewFile();
			
			// GB2312 to read the separator correctly ","
			csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile),"GB2312"),1024);
			// Write the header of file
			writeRow(head, csvWriter);
			
			// Write the body of file
			for(List<Object> row : dataList){
				writeRow(row, csvWriter);
			}
			csvWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvWriter.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return csvFile;
	}
	/**
	 * Write a line of data methods
	 * @param row
	 * @param csvWriter
	 * @throws IOException 
	 */
	private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException{
		// Write content
		int size = row.size();
        for(int i=0; i<size; i++){

            Object data = row.get(i);
            StringBuffer sb = new StringBuffer();
            if(i == (size -1)){
                String rowStr = sb.append("\"").append(data).append("\"").toString();
                csvWriter.write(rowStr);
            }else {
                String rowStr = sb.append("\"").append(data).append("\",").toString();
                csvWriter.write(rowStr);
            }
        }
        csvWriter.newLine();
	}
}
