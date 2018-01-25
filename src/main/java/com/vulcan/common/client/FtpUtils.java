package com.vulcan.common.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

/**
 * Ftp tool class
 * based on apache.commons-net
 * @author likaisheng
 *
 */
public class FtpUtils {
	
	Logger logger = Logger.getLogger(FtpUtils.class);
	private FTPClient ftpClient = null;
	private String server;
	private int port;
	private String username;
	private String password;
	
	public FtpUtils(String server, int port, String username, String password) {
		super();
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Connect server
	 * @return connect success will return true
	 */
	public boolean open(){
		if(ftpClient != null && ftpClient.isConnected()){
			return true;
		}
		try {
			// Create FTPClient
			ftpClient = new FTPClient();
			ftpClient.connect(server,port);
			ftpClient.login(username,password);
			
			// Check if the connection is successful
			int reply = ftpClient.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)){
				throw new RuntimeException("FTP server refused connection.");
			}
			logger.info("FTP connect success." + server + ":" + port);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // set upload mode:binally or ascii
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FtpUtils open connect error.cause:" + e);
			close();
			return false;
		} 
	}

	/**
	 * Switch to the parent directory
	 * @return
	 */
	private boolean changeToParentDir(){
		try {
			return ftpClient.changeToParentDirectory();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Change the current directory to the specified directory
	 * @param dir destination
	 * @return
	 */
	private boolean cd(String dir){
		try {
			return ftpClient.changeWorkingDirectory(dir);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get all the files in the directory name
	 * @param filePath
	 * @return
	 */
	private FTPFile[] getFileList(String filePath){
		try {
			return ftpClient.listFiles(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Change the working directory
	 * @param ftpPath
	 * @return
	 */
	public boolean changeDir(String ftpPath){
		if(!ftpClient.isConnected()){
			return false;
		}
		try {
			// Slash the path of unity
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for(int i = 0; i<chars.length; i++){
				if('\\' == chars[i]){
					sbStr.append('/');
				}else{
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			if(ftpPath.indexOf('/') == -1){
				// Only one directory
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "iso-8859-1"));
			}else{
				// Multi-level directory
				String[] paths = ftpPath.split("/");
				for(int i=0; i<paths.length; i++){
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(),"iso-8859-1"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Circle create a directory,and set the working directory for the current directory created
	 * @param ftpPath Need to create the directory 
	 * @return
	 */
	public boolean mkDir(String ftpPath){
		if(!ftpClient.isConnected()){
			return false;
		}
		try {
			// Slash the path of unity
			char[] chars = ftpPath.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for(int i = 0; i<chars.length; i++){
				if('\\' == chars[i]){
					sbStr.append('/');
				}else{
					sbStr.append(chars[i]);
				}
			}
			ftpPath = sbStr.toString();
			if(ftpPath.indexOf('/') == -1){
				// Only one directory
				ftpClient.makeDirectory(new String(ftpPath.getBytes(),"iso-8859-1"));
				ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(),"iso-8859-1"));
			}else{
				// Multi-level directory
				String[] paths = ftpPath.split("/");
				for(int i = 0; i< paths.length; i++){
					ftpClient.makeDirectory(new String(paths[i].getBytes(),"iso-8859-1"));
					ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(),"iso-8859-1"));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Upload file to FTP server
	 * @param localDiretoryAndFileName 	Local direcotry or file name
	 * @param ftpFileName 		The file name uploaded to the server
	 * @param ftpDirectory 		FTP direcotry. example:/path1/path2/,If the directory does not exist will automatically create a directory
	 * @return
	 */
	public boolean upload(String localDiretoryAndFileName, String ftpFileName, String ftpDirectory){
		boolean flag = false;
		
		if(!ftpClient.isConnected()){
			return false;
		}
		if(ftpClient != null){
			File srcFile = new File(localDiretoryAndFileName);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(srcFile);
				// Create directory
				mkDir(ftpDirectory);
				ftpClient.setBufferSize(100000);
				ftpClient.setControlEncoding("UTF-8");
				// Set type of file (binary)
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				// Upload
				flag = ftpClient.storeFile(new String(ftpFileName.getBytes(),"iso-8859-1"), fis);
			} catch (Exception e) {
				close();
				e.printStackTrace();
				logger.error("FtpUtils upload error.cause:" + e);
				return false;
			}finally{
				try {
					fis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		logger.info("upload file success,local filename: " + localDiretoryAndFileName + ",destination :" + ftpDirectory + "/" + ftpFileName);
		return flag;
	}
	
	/**
	 * Download file from The FTP Server
	 * @param ftpDirectoryAndFileName  Ftp server file path, starting with /dir
	 * @param localDirectoryAndFileName  Save to a local directory
	 * @return
	 */
	public boolean download(String ftpDirectoryAndFileName, String localDirectoryAndFileName){
		if(!ftpClient.isConnected()){
			return false;
		}
		ftpClient.enterLocalPassiveMode(); // Use passive mode as default
		try {
			// Slash the path of unity
			char[] chars = ftpDirectoryAndFileName.toCharArray();
			StringBuffer sbStr = new StringBuffer(256);
			for(int i = 0; i < chars.length; i++){
				if('\\' == chars[i]){
					sbStr.append('/');
				}else{
					sbStr.append(chars[i]);
				}
			}
			ftpDirectoryAndFileName = sbStr.toString();
			String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf("/"));
			String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf("/") + 1);
			changeDir(filePath);
			ftpClient.retrieveFile(new String(fileName.getBytes(),"iso-8859-1"), new FileOutputStream(localDirectoryAndFileName));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Return the list of files under the FTP directory
	 * @param pathName
	 * @return
	 */
	public String[] getFileNameList(String pathName){
		try {
			return ftpClient.listNames(pathName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Delete the file of FTP Server
	 * @param ftpDirAndFileName
	 * @return
	 */
	public boolean deleteFile(String ftpDirAndFileName){
		if(!ftpClient.isConnected()){
			return false;
		}
		try {
			return ftpClient.deleteFile(ftpDirAndFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Delete the directory of FTP Server
	 * @param ftpDirectory
	 * @return
	 */
	public boolean deleteDirectory(String ftpDirectory){
		if(!ftpClient.isConnected()){
			return false;
		}
		try {
			return ftpClient.removeDirectory(ftpDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * close connection
	 */
	public void close(){
		try {
			if(ftpClient != null && ftpClient.isConnected()){
				ftpClient.disconnect();
			}
			logger.info("Close connection success.The Server host:" + server + ":" +port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	public static void main(String[] args) {
		FtpUtils ftpUtils = new FtpUtils("10.254.128.119", 2121, "ftpuser", "chunbotest123");
		String ftpFileName = "weightPart.csv";
		try {
			if(ftpUtils.open()){
				// Upload
				ftpUtils.upload("c:/test/weightPart.csv", ftpFileName, "test");
				// Iterate
				FTPFile[] list = ftpUtils.getFileList("test");
				for(FTPFile file : list){
					String name = file.getName();
					System.out.println("---" + new String(name.getBytes("iso-8859-1"),"GB2312"));
				}
				
				// Only iterate the file name in the specified directory
				String[] names = ftpUtils.getFileNameList("test");
				for(String name : names){
					System.out.println(new String(name.getBytes("iso-8859-1"),"GB2312"));
				}
				
				// Download
				boolean download = ftpUtils.download("/test/weightPart.csv", "d:/weightPart.csv");
				System.out.println(download);
				
				// Delete file
				String ftpDirAndFileName = "test/weightPart.csv";
				boolean deleteFile = ftpUtils.deleteFile(new String(ftpDirAndFileName.getBytes(),"iso-8859-1"));
				System.out.println(deleteFile);
				
				// Delete Directory
				boolean deleteDirectory = ftpUtils.deleteDirectory("test1");
				System.out.println(deleteDirectory);
				
				ftpUtils.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
