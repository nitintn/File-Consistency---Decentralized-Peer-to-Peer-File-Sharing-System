package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import file.FileConst;
import file.FileObject;

public class P2PServer {

	public static void main(String[] args) {

		try {
			int portNo = Integer.parseInt(args[0]);
			ServerSocket socket = new ServerSocket(portNo);
			System.out.println("Server is up and running!!!");
			while (true) {
				Socket serverSocket = socket.accept();
				Thread t = new Thread(new PeerConnect(serverSocket, portNo));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class PeerConnect implements Runnable {

	public static String Path_OF_File;
	ArrayList<FileObject> fileList = new ArrayList<FileObject>();
	ArrayList<FileObject> otherFileList = new ArrayList<FileObject>();
	Queue<FileObject> pushQueue = new LinkedList<FileObject>();
	public Socket clientSocket;
	int portNumber;
	int count = 0;

	// constructor to initialize socket
	public PeerConnect(Socket socket, int portNo) throws IOException {
		this.clientSocket = socket;
		this.portNumber = portNo;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			String option = "";
			Path_OF_File = FileConst.fileDirectory;
			System.out.println("Server is up and running!!!");
			ObjectOutputStream objOut;
			ObjectInputStream objIn;
			while (true) {

				objOut = new ObjectOutputStream(clientSocket.getOutputStream());
				objIn = new ObjectInputStream(clientSocket.getInputStream());

				String fileName = "";
				option = (String) objIn.readObject();
				FileObject fObj = null;

				File configFile = new File("config.xml");
				DocumentBuilderFactory docbldFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docbldFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(configFile);

				// normalize the DOM tree
				doc.getDocumentElement().normalize();

				// get the list of peers and their information from XML <Peers>
				// tag
				NodeList nodeList = doc.getElementsByTagName("Peers");

				switch (option) {
				case "1":

					fileName = (String) objIn.readObject();

					fObj = new FileObject(fileName);

					FileObject f = fObj.createFile(this.portNumber);

					if (f != null)
						fileList.add(f);
					else
						System.out.println("Error in Creating the file!");

					for (FileObject newF : fileList) {
						newF.print();

					}
					objOut.writeObject(option);
					break;
				case "2":

					boolean flag = true;
					fileName = (String) objIn.readObject();

					FileObject fObj2 = new FileObject(fileName);

					for (FileObject newF : fileList) {

						if (newF.fileName.equals(fileName)) {
							fObj2 = newF;
							flag = false;
							break;
						}

					}
					if (flag) {
						System.out.println("File Not Found! Please try again");
					}

					int n = fObj2.modifyFile(fObj2, fObj2.originatingServerId, "HelloNew");

					// Now we need to add this file to a queue so that we can
					// push at a later time.!

					if (n > 0)
						pushQueue.add(fObj2);
					objOut.writeObject(option);

					break;

				case "3":
					objOut.writeInt(pushQueue.size());

					for (FileObject fOb : pushQueue) {
						objOut.writeObject(fOb.fileName);
						objOut.flush();
						objOut.writeObject(fOb.lastModifiedTime);
						objOut.flush();
						objOut.writeInt(fOb.versionNumber);
						objOut.flush();
						objOut.writeInt(fOb.originatingServerId);
						objOut.flush();
					}
					break;

				case "4":
					System.out.println("In Pull mode..");

					for (FileObject obj4 : otherFileList) {
						boolean check = obj4.checkStatus();
						if (check) {
							fileObtain(obj4.fileName, obj4.originatingServerId, portNumber, clientSocket);
						}
					}

					break;

				case "5":
					System.out.println("Exiting the Server!");
					System.exit(0);
					break;

				case "6":
					boolean opt = false;
					String fName = (String) objIn.readObject();
					String lastModTime = (String) objIn.readObject();
					int verNo = objIn.readInt();
					int origId = objIn.readInt();
					FileObject fObj3 = new FileObject(fName, verNo, lastModTime, origId);

					// now checking otherFileList
					if (otherFileList.size() == 0) {
						opt = true;
					} else {
						for (int i = 0; i < otherFileList.size(); i++) {
							FileObject fObj1 = otherFileList.get(i);
							if (fObj1.fileName.equals(fObj3.fileName)) {
								if (fObj1.versionNumber < fObj3.versionNumber) {
									opt = true;
									otherFileList.remove(fObj1);
									break;
								}

							}
							if (i == otherFileList.size() && opt == false) {
								opt = true;
							}
						}
					}

					if (opt == true) {
						System.out.println("Downloading the file");
						fileObtain(fObj3.fileName, fObj3.originatingServerId, portNumber, clientSocket);
						otherFileList.add(fObj3);
						opt = false;
					}

					break;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

	}

	/*
	 * Download function
	 */
	public static void fileObtain(String fileName, int sourcePort, int destPort, Socket clientSocket) {

		try {
			String sourcePath = Path_OF_File + sourcePort + "/owner/";
			String destPath = Path_OF_File + destPort + "/other/";
			DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());

			// The Path of the file to be downloaded
			File checkFile = new File(sourcePath + fileName);
			FileInputStream fin = new FileInputStream(checkFile);
			BufferedInputStream buffReader = new BufferedInputStream(fin);

			if (!checkFile.exists()) {
				System.out.println("File doesnot Exists");
				buffReader.close();
				return;
			}

			int size = (int) checkFile.length();
			byte[] buffContent = new byte[8192];

			dOut.writeLong(size);
			int numOfRead = -1;
			BufferedOutputStream buffOut = new BufferedOutputStream(clientSocket.getOutputStream());

			while ((numOfRead = buffReader.read(buffContent)) != -1) {
				dOut.write(buffContent, 0, numOfRead);
			}

			System.out.println("Transferring File SUCCESS !!!");
			buffReader.close();
		} catch (IOException ex) {
			System.err.println("Exception in file sharing");
			ex.printStackTrace();
		}
	}
}
