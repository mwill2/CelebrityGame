import java.io.IOException;

public class CelebrityNode {
	private String value;
	private boolean someoneGuessed = false;
	private String guessedById = null;
	CelebrityNode right = null;
	CelebrityNode left = null;
	private boolean isInUse;
	private int id;
	private static int counter = 0;
	private final static int bufferSize = 400;
	private final static int nodeIdOffset = 0;
	private final static int nodeIdSize = 4;
	private final static int leftNodeIdOffset = 4;
	private final static int leftNodeIdSize = 4;
	private final static int rightNodeIdOffset = 8;
	private final static int rightNodeIdSize = 4;
	private final static int nodeValueOffset = 12;
	private final static int nodeValueSize = 388;

	private static int getNodeIdSize() {
		return nodeIdSize;
	}

	private static int getLeftNodeIdSize() {
		return leftNodeIdSize;
	}

	private static int getRightNodeIdSize() {
		return rightNodeIdSize;
	}

	private static int getNodeValueSize() {
		return nodeValueSize;
	}

	public static int getNodeIdOffset() {
		return nodeIdOffset;
	}

	public static int getRightNodeIdOffset() {
		return rightNodeIdOffset;
	}

	public static int getLeftNodeIdOffset() {
		return leftNodeIdOffset;
	}

	public static int getNodeValueOffset() {
		return nodeValueOffset;
	}

	public static int getBufferSize() {
		return bufferSize;
	}

	public boolean isInUse() {
		return isInUse;
	}

	private int getSeekLocation() {
		return getId() * CelebrityNode.getBufferSize();
	}

	public int getNodeIdLocation() {
		return getSeekLocation() + CelebrityNode.getNodeIdOffset();
	}

	private int getNodeIdLocation(int bufferLocation) {
		return bufferLocation + CelebrityNode.getNodeIdOffset();
	}

	private int getNodeValueLocation(int bufferLocation) {
		return bufferLocation + CelebrityNode.getNodeValueOffset();
	}

	public static CelebrityNode createFromBufferLocation(int bufferLocation)
			throws IOException {
		CelebrityNode cn = new CelebrityNode();
		cn.parseAndSetNodeId(bufferLocation);
		cn.readLeftNodeId(bufferLocation);
		cn.readRightNodeId(bufferLocation);
		cn.parseAndSetNodeValue(bufferLocation);
		return cn;
	}
	
	private void parseAndSetNodeId(int bufferLocation) throws IOException {
		setId(readNodeId(bufferLocation));
	}
	
	private void parseAndSetNodeValue(int bufferLocation) throws IOException {
		setNodeValue(readNodeValue(bufferLocation));
	}
	
	public void setNodeValue(String nodeValueIn) {
		value = nodeValueIn;
	}

	private int calculateNextBufferLocation(int nextId) {
		return getBufferSize() * nextId;
	}

	private int readLeftNodeId(int bufferLocation) throws IOException {
		return getChildNodeId(getLeftNodeIdLocation(bufferLocation), getLeftNodeIdSize(), bufferLocation, "left");
	}

	private int readRightNodeId(int bufferLocation) throws IOException {
		return getChildNodeId(
				getRightNodeIdLocation(bufferLocation), getRightNodeIdSize(), bufferLocation, "right");
	}

	private int getChildNodeId(int offset, int size, int bufferLocation, String side)
			throws IOException {
		String idString = readStringFromSlotInBuffer(offset, size, bufferLocation);
		int id = Integer.parseInt(idString);
		createChildNodeIfExists(id, side);
		return id;
	}
	
	private String readStringFromSlotInBuffer(
			int offset, int size, int bufferLocation) throws IOException {
		byte[] buffer = new byte[size];
		int bufferOffset = 0;
		BinaryCelebrityTree.getRandomAccessFile().seek(offset);
		BinaryCelebrityTree.getRandomAccessFile().read(buffer, bufferOffset, size);

		for (int i = 0; i < buffer.length; i++) {
			System.out.println((char)buffer[i]);
		}
		StringBuilder sb = new StringBuilder();
		String result = new String(buffer);
		
		result = result.trim();

		System.out.println("result = '" + result + "'");
		return result;
	}
	
	private void createChildNodeIfExists(int id, String side) throws IOException {
		if (id > -1) {
			if (side == "left") {
				setLeft(CelebrityNode.createFromBufferLocation(calculateNextBufferLocation(id)));
			} else if (side == "right") {
				setRight(CelebrityNode.createFromBufferLocation(calculateNextBufferLocation(id)));				
			}
			
		}
	}

	private String readNodeValue(int bufferLocation) throws IOException {
		return readStringFromSlotInBuffer(
				getNodeValueLocation(bufferLocation), getNodeValueSize(), bufferLocation);
	}

	private int readNodeId(int bufferLocation) throws IOException {  
		return Integer.parseInt(
				readStringFromSlotInBuffer(
						getNodeIdLocation(bufferLocation), getNodeIdSize(), bufferLocation));
	}

	public int getLeftNodeIdLocation() {
		return getSeekLocation() + CelebrityNode.getLeftNodeIdOffset();
	}

	private int getLeftNodeIdLocation(int bufferLocation) {
		return bufferLocation + CelebrityNode.getLeftNodeIdOffset();
	}
	
	private int getRightNodeIdLocation(int bufferLocation) {
		return bufferLocation + CelebrityNode.getRightNodeIdOffset();
	}

	public int getRightNodeIdLocation() {
		return getSeekLocation() + CelebrityNode.getRightNodeIdOffset();
	}

	private int getNodeValueLocation() {
		return getSeekLocation() + CelebrityNode.getNodeValueOffset();
	}

	public void setIsInUse(boolean isInUseIn) {
		isInUse = isInUseIn;
	}

	public CelebrityNode(String q, CelebrityNode p, CelebrityNode lft,
			CelebrityNode rht, int idIn) {
		setup(q, p, lft, rht);
		setId(idIn);
	}

	public CelebrityNode(String q, CelebrityNode p, CelebrityNode lft,
			CelebrityNode rht) {
		setup(q, p, lft, rht);
		generateAndSetId();
	}

	private void setup(String q, CelebrityNode p, CelebrityNode lft,
			CelebrityNode rht) {
		setValue(q);
		setLeft(lft);
		setRight(rht);
		setIsInUse(false);
		counter++;
	}

	public CelebrityNode(String q) {
		setValue(q);
		generateAndSetId();
	}

	public CelebrityNode(String q, int idIn) {
		setValue(q);
	}

	public CelebrityNode() {
		counter++;
	}

	private void generateAndSetId() {
		id = nextCounterValue();
		System.out.println("generateAndSetId Id# = " + id);
	}

	private int nextCounterValue() {
		return counter++;
	}

	public String getValue() {
		return value;
	}

	public static int size(CelebrityNode t) {
		if (t == null) {
			return 0;
		} else {
			return 1 + size(t.left) + size(t.right);
		}
	}

	public static int height(CelebrityNode t) {
		if (t == null) {
			return -1;
		} else {
			return 1 + max(height(t.left), height(t.right));
		}
	}

	private static int max(int height, int height2) {
		if (height > height2) {
			return height;
		} else
			return height2;
	}

	public CelebrityNode getLeft() {
		if (this.left != null) {
			return this.left;
		} else {
			return null;
		}
	}

	public CelebrityNode getRight() {
		if (this.right != null) {
			return this.right;
		} else {
			return null;
		}
	}

	public void setLeft(CelebrityNode n) {
		this.left = n;
	}

	public void setRight(CelebrityNode current) {
		this.right = current;
	}

	public void setValue(String n2) {
		this.value = n2;
	}

	public String getGuessedById() {
		return guessedById;
	}

	public void setGuessedById(String guessedById) {
		this.guessedById = guessedById;
	}

	public boolean isSomeoneGuessed() {
		return someoneGuessed;
	}

	public void setSomeoneGuessed(boolean someoneGuessed) {
		this.someoneGuessed = someoneGuessed;
	}

	public int getId() {
		return id;
	}

	public String getIdString() {

		return String.valueOf(id);
	}

	public void setId(int id) {
		this.id = id;
	}
}
