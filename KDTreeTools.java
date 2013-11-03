import java.util.*;


public class KDTreeTools {
	private int[][] origarray;
	private int[][] sortedarray;
	private ArrayList srchresult = new ArrayList<Integer>();
	private Hashtable indices = new Hashtable();		// Keeping track of which indices have been found.

	//	This creates the KD-tree. The argument tells getList which of the test lists should be returned.
	public KDTree buildKDTree(int whichinput) {

		origarray = getList(whichinput);				// Original data
		sortedarray = fixArray(origarray);				// Each row in origarray is sorted independently and idx added
														// as a row underneath each datarow.
		int[] idxset = sortedarray[1];					// Indices for the points we consider now.

		int depth = 0;									// Start in the first dimension.
		int dimensions = origarray.length;				// Number of dimensions.

		KDTree mytree = buildIt(idxset, depth, dimensions);

		return mytree;
	}


	//	This creates the KD-tree. The argument contains the data to be indexed.
	public KDTree buildKDTree(int[][] array) {

		origarray = array;
		sortedarray = fixArray(origarray);				// Each row in origarray is sorted independently and idx added
														// as a row underneath each datarow.
		int[] idxset = sortedarray[1];					// Indices for the points we consider now.

		int depth = 0;									// Start in the first dimension.
		int dimensions = origarray.length;				// Number of dimensions.

		KDTree mytree = buildIt(idxset, depth, dimensions);

		return mytree;
	}



	private KDTree buildIt(int[] idxset, int depth, int dimensions) {
		KDTree kdroot, leftchild, rightchild;

		if (idxset.length == 1) {
			int[] coords = new int[dimensions];

			for (int i = 0; i < dimensions; i++) {
				coords[i] = origarray[i][idxset[0]];
			}

			KDTree leaf = new KDTree(coords, idxset[0]);

			return leaf;
		}

		int splitIdx = Math.abs(idxset.length/2);
		int newdepth = (depth+1) % dimensions;

		int[] lower_idxset = getNextIndexSet(idxset, sortedarray, splitIdx, depth, newdepth, dimensions, true);
		int[] upper_idxset = getNextIndexSet(idxset, sortedarray, splitIdx, depth, newdepth, dimensions, false);

		kdroot = new KDTree(origarray[depth][idxset[splitIdx-1]]);

		leftchild  = buildIt(lower_idxset, newdepth, dimensions);
		rightchild = buildIt(upper_idxset, newdepth, dimensions);

		kdroot.insertLeftChild(leftchild);
		kdroot.insertRightChild(rightchild);

		return kdroot;
	}


	// From a current 'slice', get the same indices, but sorted according to the data in the next depth.
	private int[] getNextIndexSet(int[] idxset, int[][] sortedarray, int splitIdx, int depth,
										int newdepth, int dimensions, boolean lowerpart) {

		int[][] new_idxset;

		// Jeg finder de indices der er over/under splitIdx, og laegger dem med de tilhoerende
		// data for den nye dybde i en array - 1. raekke er data, anden raekke er indices.
		// Derefter sorterer jeg paa dataraekken, og returnerer index-raekken.
		// Jeg ved at man ikke burde sortere her, da dette skal kunne loeses i lineaer tid...
		// men det kommer senere - dette er et quick-fix.

		if (lowerpart) {														// We deal with either upper/right
			new_idxset = new int[2][splitIdx];									// or lower/left part.

			for (int i = 0; i < splitIdx; i++) {
				new_idxset[0][i] = origarray[newdepth][idxset[i]];
				new_idxset[1][i] = idxset[i];
			}
		}
		else {
			new_idxset = new int[2][idxset.length - splitIdx];

			int j = 0;
			for (int i = splitIdx; i < idxset.length; i++) {
				new_idxset[0][j] = origarray[newdepth][idxset[i]];
				new_idxset[1][j] = idxset[i];
				++j;
			}
		}

		new_idxset = doQuicksort2(new_idxset, 0);

		return new_idxset[1];
	}


	// Takes an array as input, sorts each row independently, and adds indices to the original position
	// after each datarow. We then return an array that has twice the number of rows.
	private int[][] fixArray(int[][] array) {
		int[][] result   = new int[2*array.length][array[0].length];

		for (int i = 0; i < array.length; i++) {
			int[][] tmparray = new int[2][array[i].length];

			for (int j = 0; j < array[i].length; j++) {
				tmparray[0][j] = array[i][j];
				tmparray[1][j] = j;
			}


			tmparray = doQuicksort2(tmparray, 0);
			result[i*2]   = tmparray[0];
			result[i*2+1] = tmparray[1];
		}

		return result;
	}



	// Denne metode skal vel returnere en array af punkter..?
	public int searchKDTree(KDTree node, int[][] range) {
		int hitcounter = searchKDTree(node, range, 0, range.length, 0);
		return hitcounter;
	}


	public int searchKDTree(KDTree node, int[][] range, int depth, int dimensions, int hitcounter) {
		int[] leafdata = node.Key();

		if (node.isLeaf()) {
			boolean inRange = true;

			for (int i = 0; i < leafdata.length; i++) {
				if (leafdata[i] < range[i][0] || leafdata[i] > range[i][1]) {
					inRange = false;
				}
			}
			if (inRange) 	{
				if (!indices.containsKey(node.Index())) {
					indices.put(node.Index(), "FOUND");	// Just to keep track of what's been found.
														// The search is repeated for growing ranges,
														// as it's unlikely that everything is found
														// in forst call.

					srchresult.add(node.Index());	// This is what we're really after.
				}									// Can be accessed through method getResult().

				++hitcounter;
				//printList(leafdata, node.Index());
				return hitcounter;
			}
		}
		else {
			// Jeg soeger i venstre undertrae hvis nodevaerdi >= rangevaerdi.
			if (range[depth][0] <= leafdata[0]) {
				hitcounter = searchKDTree(node.MyLeftChild(), range, (depth+1)%dimensions, dimensions, hitcounter);
			}

			// Jeg soeger i hoejre undertrae hvis nodevaerdi <= rangevaerdi. Dette er min maade
			// at implementere den lexikografiske sammenligning paa. Der kan forekomme undertraeer,
			// hvis rod er f.ex. a, og hvor venstre undertrae indeholder (som forventet) de punkter
			// der har vaerdier <= a, men ogsaa i hoejre trae kan der forekomme punkter
			// hvis vaerdi er lig med a. Se exempel 1 i getList().
			if (range[depth][1] >= leafdata[0]) {
				hitcounter = searchKDTree(node.MyRightChild(), range, (depth+1)%dimensions, dimensions, hitcounter);
			}
		}

		return hitcounter;
	}


	public ArrayList getSearchResult() {
		return srchresult;
	}

	private static void printList(int[][] list) {
		for (int x = 0; x < list.length; x++) {
			for (int y = 0; y < list[x].length; y++) {
				System.out.print(list[x][y] + " ");
			}
			System.out.println();
		}
	}

	private static void printList(int[] list, int index) {
		for (int i = 0; i < list.length; i++) {
			System.out.print(list[i] + " ");
		}
		System.out.println(" idx: " + index);
	}


	private static int[][] getList(int which) {

		if (which == 0) {
			int[][] list0 = {
						{7, 3, 2, 1, 3, 4, 7, 12, 0, 23}
						};
			return list0;
		}
		else if (which == 1) {
			int[][] list1 = {
						{6, 5, 3, 1, 7, 7, 7, 7},
						{7, 4, 5, 9, 2, 2, 6, 9}
						};

			return list1;
		}
		else if (which == 2) {
			int[][] list2 = {
						{6, 5, 3, 1, 7, 7},
						{7, 4, 5, 9, 2, 3},
						{8, 2, 1, 7, 3, 2}
						};

			return list2;
		}
		else if (which == 3) {
			int[][] list3 = {
						{12,6,1,2,9,5,17,3,19,11,4,7,13,8,14,10,25,30,20},
						{9,10,1,5,3,12,8,4,7,2,14,16,6,11,15,30,20,19,25}
						};

			return list3;
		}
		else if (which == 4) {
			int[][] list4 = {
						{105,100,80,70,62,59,37,30,23,19,10,3},
						{10,20,30,40,50,60,70,80,90,100,110,120}
						};

			return list4;
		}
		else if (which > 4) {
	    	int[][] list5 = new int[2][which];

			for (int i = 0; i < which; i++) {
				list5[0][i] = (int)(Math.random() * which);
				list5[1][i] = (int)(Math.random() * which);
			}

			return list5;
	    }
	    else {
			int[][] list1 = {
						{6, 5, 3, 1, 7, 7},
						{7, 4, 5, 9, 2, 3}
						};

			return list1;
		}
	}



/* --------------------- HERUNDER KUN QuickSort -------------------------------------- */
// Denne quicksort sorterer en matrix med 2 dimensioner, hvor alle raekker
// skal vaere lige lange.
// Indtil videre sorteres altid paa 2. dimension, men man kan selv, via rownum,
// angive hvilken raekke der sorteres efter.
// Denne qsort mangler at blive randomiseret...

	private int[][] doQuicksort2(int[][] list, int rownum) {
		qSort(list, 0, list[rownum].length-1, rownum);
		return list;
	}

    private void qSort(int[][] list, int p, int r, int rownum) {
       int q;

       if (p < r) {
		   q = qsPartition(list, p, r, rownum);
		   qSort(list, p, q-1, rownum);
		   qSort(list, q+1, r, rownum);
	   }
	}


	private int qsPartition(int[][] list, int p, int r, int rownum) {
       int x, i, j;
       int rows = list.length;

	   x = list[rownum][r];
       i = p - 1;

       for (j = p; j < r; j++) {
          if (list[rownum][j] <= x) {
			  ++i;

			  for (int rowid = 0; rowid < rows; rowid++) {
				  int tmp = list[rowid][j];
				  list[rowid][j] = list[rowid][i];
				  list[rowid][i] = tmp;
			  }
		  }
	   }

		for (int rowid = 0; rowid < rows; rowid++) {
			int tmp = list[rowid][i+1];
			list[rowid][i+1] = list[rowid][r];
			list[rowid][r] = tmp;
		}

		return i+1;
	}
}






