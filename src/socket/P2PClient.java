package socket;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import file.FileObject;

public class P2PClient {


	public static void main(String[] args) {

		Node nNode;
		try{
			String option="";
			String check="";
			String fileName="";
			String peerName;
			int peerPort=0;
			String peerHostAddress;

			System.out.println("Client!!");

			File configFile = new File("config.xml");
			DocumentBuilderFactory docbldFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docbldFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);

			//normalize the DOM tree
			doc.getDocumentElement().normalize();

			//get the list of peers and their information from XML <Peers> tag
			NodeList nodeList = doc.getElementsByTagName("Peers");

			int portNo;
			portNo = Integer.parseInt(args[0]);
			Socket clientSocket = new Socket("localhost",portNo);
			Socket socket;
			ObjectOutputStream sockOut;
			ObjectInputStream sockIn;
			Scanner sc = new Scanner(System.in);
			
			Queue<FileObject> pushQ = new LinkedList<FileObject>();
			do{
				System.out.println("****MENU****");
				System.out.println("1. Create");
				System.out.println("2. Modify");
				System.out.println("3. Poll");
				System.out.println("4. Pull");
				System.out.println("5. Exit");

				option = sc.nextLine();

				ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());


				switch(option){
				case "1":
					objOut.writeObject(option);
					System.out.println("Enter file name:");
					fileName= sc.nextLine();
					objOut.writeObject(fileName);
					check = (String) objIn.readObject();
					System.out.println("File Created");
					break;
				case "2":
					objOut.writeObject(option);
					System.out.println("Enter file name:");
					fileName= sc.nextLine();
					objOut.writeObject(fileName);
					check = (String) objIn.readObject();
					break;
				case "3":
					String fName="", lastModTime="";
					int version, origId;
					System.out.println("Sending Invalidate message:");
					objOut.writeObject(option);
					int size = objIn.readInt();
					int i = 0;
					while(i++<size){

						fName = (String) objIn.readObject();
						lastModTime = (String) objIn.readObject();
						version = objIn.readInt();
						origId = objIn.readInt();
						FileObject fObj2 = new FileObject(fName, version, lastModTime, origId);
						for(int j=0;j<nodeList.getLength();j++){
							nNode = nodeList.item(j);
							
							if (nNode.getNodeType() == Node.ELEMENT_NODE){
								
								Element element = (Element) nNode;

								//get all the attributes for each of the servers

								peerPort = Integer.parseInt(element.getElementsByTagName("PeerPort").item(0).getTextContent());
								peerHostAddress = element.getElementsByTagName("PeerIP").item(0).getTextContent();
								if(peerPort == portNo){
									//continue;
								}
								else{
									socket = new Socket("localhost", peerPort);
									sockOut = new ObjectOutputStream(socket.getOutputStream());
									sockIn = new ObjectInputStream(socket.getInputStream());

									sockOut.writeObject("6");
									sockOut.writeObject(fObj2.fileName);
									sockOut.flush();
									sockOut.writeObject(fObj2.lastModifiedTime);
									sockOut.flush();
									sockOut.writeInt(fObj2.versionNumber);	
									sockOut.flush();
									sockOut.writeInt(fObj2.originatingServerId);
									sockOut.flush();

								}
							}
						}
						
					}
					objOut.flush();
					
					break;
				case "4":
					System.out.println("In Pull Mode.");
					objOut.writeObject(option);
					break;
					
				case "5":
					System.out.println("Client Closed!!");
					System.exit(0);
					break;

				}
			}while(!option.equals("5"));

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
