import java.io.PrintStream;
import java.util.PriorityQueue;

//burrows and wheeler
public class HuffmanTree2 {
	
	private PriorityQueue<Node> nodes = new PriorityQueue<Node>();
	public Node root;
	
	//pointer class
	private class Node implements Comparable<Node> {

		public int freq;
		public int ASCII;
		public Node left;
		public Node right;
		
		public Node(int freq, int ascii, Node l, Node r) {

			this.ASCII = ascii;
			this.freq = freq;
			this.left = l;
			this.right = r;
		}

		@Override
		public int compareTo(Node o) {
			if (this.freq == o.freq)
				return 0;
			else if (this.freq > o.freq)
				return 1;
			else
				return -1;
		}
		

		public String toString() {
			return freq + ", " + ASCII;
		}

	}
	
	/*HuffmanTree2(int[] count){
	 * 
	 */
	public HuffmanTree2(int[] count) {
		for (int i = 0; i < count.length; i++) {
			if (count[i] > 0) {
				Node N = new Node(count[i], i, null, null);
				nodes.add(N);
			}
		}
		
		//in order to find the end of the tree
		nodes.add(new Node(1, 256, null, null));

		while (nodes.size() > 1) {
			Node n1 = nodes.poll();
			Node n2 = nodes.poll();
			Node branch = new Node(n1.freq + n2.freq, 0, n1, n2);
			nodes.add(branch);
		}
		
		root = nodes.poll();
	}
	
	//decode constructor
	public HuffmanTree2(BitInputStream input) {
		root = readNode(input);		
	}
	
	// this method will read each node to check if it's a branch or leaf
	// then assign 0 & 1 correspondingly 
	private Node readNode(BitInputStream input) {
		//so basically the code below is 
		//if (input.readBit() == 0) {
		//	N = new Node(0, 0, readNode(input), readNode(input));
		//} else {
		//		N = new Node(0, read9(input), null, null);
		//}
		//return N;
		return input.readBit() == 0 ? new Node(0, 0, readNode(input), readNode(input)): new Node(0, read9(input), null, null);
	}
	
	
	// it traverse throw the tree top to bottom
	public void traverse(Node N, StringBuffer sb, String[] codes) {

		if (N.left != null) {
			sb.append("0");
			traverse(N.left, sb, codes);
			sb.deleteCharAt(sb.length() - 1);
		}

		if (N.right != null) {
			sb.append("1");
			traverse(N.right, sb, codes);
			sb.deleteCharAt(sb.length() - 1);
		}

		if (N.ASCII > 0) {
			codes[N.ASCII] = sb.toString();
		}
	}

	//write the header in the encoded file
	public void writeHeader(BitOutputStream output) {
		writePreorder(root, output);
	}
	
	//to write down the preorder traversal
	private void writePreorder(Node N, BitOutputStream output) {
		if (N.left != null) {
			output.writeBit(0);
			writePreorder(N.left, output);
			writePreorder(N.right, output);
		} else {
			// leaf
			output.writeBit(1);
			write9(output, N.ASCII);
		}
	}
	
	// pre : 0 <= n < 512
	// post: writes a 9-bit representation of n to the given output stream
	private void write9(BitOutputStream output, int n) {
		for (int i = 0; i < 9; i++) {
			output.writeBit(n % 2);
			n /= 2;
		}
	}

	// pre : an integer n has been encoded using write9 or its equivalent
	// post: reads 9 bits to reconstruct the original integer
	private int read9(BitInputStream input) {
		int multiplier = 1;
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += multiplier * input.readBit();
			multiplier *= 2;
		}
		return sum;
	}
	
	// assign the tree map to the array
	public void assign(String[] codes) {
		traverse(root, new StringBuffer(), codes);
	}
	
	//decodes the encoded files back to normal
    public void decode(BitInputStream input, PrintStream output, int charMax) {
		int count;
		Node temp = root;

		while ((count = input.readBit())!= -1){
			if( count == 0){
				temp = temp.left ;
			}
			else{
				temp = temp.right;
			}
			
			if (temp.ASCII>0){

				if(temp.ASCII == charMax){
					return;
				}
				output.write(temp.ASCII);;
				temp = root;
			}
		}
	}
}
