import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class BinaryCelebrityTree {
	static CelebrityNode root; 
	CelebrityNode current;
	private File initialFile;
	private static RandomAccessFile randomAccessFile;
	private static String initialCelebrityString = "Obama";
	private static byte[] emptyBuffer = new byte[CelebrityNode.getBufferSize()];
	private final static int initialBufferLocation = 0;
	private static int size;
    

    
	public BinaryCelebrityTree() {
		setRoot(new CelebrityNode(initialCelebrityString));
	}
	
	public static void main(String [] args) {
		BinaryCelebrityTree bct = new BinaryCelebrityTree("file.txt");
		System.out.println("yooo");
	}
	
	public BinaryCelebrityTree(String filePath) {
		readTheFileIn(filePath);
	}
	
	public void readTheFileIn(String filePath) {
		try {
			setInitialFile(new File(filePath));
			BinaryCelebrityTree.setRandomAccessFile(new RandomAccessFile(getInitialFile(), "rw"));
			if (BinaryCelebrityTree.getRandomAccessFile().readLine() != null) {
				System.out.println("file has stuff");
				setRoot(CelebrityNode.createFromBufferLocation(
								BinaryCelebrityTree.initialBufferLocation));
			} else {
				System.out.println("empty file");
		
				initFile();
			}
		} catch(IOException ioe) {
			System.out.println("Unable to open the flie" + ioe.getMessage());
		}
	}
	
	private void initFile() {
		System.out.println("inside BinaryCelebrityTree.initFile()");
		setRoot(new CelebrityNode(initialCelebrityString));
		try {
			writeToFileFirstTime();
			//BinaryCelebrityTree.getRandomAccessFile().writeUTF("123");
		} catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}	 
	}
	
	public void writeToFile(
			int offset, int nodeId, int leftNodeId, int rightNodeId, String nodeValue) throws IOException {
		
		clearBytes(offset);
		BinaryCelebrityTree.getRandomAccessFile().seek(offset + CelebrityNode.getNodeIdOffset());
		String idString = Integer.toString(nodeId);
		BinaryCelebrityTree.getRandomAccessFile().writeBytes(
				idString);
		BinaryCelebrityTree.getRandomAccessFile().seek(offset + CelebrityNode.getLeftNodeIdOffset());
		BinaryCelebrityTree.getRandomAccessFile().writeBytes(Integer.toString(leftNodeId));
		BinaryCelebrityTree.getRandomAccessFile().seek(offset + CelebrityNode.getRightNodeIdOffset());
		BinaryCelebrityTree.getRandomAccessFile().writeBytes(Integer.toString(rightNodeId));
		BinaryCelebrityTree.getRandomAccessFile().seek(offset + CelebrityNode.getNodeValueOffset());
		BinaryCelebrityTree.getRandomAccessFile().writeBytes(nodeValue);
	}
	
	private void clearBytes(int offset) throws IOException {
		BinaryCelebrityTree.getRandomAccessFile().seek(offset);
		BinaryCelebrityTree.getRandomAccessFile().write(emptyBuffer);
	}

	public CelebrityNode getRoot() {
		return getRootNode();
	}
	
	public void writeToFileFirstTime() throws IOException {
		writeToFile(initialBufferLocation, getRoot().getId(), -1, -1, initialCelebrityString);
	}	

	public static int getSize() {
		return CelebrityNode.size(root);
	}
	
	public int height() {
		return CelebrityNode.height(root);
	}

	public CelebrityNode getRootNode() {
		return root;
	}

	public void setRoot(CelebrityNode n2) {
		root = n2;
	}

	public void printInOrder(CelebrityNode node) {
//		FileWriter file = new FileWriter("Tree.txt");
//		BufferedWriter writer = new BufferedWriter(file);  
		if (node != null) {
		  printInOrder(node.left);
	     // writer.writeObject(node.left.getValue());
		  System.out.println("  Traversed " + node.getValue());
		  printInOrder(node.right);
		 
		  }
		}

	public File getInitialFile() {
		return initialFile;
	}

	public void setInitialFile(File initialFile) {
		this.initialFile = initialFile;
	}
	
	public static RandomAccessFile getRandomAccessFile() {
		return BinaryCelebrityTree.randomAccessFile;
	}

	public static void setRandomAccessFile(RandomAccessFile randomAccessFile) {
		BinaryCelebrityTree.randomAccessFile = randomAccessFile;
	}

//	public int getCounter() {
//		return size();
//	}
}
