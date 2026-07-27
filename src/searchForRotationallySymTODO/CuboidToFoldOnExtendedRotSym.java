package searchForRotationallySymTODO;

import Coord.Coord2D;
import Coord.CoordWithRotationAndIndex;
import DupRemover.BasicUniqueCheckImproved;
import Model.CuboidToFoldOn;
import Model.CuboidToFoldOnInterface;
import Model.DataModelViews;
import Model.NeighbourGraphCreator;
import Model.Utils;
//import NewModelWithIntersection.fastRegionCheck.FastRegionCheck;
//import NewModelWithIntersection.filterOutTwoTops.FilterOutTwoTopsFaster4;
import NewModel.firstIteration.Nx1x1CuboidToFoldAndDrawNet;
import  NewModelWithIntersection.topAndBottomTransitionList.TopAndBottomTransitionList2;

public class CuboidToFoldOnExtendedRotSym  implements CuboidToFoldOnInterface {

	
	public CoordWithRotationAndIndex[][] neighbours;
	
	public int dimensions[] = new int[3];

	
	public CuboidToFoldOnExtendedRotSym(int a, int b, int c) {
		this(a, b, c, true, true);
	}

	public CuboidToFoldOnExtendedRotSym(int a, int b, int c, boolean verbose, boolean setup) {

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c, verbose);

		
		dimensions[0] = a;
		dimensions[1] = b;
		dimensions[2] = c;

		DIM_N_OF_Nx1x1_DIV_2 = (Utils.getTotalArea(this.dimensions)-2) / 8;
		
		if(setup) {
			setupAnswerSheetInBetweenLayers();
 			setupAnswerSheetFor1x1Cell();
		}
		
	}
	
	public int getNumCellsToFill() {
		return Utils.getTotalArea(this.dimensions);
	}
	
	public CoordWithRotationAndIndex[] getNeighbours(int cellIndex) {
		return neighbours[cellIndex];
	}
	

	@Override
	public int[] getDimensions() {
		return dimensions;
	}
	
	private int initialSepIfEvenLayers = -1;
	public static int NOT_APPLICABLE = -100;

	
	private static boolean isEvenNumberOfLayers(int area) {
		
		//Nx1x1 version of the cuboid will be even iff area is 2 mod 8:
		return (area - 2) % 8 == 0;
	}
	
	public static final int DUD_INTT = -1;
	private boolean isDudInit() {
		return this.currentLayerIndex[0] == DUD_INTT;
	}
	
	public void initializeNewBottomIndexAndRotation(int startIndex, int startRotationRelativeFlatMap, int initialSepIfEvenLayers) {
		
		prevGroundedIndexes = new int[2][DIM_N_OF_Nx1x1_DIV_2];
		prevGroundedRotations = new int[2][DIM_N_OF_Nx1x1_DIV_2];
		prevSideBumps = new int[2][DIM_N_OF_Nx1x1_DIV_2];
		
		this.prevGroundedIndexes[0][0] = startIndex;
		this.prevGroundedRotations[0][0] = startRotationRelativeFlatMap;
		
		Coord2D origInit = new Coord2D(startIndex, startRotationRelativeFlatMap);
		
		Coord2D flipedInit = TopAndBottomTransitionList2.topLeftIndexRotAfter180Flip1x4layer(neighbours, startIndex, startRotationRelativeFlatMap);

		this.prevGroundedIndexes[1][0] = flipedInit.i;
		this.prevGroundedRotations[1][0] = flipedInit.j;
		
		this.currentLayerIndex = new int[2];
		this.currentLayerIndex[0] = 0;
		this.currentLayerIndex[1] = 0;
		
		this.layerIndexOf1x1Cell = new int[2];
		this.layerIndexOf1x1Cell[0] = -1;
		this.layerIndexOf1x1Cell[1] = -1;
		
		currentLayerIndex = new int[2];
		currentLayerIndex[0] = 0;
		currentLayerIndex[1] = 0;

		//TODO: wrong; (cover 4 of them!)
		boolean tmpArray[] = new boolean[Utils.getTotalArea(this.dimensions)];
		
		Coord2D currentCellLocation = origInit;
		
		for(int i=0; i<4; i++) {
			if(tmpArray[currentCellLocation.i]) {
				System.out.println("DOH 1: currentCellLocation = origInit;");
			}
			tmpArray[currentCellLocation.i] = true;
			currentCellLocation = tryAttachCellInDir(currentCellLocation.i, currentCellLocation.j, RIGHT);
		}

		this.curState = convertBoolArrayToLongs(tmpArray);
		
		
		//Add first flipped layer if even # of layers:
		
		if(this.isEvenNumberOfLayers(this.getNumCellsToFill())) {
			//EVEN # layers
			this.initialSepIfEvenLayers = initialSepIfEvenLayers;
			
			if(this.isNewLayerValidSimpleFast(this.initialSepIfEvenLayers, 1)) {
				this.addNewLayerFast(this.initialSepIfEvenLayers, 1);
			
			} else {
				//TODO: comment out once tested:
				System.out.println("Warning: can't add 1st mirror layer");
				this.currentLayerIndex[0] = DUD_INTT;
				this.currentLayerIndex[1] = DUD_INTT;
				return;
			}
			
			flipedInit = new Coord2D(this.prevGroundedIndexes[1][1], this.prevGroundedRotations[1][1]);
			
			currentCellLocation = flipedInit;
			for(int i=0; i<4; i++) {
				if(tmpArray[currentCellLocation.i]) {
					System.out.println("DOH 2: currentCellLocation = flipedInit;");
				}
				tmpArray[currentCellLocation.i] = true;
				currentCellLocation = tryAttachCellInDir(currentCellLocation.i, currentCellLocation.j, RIGHT);
			}
			
		} else {
			//ODD # layers
			this.initialSepIfEvenLayers = NOT_APPLICABLE;
			
		}
		
		
	}


	//Constants:

	//7 *2 -1
	public static final int NUM_POSSIBLE_SIDE_BUMPS = 13;
	
	public static final int NUM_NEIGHBOURS = 4;
	public static final int NUM_ROTATIONS = 4;
	

	public static final int BAD_ROTATION = -10;
	public static final int BAD_INDEX = -20;
	
	private static final int NUM_BYTES_IN_LONG = 64;
	private static final int NUM_LONGS_TO_USE = 3;
	
	public static int NUM_SIDE_BUMP_OPTIONS = 15;
	
	//Variables to compute at construction time:
	
	private int DIM_N_OF_Nx1x1_DIV_2;
	
	private long answerSheet[][][][];
	private int newGroundedIndexAbove[][][];
	private int newGroundedRotationAbove[][][];
	
	
	private long answerSheetFor1x1Cell[][][][];
	private long answerSheetFor1x1CellAnySideBump[][][];

	private long preComputedPossiblyEmptyCellsAroundNewLayer[][][][];
	private boolean preComputedForceRegionSplitIfEmptyAroundNewLayer[][][];


	//State variables:
	public long curState[] = new long[NUM_LONGS_TO_USE];
	
	private int prevSideBumps[][];
	private int prevGroundedIndexes[][];
	private int prevGroundedRotations[][];
	private int currentLayerIndex[];
	private int layerIndexOf1x1Cell[];
	
	private long debugThru = 0L;
	private long debugStop = 0L;
	private long debugBugFix = 0L;
	//private long DEBUG_LAYER_INDEX = 14;
	
	
	
	
	public boolean isCellIoccupied(int i) {
		int indexArray = i / NUM_BYTES_IN_LONG;
		int bitShift = (NUM_BYTES_IN_LONG - 1) - i - indexArray * NUM_BYTES_IN_LONG;
		
		return ((1L << bitShift) & this.curState[indexArray]) != 0L;
	}
	
	public boolean isNewLayerValidSimpleFast(int sideBump, int indexTrail) {
	
		System.out.println("Side Bump test: " + sideBump);
		long tmp[] = answerSheet
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];

		System.out.println("Test valid:");
		System.out.println(tmp[0]);
		System.out.println(tmp[1]);
		System.out.println(tmp[2]);
		
		System.out.println("curState:");
		System.out.println(curState[0]);
		System.out.println(curState[1]);
		System.out.println(curState[2]);
	
		return ((curState[0] & tmp[0]) | (curState[1] & tmp[1]) | (curState[2] & tmp[2])) == 0L /*&& ! unoccupiedRegionSplit(tmp, sideBump)*/;
		
	}
	
	public void addNewLayerFast(int sideBump, int indexTrail) {
		
		long tmp[] = answerSheet
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
		curState[0] = curState[0] | tmp[0];
		curState[1] = curState[1] | tmp[1];
		curState[2] = curState[2] | tmp[2];
		
		this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail] + 1]
				= newGroundedIndexAbove
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
		this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail] + 1] 
				= newGroundedRotationAbove
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
	
		this.prevSideBumps[indexTrail][currentLayerIndex[indexTrail]] = sideBump;
		
		this.currentLayerIndex[indexTrail]++;
	}
	
	public void removePrevLayerFast(int indexTrail) {
		
		currentLayerIndex[indexTrail]--;
		int sideBumpToCancel  = prevSideBumps[indexTrail][currentLayerIndex[indexTrail]];
		
		
		long tmp[] = answerSheet
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBumpToCancel];
				
		curState[0] = curState[0] ^ tmp[0];
		curState[1] = curState[1] ^ tmp[1];
		curState[2] = curState[2] ^ tmp[2];
	}
	
	
	public void addNew1x1CellFast(int sideBump, int indexTrail) {

		//TODO: actually change state for this?
		// Meh!
		/*
		long tmp[] = answerSheet
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
		curState[0] = curState[0] | tmp[0];
		curState[1] = curState[1] | tmp[1];
		curState[2] = curState[2] | tmp[2];
		*/
		
		this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail] + 1]
				= newGroundedIndexAbove
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
		/*
		this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail] + 1] 
				= newGroundedRotationAbove
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
	
		*/
		this.prevSideBumps[indexTrail][currentLayerIndex[indexTrail]] = sideBump;
		
		layerIndexOf1x1Cell[indexTrail] = this.currentLayerIndex[indexTrail] + 1;

		this.currentLayerIndex[indexTrail]++;
	}
	
	public void removePrev1x1CellFast(int indexTrail) {
		
		currentLayerIndex[indexTrail]--;
		layerIndexOf1x1Cell[indexTrail] = -1;
		
		//TODO: actually change state for this?
		// Meh!
		/*
		int sideBumpToCancel  = prevSideBumps[indexTrail][currentLayerIndex[indexTrail]];
		
		long tmp[] = answerSheet
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBumpToCancel];
				
		curState[0] = curState[0] ^ tmp[0];
		curState[1] = curState[1] ^ tmp[1];
		curState[2] = curState[2] ^ tmp[2];
		*/
	}
	
	//TODO: deal with top/bottom cell later:
	
	//pre: The only cell left is top cell:
	public boolean is1x1CellAbleToBeAddedFast(int indexTrail) {
		
		long tmp[] = answerSheetFor1x1CellAnySideBump
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]];
		
		boolean ret = ((~curState[0] & tmp[0]) | (~curState[1] & tmp[1]) | (~curState[2] & tmp[2])) != 0;

		
		return ret;
	}

	//pre: The only cell left is top cell:
	public boolean is1x1CellAbleToBeAddedForSideBumpFast(int sideBump, int indexTrail) {
		long tmp[] = answerSheetFor1x1Cell
				[this.prevGroundedIndexes[indexTrail][this.currentLayerIndex[indexTrail]]]
				[this.prevGroundedRotations[indexTrail][this.currentLayerIndex[indexTrail]]]
				[sideBump];
		
		return ((~curState[0] & tmp[0]) | (~curState[1] & tmp[1]) | (~curState[2] & tmp[2])) != 0;
	}
	
	private void setupAnswerSheetInBetweenLayers() {
		
		answerSheet = new long[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS][NUM_LONGS_TO_USE];
		newGroundedRotationAbove = new int[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS];
		newGroundedIndexAbove = new int[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS];
		
		preComputedPossiblyEmptyCellsAroundNewLayer = new long[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS][NUM_LONGS_TO_USE];
		preComputedForceRegionSplitIfEmptyAroundNewLayer = new boolean[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS];

		for(int index=0; index<Utils.getTotalArea(this.dimensions); index++) {
			for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
				
				for(int sideBump=0; sideBump<NUM_POSSIBLE_SIDE_BUMPS; sideBump++) {
				
					boolean tmpArray[] = new boolean[Utils.getTotalArea(this.dimensions)];
					
					int leftMostRelativeTopLeftGrounded = sideBump - 6;
					
					if( leftMostRelativeTopLeftGrounded < -3 || leftMostRelativeTopLeftGrounded > 3) {
						
						answerSheet[index][rotation][sideBump] = setImpossibleForAnswerSheet();
						newGroundedIndexAbove[index][rotation][sideBump] = BAD_INDEX;
						newGroundedRotationAbove[index][rotation][sideBump] = BAD_ROTATION;						
						continue;
					}
					
			
					for(int i=0; i<tmpArray.length; i++) {
						tmpArray[i] = false;
					}
	
					Coord2D nextGounded = null;
					
					if(leftMostRelativeTopLeftGrounded<=0) {
						
						Coord2D aboveGroundedTopLeft = tryAttachCellInDir(index, rotation, ABOVE);
			
						tmpArray[aboveGroundedTopLeft.i] = true;
						
						Coord2D cur = aboveGroundedTopLeft;
						//Go to left:
						for(int i=0; i>leftMostRelativeTopLeftGrounded; i--) {
							cur = tryAttachCellInDir(cur.i, cur.j, LEFT);
							tmpArray[cur.i] = true;
						}
						
						nextGounded = cur;
						
						cur = aboveGroundedTopLeft;
						//Go to right:
						for(int i=0; i<leftMostRelativeTopLeftGrounded + 3; i++) {
							
							cur = tryAttachCellInDir(cur.i, cur.j, RIGHT);
							tmpArray[cur.i] = true;
						}
						
					} else {
						
						Coord2D cur = new Coord2D(index, rotation);
						//Go to right until there's a cell above:
						
						for(int i=0; i<leftMostRelativeTopLeftGrounded; i++) {
							cur = tryAttachCellInDir(cur.i, cur.j, RIGHT);
						}
						
						
						Coord2D cellAbove = tryAttachCellInDir(cur.i, cur.j, ABOVE);
						
						nextGounded = cellAbove;
						
						tmpArray[cellAbove.i] = true;
						
						cur = cellAbove;
						//Go to right:
						for(int i=0; i<3; i++) {
							cur = tryAttachCellInDir(cur.i, cur.j, RIGHT);
							tmpArray[cur.i] = true;
						}
						
					}
					
					answerSheet[index][rotation][sideBump] = convertBoolArrayToLongs(tmpArray);
					preComputedPossiblyEmptyCellsAroundNewLayer[index][rotation][sideBump]  = getPossiblyEmptyCellsAroundNewLayer(tmpArray, index, rotation);
					
					newGroundedIndexAbove[index][rotation][sideBump] = nextGounded.i;
					newGroundedRotationAbove[index][rotation][sideBump] = nextGounded.j;
				}
			}
		}
		
	}
	
	private long[] getPossiblyEmptyCellsAroundNewLayer(boolean newLayerArray[], int prevGroundIndex, int prevGroundRotation) {
		
		boolean tmpArray[] = new boolean[newLayerArray.length];
		
		//Get the bool array with the new layer indexes true:
		for(int i=0; i<tmpArray.length; i++) {
			tmpArray[i] = newLayerArray[i];
		}
		
		
		//Set the prev layer's indexes to true:
		Coord2D cur = new Coord2D(prevGroundIndex, prevGroundRotation);
		
		for(int i=0; i<NUM_ROTATIONS; i++) {
			tmpArray[cur.i] = true;
			cur = tryAttachCellInDir(cur.i, cur.j, RIGHT);	
		}
		
		

		//Set output to the cells around the new layer that aren't the new layer and aren't the old layer:
		// This assumes both layers are type 0. (4 cells in a row)
		boolean output[] = new boolean[newLayerArray.length];

		for(int i=0; i<tmpArray.length; i++) {
			output[i] = false;
		}
		
		for(int i=0; i<newLayerArray.length; i++) {
			if(newLayerArray[i]) {
				
				for(int dir=0; dir<NUM_ROTATIONS; dir++) {
					
					cur = tryAttachCellInDir(i, 0, dir);
					
					if(tmpArray[cur.i] == false) {
						output[cur.i] = true;
					}
					
					//cells touching corner to corner are also around new layer:
					for(int dir2=0; dir2<NUM_ROTATIONS; dir2++) {
						
						if(dir2 % 2 == dir % 2) {
							continue;
						}
						Coord2D cur2 = tryAttachCellInDir(cur.i, cur.j, dir2);
						
						if(tmpArray[cur2.i] == false) {
							output[cur2.i] = true;
						}
						
						
					}
					
				}
			}
		}
		
		return convertBoolArrayToLongs(output);
	}
	

	public void setupAnswerSheetFor1x1Cell() {
		
		answerSheetFor1x1Cell = new long[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_SIDE_BUMP_OPTIONS][NUM_LONGS_TO_USE];
		answerSheetFor1x1CellAnySideBump = new long[Utils.getTotalArea(this.dimensions)][NUM_NEIGHBOURS][NUM_LONGS_TO_USE];
		
		for(int index=0; index<Utils.getTotalArea(this.dimensions); index++) {
			for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
				
				boolean tmpArrayForAnySideBump[] = new boolean[Utils.getTotalArea(this.dimensions)];
				
				for(int sideBump=0; sideBump<NUM_POSSIBLE_SIDE_BUMPS; sideBump++) {
					
					
					Coord2D cur = new Coord2D(index, rotation);
					//Go to right until there's a cell above:
			
					int leftMostRelativeTopLeftGrounded = sideBump - 6;
					
					if(leftMostRelativeTopLeftGrounded >= 0 && leftMostRelativeTopLeftGrounded < 4) {
					

						boolean tmpArray[] = new boolean[Utils.getTotalArea(this.dimensions)];
						
						for(int i=0; i<leftMostRelativeTopLeftGrounded; i++) {
				
							cur = tryAttachCellInDir(cur.i, cur.j, RIGHT);
						}
						
						Coord2D cellAbove = tryAttachCellInDir(cur.i, cur.j, ABOVE);
						
						
						tmpArray[cellAbove.i] = true;
						tmpArrayForAnySideBump[cellAbove.i] = true;
						
						answerSheetFor1x1Cell[index][rotation][sideBump] = convertBoolArrayToLongs(tmpArray);
						//return ! this.cellsUsed[cellAbove.i];
			
					} else {
						answerSheetFor1x1Cell[index][rotation][sideBump] = setImpossibleForTopAnswerSheet();
					}
				}
				
				answerSheetFor1x1CellAnySideBump[index][rotation] = convertBoolArrayToLongs(tmpArrayForAnySideBump);
			}
		}
		
	}

	
	private long[] convertBoolArrayToLongs(boolean tmpArray[]) {
		
		//1st entry:
		long ret[] = new long[NUM_LONGS_TO_USE];
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = 0;
		}
		
		for(int i=0; i<tmpArray.length; i++) {
			
			if(tmpArray[i]) {
				int indexArray = i / NUM_BYTES_IN_LONG;
				int bitShift = (NUM_BYTES_IN_LONG - 1) - i - indexArray * NUM_BYTES_IN_LONG;
				
				ret[indexArray] += 1L << bitShift;
			}
		}
		
		
		return ret;
	}
	
	private static long[] setImpossibleForAnswerSheet() {
		
		long ret[] = new long[NUM_LONGS_TO_USE];
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = -1L;
		}
		
		return ret;
	}
	
	private static long[] setImpossibleForTopAnswerSheet() {
		
		long ret[] = new long[NUM_LONGS_TO_USE];
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = 0L;
		}
		
		return ret;
	}

	public static final int ABOVE = 0;
	public static final int RIGHT = 1;
	public static final int BELOW = 2;
	public static final int LEFT = 3;
	
	private Coord2D tryAttachCellInDir(int curIndex, int rotationRelativeFlatMap, int dir) {
		CoordWithRotationAndIndex neighbours[] = this.neighbours[curIndex];
		
		int neighbourIndex = (rotationRelativeFlatMap + dir) % NUM_NEIGHBOURS;
		curIndex = neighbours[neighbourIndex].getIndex();
		rotationRelativeFlatMap = (rotationRelativeFlatMap + neighbours[neighbourIndex].getRot() + NUM_NEIGHBOURS) % NUM_NEIGHBOURS;
		
		return new Coord2D(curIndex, rotationRelativeFlatMap);
	}

	

	
	//TODO: this needs to be fixed!
	//DEBUG PRINT STATE ON OTHER CUBOID:
	public void printCurrentStateOnOtherCuboidsFlatMap() {
		
		CuboidToFoldOnExtendedRotSym toPrint = new CuboidToFoldOnExtendedRotSym(
				this.dimensions[0],
				this.dimensions[1],
				this.dimensions[2],
				false,
				true//Use the answer sheet...
				);
		
		toPrint.initializeNewBottomIndexAndRotation(
				this.prevGroundedIndexes[0][0],
				this.prevGroundedRotations[0][0],
				this.initialSepIfEvenLayers
				);
		
		String labels[] = new String[Utils.getTotalArea(toPrint.dimensions)];
		
		for(int i=0; i<labels.length; i++) {
			labels[i] = null;
		}
		
		//TODO: bottom index...
		//Set the start index:
		
		for(int indexTrail=0; indexTrail<2; indexTrail++) {
			for(int i=0; i<this.currentLayerIndex[indexTrail] + 1 ; i++) {
				//TODO: accomedate i > 26 eventually...
				
				int adjustLabelStart = 0; 
				
				if(indexTrail == 1 && i == 0) {
					//For better or worse, the first layer of the fliped trail is the first layer of the normal trail,
					//so don't label it:
					continue;
				} else if(indexTrail == 1 && isEvenNumberOfLayers(this.getNumCellsToFill())) {
					//If there's an even number of layers, then the 2nd layer on the flipped trail should be labeled 'a'
					adjustLabelStart = 1;
				}
				
				char charToUse;
				if(indexTrail == 1) {
					charToUse = (char)('a' + i%26 - adjustLabelStart);
				} else {
					charToUse = (char)('A' + i%26);
				}
				
				
				int num = i /26;
				String labelToUse = "";
				if(num > 0) {
	
					labelToUse = charToUse + "" + (num-1);
				} else {
					labelToUse = charToUse + "" + charToUse;
				}
				
				labels[this.prevGroundedIndexes[indexTrail][i]] = labelToUse;
				
				if(this.layerIndexOf1x1Cell[indexTrail] < 0 || i < this.layerIndexOf1x1Cell[indexTrail]) {
					//TODO: copy paste code
					Coord2D curStart = new Coord2D(this.prevGroundedIndexes[indexTrail][i], this.prevGroundedRotations[indexTrail][i]);
					
					for(int j=0; j<4 - 1; j++) {
						curStart = this.tryAttachCellInDir(curStart.i, curStart.j, RIGHT);
						labels[curStart.i] = labelToUse;
						
					}
					//END TODO: copy paste code
				
				} else {
					
					if(indexTrail == 0) {
						labels[this.prevGroundedIndexes[indexTrail][i]] = "To";
					} else {
						labels[this.prevGroundedIndexes[indexTrail][i]] = "Bo";
					}
				}
			}
		}
			
		
		
		//TODO:
		/*
		
		int numNullLabels = 0;
		int curTopIndex = -1;
		//Add the top:
		for(int i=0; i<labels.length; i++) {
			if(labels[i] == null) {
				numNullLabels++;
				curTopIndex = i;
			}
		}
		
		if(numNullLabels == 1) {
			labels[curTopIndex] = "To";
		}
		*/

		System.out.println(DataModelViews.getFlatNumberingView(this.dimensions[0],
				this.dimensions[1],
				this.dimensions[2],
				labels));
		
	}
	
	public boolean[][] createResultantNetAsBoolArray() {
		
		boolean ret[][] = null;
		
		int height_Nx1x1 = (this.getNumCellsToFill() - 2) / 4;
		//TODO make sure height_Nx1x1 == curHeightIfUnBuilt if built.
		
		
		int curHeightIfUnBuilt = 2 + this.currentLayerIndex[0] + this.currentLayerIndex[1];
		if( ! this.isEvenNumberOfLayers(this.getNumCellsToFill())) {
			curHeightIfUnBuilt -= 1;
		}
		
		Nx1x1CuboidToFoldAndDrawNet reference = new Nx1x1CuboidToFoldAndDrawNet(curHeightIfUnBuilt);
		
		//Start from the bottom:
		
		//WRONG, but it's a start:
		for(int i=this.currentLayerIndex[1] - 1; i>=0; i--) {

			if(i+1 == this.layerIndexOf1x1Cell[1]) {
				
				//6 - > 3, 7->4, 8 - >5,9->4 
				int bumpToUse = this.prevSideBumps[1][i] - 3;
				
				
				reference.addNextLevel(new Coord2D(0, bumpToUse));
				
			} else {
				reference.addNextLevel(new Coord2D(0, this.prevSideBumps[1][i]));
			}
			//if(this.currentLayerIndex[1])
		}
		for(int i=0; i<this.currentLayerIndex[0]; i++) {
			
			//if(this.currentLayerIndex[1])
			reference.addNextLevel(new Coord2D(0, this.prevSideBumps[0][i]));
		}
		//reference.addNextLevel(neLevelDesc, otherCuboid)
		
		//reference. ??
		
		System.out.println("Current best effort:");
		System.out.println(reference.toString());
		
		return reference.setupBoolArrayNet();
	}

	//END DEBUG PRINT STATE ON OTHER CUBOID:

	public static void main(String args[]) {
		CuboidToFoldOnExtendedRotSym test1 = new CuboidToFoldOnExtendedRotSym(5, 2, 1);
		//CuboidToFoldOnExtendedRotSym test1 = new CuboidToFoldOnExtendedRotSym(5, 3, 1);
		
		test1.initializeNewBottomIndexAndRotation(25, 0, 9);
		
		if( test1.isDudInit()) {
			System.out.println("Dud!");
			return;
		}

		//test1.printCurrentStateOnOtherCuboidsFlatMap();
		
		test1.addNewLayerFast(7, 0);
		
		test1.addNewLayerFast(7, 1);
		
		test1.printCurrentStateOnOtherCuboidsFlatMap();
		
		test1.removePrevLayerFast(1);
		test1.addNewLayerFast(7, 1);
		
		if(test1.is1x1CellAbleToBeAddedFast(0)) {
			
			if(test1.is1x1CellAbleToBeAddedForSideBumpFast(6, 0)) {
				test1.addNew1x1CellFast(6, 0);
			}
			
		}
		
		test1.addNew1x1CellFast(6, 1);
		
		test1.printCurrentStateOnOtherCuboidsFlatMap();
		
		System.out.println("Done!");
		
		boolean tmp[][] = test1.createResultantNetAsBoolArray();
		
		//tmp matches the toString function: (that's good!)
		/*
		for(int i=0; i<tmp.length; i++) {
			for(int j=0; j<tmp[0].length; j++) {
				if(tmp[i][j]) {
					System.out.print("#");
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
		*/
		
		boolean isUnique = BasicUniqueCheckImproved.isUnique(tmp);
		
		if(isUnique) {
			System.out.println("First check is unique");
		} else {
			System.out.println("Doh! Is unique");
			System.exit(1);
			
		}
		
		boolean isUnique2 = BasicUniqueCheckImproved.isUnique(tmp);
		
		//TODO: use C:\Users\Michael\projectEuler2\CuboidSimplePhaseNetSearch\src\OutputReader\CheckSolutionsCodesAgainstOtherCuboid2.java
		// to check the solutions against another way to verify...
		//That file looks for the key text: 'solution code: [NUMBER]'"
		System.out.println("solution code: " + BasicUniqueCheckImproved.debugLastScore);
		
		if(isUnique2) {
			System.out.println("Doh! Second check is unique");
			System.exit(1);
		} else {
			System.out.println("Second check is not unique.");
			
		}
	}
}
