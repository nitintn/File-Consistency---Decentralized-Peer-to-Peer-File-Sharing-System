/**
 * This class is for the Metadata about Files stored in the directories.
 * Variable: 
 * 
 * @author dmehta
 * @date   Mar 05 2017
 */

package file;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileObject {

	public String fileName;	//Name of the file

	public int versionNumber;	//version number of the file

	public boolean isMaster;	//is this the master copy

	public int consistencyState; //(valid(1)|invalid(0)|TTRexpired(-1))

	public int originatingServerId; //Peer id of the server with the master copy of the file

	public int TTR; //in minutes

	public String lastModifiedTime; 

	//Constructor 1
	public FileObject(String fName) {
		this.fileName = fName;
		//this.lastModifiedTime = gc.getTime();
	}

	//Constructor 2
	public FileObject(String fName, int ver, String lastModTime) {
		fileName = fName;
		this.versionNumber = ver;
		this.lastModifiedTime = lastModTime;
	}
	
	public FileObject(String fName, int ver, String lastModTime, int orignatingId) {
		fileName = fName;
		this.versionNumber = ver;
		this.lastModifiedTime = lastModTime;
		this.originatingServerId = orignatingId;
	}
	
	/**
	 * This function creates a new file in Directory/PeerId/owner folder.
	 * @param portNumber
	 * @return FileObject
	 * @return null for any error
	 */

	public FileObject createFile(int portNumber){
		String directory = FileConst.fileDirectory + Integer.toString(portNumber) + "/owner/" ;
		String fName =  directory + this.fileName;
		System.out.println(fName);
		File file = new File(fName);
		try {
			if (file.createNewFile()){
				System.out.println("File is created!");
			}else{
				System.out.println("File already exists.");
				return null;
			}
		} catch (IOException e) {
			System.out.println("Error in Creating File");
			e.printStackTrace();
			return null;
		}
		this.versionNumber = 1;

		this.isMaster = true;

		this.originatingServerId = portNumber;

		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		this.lastModifiedTime = df.format(cal.getTime());

		this.TTR = FileConst.default_TTR;

		this.consistencyState = FileConst.Valid_Consistency_State;

		return this;

	}

	
	public int modifyFile(FileObject file, int portNumber, String text){

		String address = FileConst.fileDirectory+Integer.toString(portNumber)+"/owner/"+file.fileName;
		if(!file.isMaster){
			System.out.println("Cannot Modify this file! Not a master Copy");
			return -1;
		}

		File f = new File(address);

		if(!f.exists()){
			System.out.println("Error - File Does Not Exists!");
			return -1;
		}
		try {
			FileReader fileIn = new FileReader(address);
			BufferedReader fileReadBuf = new BufferedReader(fileIn);

			FileWriter fileout = new FileWriter(address);
			BufferedWriter filebuf = new BufferedWriter(fileout);
			PrintWriter printout = new PrintWriter(filebuf);

			String temp;

			while((temp = fileReadBuf.readLine()) != null){
				continue;
			}

			printout.write(text);
			printout.println();

			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			this.lastModifiedTime = df.format(cal.getTime());

			printout.write("File Modified at "+this.lastModifiedTime);

			printout.close();
			fileIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.versionNumber = this.versionNumber + 1;
		
		return 1;
	}
	
	public void print(){
		System.out.println(this.fileName);
		System.out.println(this.isMaster);
		System.out.println(this.lastModifiedTime);
		System.out.println(this.versionNumber);
	}

	public boolean checkStatus() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:m");
		try {
			Date lastModTime = sdf.parse(this.lastModifiedTime);
			Calendar cal = Calendar.getInstance();
			Date currentDateTime = cal.getTime();
			cal.setTime(lastModTime);
			cal.add(Calendar.MINUTE, FileConst.default_TTR);
			Date expiringDateTime = cal.getTime();

			return expiringDateTime.before(currentDateTime);

		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		return false;
		
	}
}
