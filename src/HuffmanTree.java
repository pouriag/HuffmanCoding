import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Scanner;

//burrows and wheeler
public class HuffmanTree {
	
	private PriorityQueue<Node> nodes = new PriorityQueue<Node>();
	private PrintStream ps;
	public Node root;
	
	//pointer Class
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
	
	/*HuffmanTree(int[] count){
	 * 
	 */
	public HuffmanTree(int[] count) {
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
	}
	
	/*HuffmanTree(Scanner codeInput){
	 * 
	 * constructor for decoding
	 */
	public HuffmanTree(Scanner codeInput) {
		
		Node temp =  new Node (0,0,null, null);
		root = temp;
		
		while (codeInput.hasNextLine()){
			
			int ascii  = Integer.parseInt(codeInput.nextLine());
			String code = codeInput.nextLine();
			
			for (int i=  0 ; i< code.length()-1;i++){
				
				if (code.substring(i, i+1).equals("0")){
					
					if (temp.left == null){
						temp.left = new Node (0,0,null, null);
					}
					temp = temp.left;
				}
				
				else if (code.substring(i, i+1).equals("1")){
					
					if (temp.right== null){
						temp.right = new Node (0,0,null, null);
					}
					temp = temp.right;
				}
			}
			if (code.endsWith("0")){
				temp.left = new Node (0,ascii,null,null);
			}
			else if (code.endsWith("1")){
				temp.right = new Node(0,ascii,null,null);
			}

			temp = root;
		}
		
	}
	
	//to create the output file
	public void write(PrintStream output) {
		ps = output;
		StringBuffer SB = new StringBuffer();
		traverse(nodes.poll(), SB);

	}
	
	//method to traverse through the tree 
	public void traverse(Node N, StringBuffer sb) {

		if (N.left != null) {
			sb.append("0");
			traverse(N.left, sb);
			sb.deleteCharAt(sb.length() - 1);
		}

		if (N.right != null) {
			sb.append("1");
			traverse(N.right, sb);
			sb.deleteCharAt(sb.length() - 1);
		}

		if (N.ASCII > 0) {
			ps.println(N.ASCII);
			ps.println(sb.toString());
		}
	}
	
	//decode method to convert the encoded file back to normal
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
