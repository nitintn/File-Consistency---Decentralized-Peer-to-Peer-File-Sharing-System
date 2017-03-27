# File-Consistency---Decentralized-Peer-to-Peer-File-Sharing-System
Implemented a Decentralized Peer to Peer system with Java. 
Used Socket programming to implement the system. 
Implemented consistency mechanism in the file sharing system.

INTRODUCTION:
This implementation is Java based Peer to Peer (P2P) File Sharing System, which allows the end-users to share resources (Files). It is a Decentralized P2P file sharing system based on the popular Gnutella Protocol, where each peer acts as both server and client to each other. There is no central Indexing Server in this file system, each peer maintains its own list of files and the files listed on other peers. In this assignment, we have added consistency mechanisms to Gnutella-Style P2P file sharing system.
The peer acts as a server and client both. As a Client, it requests for files from other clients and maintains its own file system. There is another list of the neighboring connected peers maintained at each peer.
The objective of the assignment is to ensure that all copies of a file in the P2P system consistent with one another. The consistency can be achieved either using push or pull. 

1.	In Push, whenever the master copy of the file is modified, the origin server broadcasts an "Invalidate" message for the file. The invalidate message propagates exactly like a "query" message. Upon receiving an "Invalidate" message, each peer checks if it has a copy of the object (and if so, discards it) and download the new copy.
2.	In the Pull approach, it is the responsibility of the peers to poll the origin server to see if the file is modified and if it the case then download the new copy.


Implementation’s File Structure:
Folder Name: 01_GROUP10_PA3.zip
1.	Documents
2.	PA3_FileConsistencyP2P
•	sharedFolder
•	src
o	file
	FileConst.java
	FileObject.java
o	Message
	ConsistencyMessage.java
	InvalidateMessage.java
	Message.java
	MessageConst.java
	PollMessage.java
	QueryHitMessage.java
	QueryMessage.java
o	Socket
	P2PClient.java
	P2PServer.java
o	config.xml

Before RUN:
1.	Edit P2PClient.java file, to change the path according to your system, because it is the path where the shared folder is located, and file will be downloaded to this folder, line (16 & 93)
2.	 Demo:
a.	Currently it is: 
String fileDirectory = "D:/Project/HW3/hw3/hw3_consistent_p2p/sharedFolder". 
You can change this to where you want to store, and follow the next step b.

b.	Copy the sharedFolder, to the path which is specified by you, above


HOW TO RUN:
Using Command Prompt:
1.	Extract the 01_GROUP10_PA3.zip 
2.	Open cmd
3.	Go to \01_GROUP10_PA2\ PA3_FileConsistencyP2P\src\ 
3.	Run the cmd: javac file/*.java & javac message/*.java & javac socket/*.java
4.	Now to run server code: java socket.P2PServer <port_number>
5.	Now to run client code: java socket.P2PClient <port_number> 
6.	Run as many as 10 peers and wait for maximum of 40s from the start so as to let the peers create the connection.
7.	You can have as many clients you want, don’t forget to specify peer name, else the program will stop functioning.
	




IMPORTANT
The peer name passed as cmd line arg, will be used as the folder to download/read the files for the peer(s) i.e. peer name will be the folder name, when the peer asks to obtain a file, the peerID will be specified as what is returned by the search option.
The folder will be: C(Any drive):/sharedFolder/<port_number>
While downloading files, the peer who has requested the download, need not have the folder, it will be created automatically.
But, for the source folder from where it is getting downloaded, the folder must be present inside sharedFolder

