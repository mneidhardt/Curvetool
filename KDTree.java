//Based on BSTree.java which was originally written by Francois Rivest, 1997
//E-Mail: frives@po-box.mcgill.ca
//Made into KDTree.java by me, meem@mail.dk, 2005.


//==============================================================================
// kd-Tree class
//
//==============================================================================

public class KDTree
{
	int[] m_Key;
	int m_Index;				// Data in this node that is unrelated to searching.
	KDTree m_LeftChild;
	KDTree m_RightChild;
	boolean is_a_leaf = true;



	//Constructs a node.
	public KDTree(int[] key)
	{
		m_Key = key;
		m_LeftChild = null;
		m_RightChild = null;
	}

	//Constructs a node.
	public KDTree(int key)
	{
		int tmp[] = new int[1];
		tmp[0] = key;
		m_Key = tmp;
		m_LeftChild = null;
		m_RightChild = null;
	}

	//Constructs a node.
	public KDTree(int[] key, int index)
	{
		m_Key = key;
		m_Index = index;
		m_LeftChild = null;
		m_RightChild = null;
	}



	//Returns the current node value
	public int[] Key()
	{
		return m_Key;
	}

	public int Index()
	{
		return m_Index;
	}


	//Returns the current node's left child
	public KDTree MyLeftChild()
	{
		return m_LeftChild;
	}


	//Returns the current node's right child
	public KDTree MyRightChild()
	{
		return m_RightChild;
	}


	//Returns true if node is a leaf, else false.
	public boolean isLeaf()
	{
		return is_a_leaf;
	}


	public void insertLeftChild(KDTree child) {
		m_LeftChild = child;
		is_a_leaf = false;
	}

	public void insertRightChild(KDTree child) {
		m_RightChild = child;
		is_a_leaf = false;
	}


	//Prints out the entire tree in numerical order (of the first number in each key).
	// String depth initially contains just 0 or 1 space, and as it grows will help visualise the tree.
	public static void InOrderWalk(KDTree m_Node, String depth) {
		if (m_Node != null) {

			InOrderWalk(m_Node.m_RightChild, (depth+"    "));		// Det er aestetiske aarsager der goer
																	// at jeg tager hoejre foer venstre.
																	// Paa denne maade kan jeg visualisere traeet
																	// bedre.

			System.out.print(depth);
			int[] mykey = m_Node.Key();

			for (int i = 0; i < mykey.length; i++) {
				System.out.print(mykey[i]);
				if (i < mykey.length-1) { System.out.print(","); }
			}

			System.out.println();

			InOrderWalk(m_Node.m_LeftChild, (depth+"    "));
		}
	}
}

