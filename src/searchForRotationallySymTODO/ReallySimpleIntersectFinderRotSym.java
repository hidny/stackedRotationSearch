package searchForRotationallySymTODO;

import java.util.ArrayList;

import Coord.Coord2D;
import DupRemover.BasicUniqueCheckImproved;
import GraphUtils.PivotCellDescription;
import GraphUtils.PivotCellDescriptionForSimplePhase;
import Model.Utils;
import NewModel.firstIteration.Nx1x1CuboidToFoldAndDrawNet;
//import NewModelWithIntersection.fastRegionCheck.FastRegionCheck;
import SolutionResolver.SolutionResolverInterface;
import SolutionResolver.StandardResolverForSmallIntersectSolutions;

public class ReallySimpleIntersectFinderRotSym {


	public static long rotSymSolutions = 0;
	
	public static void main(String[] args) {
		
		//reallySimpleSearch(2, 1, 1);
		
		//N: 5
		//38460 solutions (Makes sense because it's Nx1x1) (9702 unique solutions)
		//reallySimpleSearch(5, 1, 1);
		
		//reallySimpleSearch(4, 1, 1);
		
		//26 solutions: (7 unique solutions) (good)
		//rotationally symmetric solutions found: 1
		//reallySimpleSearch(3, 2, 1);

		//N: 7
		//6 solutions: (2 unique solutions) (good)
		//rotationally symmetric solutions found: 1
		//reallySimpleSearch(3, 3, 1);
		

		//N: 8
		//404 solutions: (109 unique solutions)
		//rotationally symmetric solutions found: 16
		//reallySimpleSearch(5, 2, 1);
		

		//N: 9
		//42 solutions: (12 unique solutions)
		//rotationally symmetric solutions found: 3
		//reallySimpleSearch(4, 3, 1);
		

		//N: 10
		/*
		 * Looks like I had a typographical error for a long time. This number used to be 113, but after
		 * Landon correctly stated that there are 133 solutions, I
		 * reran this program without changes, and got 133 unique solutions...
		 * "Done
Found 498 different solutions if we ignore symmetric solutions

Done using the 2nd iteration (using pre-computed long arrays)
Found 133 unique solution."
*/
		//498 solutions: (133 unique solution)
		//rotationally symmetric solutions found: 17
		//reallySimpleSearch(3, 3, 2);
		
		
		//N: 11
		//2364 solutions: (591 unique solutions)
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(3, 5, 1);
		
		//74 solutions (19 unique solutions)
		//rotationally symmetric solutions found: 1
		//reallySimpleSearch(7, 2, 1);
		
		
		//N: 13
		//680 solutions: (10 unique solutions)
		//rotationally symmetric solutions found: 10
		//reallySimpleSearch(3, 3, 3);

		//20 solutions: (6 unique solutions)
		//rotationally symmetric solutions found: 2
		//reallySimpleSearch(6, 3, 1);

		//N: 14
		//16504 solutions (That's promising!) (4182 unique solutions)
		//rotationally symmetric solutions found: 112
		//reallySimpleSearch(5, 4, 1);

		//564 solutions (152 unique solutions)
		//rotationally symmetric solutions found: 22
		//reallySimpleSearch(9, 2, 1);

		//N:15
		//722 solutions (184 unique solutions)
		//rotationally symmetric solutions found: 7
		//reallySimpleSearch(5, 3, 2);

		//36 solutions (9 unique solutions)
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(7, 3, 1);


		//N:16
		//3724 solutions (1285 unique solutions)
		//rotationally symmetric solutions found: 77
		//reallySimpleSearch(4, 3, 3);
		
		
		//N: 17
		// 115268 solutions (28817 uniq solutions) (This took 40 seconds)
		// rotationally symmetric solutions found: 0
		//reallySimpleSearch(5, 5, 1);
		
		// 60 solutions (17 unique) (7 minutes)
		//rotationally symmetric solutions found: 4
		//reallySimpleSearch(8, 3, 1);
		
		
		//114 solutions (29 unique)
		//rotationally symmetric solutions found: 1
		//reallySimpleSearch(11, 2, 1);
		
		//N: 19 (No luck)
		// 8418 unique solution.
		//rotationally symmetric solutions found: 81
		//reallySimpleSearch(5, 3, 3);
		
		
		// 27 unique solutions (under 80 seconds)
		//rotationally symmetric solutions found: 3
		//reallySimpleSearch(7, 4, 1);
		
		
		//951 unique solution.
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(9, 3, 1);
		
		
		//N: 20
		// Found 202106 unique solution.
		// rotationally symmetric solutions found: 784
		//reallySimpleSearch(6, 5, 1);
		
		// 296 unique solutions
		// rotationally symmetric solutions found: 10
		//reallySimpleSearch(7, 3, 2);
		
		//Found 798 non-unique solutions and 211 unique solution.
		//rotationally symmetric solutions found: 23
		//reallySimpleSearch(13, 2, 1);
		
		//N = 21
		//Found 11 unique solution.
		//rotationally symmetric solutions found: 4
		//reallySimpleSearch(10, 3, 1);

		// N = 22
		//{5, 5, 2}, {6, 3, 3}
		// 24 uniq solutions
		//rotationally symmetric solutions found: 4
		//reallySimpleSearch(5, 5, 2);
		

		//Found 58891 unique solution.
		//rotationally symmetric solutions found: 542
		//reallySimpleSearch(6, 3, 3);
		
		// N = 23 (4 other ones...)
		/*
		 * 5 x 4 x 3: 94
7 x 5 x 1: 94
11 x 3 x 1: 94
15 x 2 x 1: 94
		 */
		
		
		//5, 4, 3 118 different solutions and 61 unique solution.
		//rotationally symmetric solutions found: 5
		//reallySimpleSearch(5, 4, 3);

		 //7, 5, 1
		//Found 1411798 unique solution.
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(7, 5, 1);
		
		 //11,3,1: (took about 20 hours)51 different solutions and  15 unique solution. (Latest change: less than 2 hours)
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(11, 3, 1);
		
		// Found 28 unique solution.
		// rotationally symmetric solutions found: 0 (0 with a dimension that's even!)
		//reallySimpleSearch(15, 2, 1);

		//N=24:
		//Found 6853 unique solution.
		//rotationally symmetric solutions found: 160
		//reallySimpleSearch(9, 4, 1);
		

		
		// N = 25
		/*
		 * 7 x 3 x 3: 102
9 x 3 x 2: 102
12 x 3 x 1: 102
		 */

		//Found 410329 unique solution.
		//rotationally symmetric solutions found: 522
		//reallySimpleSearch(7, 3, 3);

		//Found 64 unique solution.
		//rotationally symmetric solutions found: 9
		//reallySimpleSearch(9, 3, 2);


		//28 unique solution.
		//rotationally symmetric solutions found: 7
		//reallySimpleSearch(12, 3, 1);

		// N = 26
		//268 unique solution for 17x2x1 (just over 16 minutes)
		//rotationally symmetric solutions found: 25
		//reallySimpleSearch(17, 2, 1);
		

		//9,885,286 uniq solutions (and about 9,885,263 unique solutions after searching the cell left of 5x1 side)
		//rotationally symmetric solutions found: 5488
		//reallySimpleSearch(8, 5, 1);
		
		// N = 27
		//1310 unique solution
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(13, 3, 1);
		
		//Found 154 unique solution.
		//rotationally symmetric solutions found: 4
		//reallySimpleSearch(5, 5, 3);
		
		//Found 50 unique solution.
		//rotationally symmetric solutions found: 3
		//reallySimpleSearch(7, 6, 1);
		
		//N = 28

		//Found 2870327 unique solution.
		//rotationally symmetric solutions found: 3662
		//reallySimpleSearch(8, 3, 3);
		
		// N = 29 (5 other ones...)
		//Found 165 unique solution.
		//rotationally symmetric solutions found: 4
		//reallySimpleSearch(7, 5, 2);
		
		//Found 36 unique solution.
		//This one is slow!
		//TODO: you might have to do the region split logic after all!
		//rotationally symmetric solutions found: 0
		//reallySimpleSearch(11, 4, 1);
		
		
		// N =30:
		//rotationally symmetric solutions found: 67
		//reallySimpleSearch(7, 4, 3);
		
		//rotationally symmetric solutions found: 10
		//reallySimpleSearch(11, 3, 2);
		
		//Oh crap!
		//My conjecture is broken!
		//rotationally symmetric solutions found: 2
		reallySimpleSearch(5, 5, 5);
		
		
		//reallySimpleSearch(9, 3, 2);
		
		System.out.println("Number of rotationally symmetric solutions found: " + rotSymSolutions);
	}
	
	public static SolutionResolverInterface solutionResolver;

	public static void reallySimpleSearch(int a, int b, int c) {

		rotSymSolutions = 0;
		
		BasicUniqueCheckImproved.resetUniqList();
		solutionResolver = new StandardResolverForSmallIntersectSolutions();
		
		
		CuboidToFoldOnExtendedRotSym cuboidToBuild = new CuboidToFoldOnExtendedRotSym(a, b, c);
		
		if(cuboidToBuild.getNumCellsToFill() % 4 != 2) {
			System.out.println("ERROR: trying to find intersect between Nx1x1 solution and a cuboid solution that doesn't have a surface area that matches any Nx1x1 cuboid.");
			return;
		}
		
		int NofNx1x1Cuboid = getNumLayers(cuboidToBuild);
		

		ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBuild);
		
		long ret = 0;
		
		//FastRegionCheck fastRegionCheckSetup = cuboidToBuild.getFastRegionCheck();
		
		for(int i=0; i<startingPointsAndRotationsToCheck.size(); i++) {
			
			int otherCuboidStartIndex =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			int otherCuboidStartRotation = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
			
			System.out.println("Start recursion for other cuboid start index and rotation: (" + otherCuboidStartIndex + ", " + otherCuboidStartRotation + ")");
			
			System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			
			cuboidToBuild = new CuboidToFoldOnExtendedRotSym(a, b, c/*, fastRegionCheckSetup*/);
			
			int rangeSideBumps[] = null;
			int firstLayerIdnex = 0;
			
			if(CuboidToFoldOnExtendedRotSym.isEvenNumberOfLayers(cuboidToBuild.getNumCellsToFill())) {
				rangeSideBumps = new int[] {3, 4, 5, 6, 7, 8, 9};
				firstLayerIdnex = 1;
				
			} else {
				rangeSideBumps = new int[] {-1};
			}
			
			for(int j=0; j<rangeSideBumps.length; j++) {
				cuboidToBuild.initializeNewBottomIndexAndRotation(otherCuboidStartIndex, otherCuboidStartRotation, rangeSideBumps[j]);
				
				/*if(fastRegionCheckSetup == null) {
					System.out.println("oops");
					System.exit(1);
				}*/
				
				if( ! cuboidToBuild.isDudInit()) { 
					ret += findReallySimpleSolutionsRecursion(cuboidToBuild, firstLayerIdnex);
				
					System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + otherCuboidStartIndex + " and a rotation index of " + otherCuboidStartRotation +".");
					System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
				} else {
					System.out.println("DUD");
					//System.exit(1);
				}
				
			}
			
			
		}
		System.out.println("Done");
		System.out.println("Found " + ret + " different solutions if we ignore symmetric solutions");
		System.out.println();
		System.out.println("Done using the 2nd iteration (using pre-computed long arrays)");
		System.out.println("Found " + BasicUniqueCheckImproved.uniqList.size() + " unique solution.");

		System.out.println("Done for " + a + "x" + b + "x" + c);
	}
	
	public static int getNumLayers(CuboidToFoldOnExtendedRotSym cuboidToBuild) {
		return (cuboidToBuild.getNumCellsToFill() - 2) / 4;
	}
	
	public static long findReallySimpleSolutionsRecursion(CuboidToFoldOnExtendedRotSym cuboidToBuild, int firstLayerIdnex) {
		return findReallySimpleSolutionsRecursion(cuboidToBuild, firstLayerIdnex, getNumLayers(cuboidToBuild), 0);
	}
	
	public static final long DEBUG_MODULO =100000L;
	public static long debug = 0;

	
	public static long findReallySimpleSolutionsRecursion(
			CuboidToFoldOnExtendedRotSym cuboidToBuild,
			int layerIndex,
			int numLayers,
			int indexTrail
		) {

		long ret = 0;
		if(debug % DEBUG_MODULO == 1000000) {
			cuboidToBuild.printCurrentStateOnOtherCuboidsFlatMap();
		}
		debug++;
		
		if(layerIndex >= numLayers - 1) {
			
			if(cuboidToBuild.is1x1CellAbleToBeAddedFast(indexTrail)) {

				for(int sideBump=6; sideBump <10; sideBump++) {
					if(cuboidToBuild.is1x1CellAbleToBeAddedForSideBumpFast(sideBump, indexTrail)) {
						
						if(layerIndex == numLayers) {


							cuboidToBuild.addNew1x1CellFast(sideBump, indexTrail);
							
							//Hacky way to check that the top cell and bottom cell are different:
							if(cuboidToBuild.topAndBottomDontOccupySameSpace()) {
								
								ret++;
								
								//TODO: maybe also cut the margins, so you don't have to do it again?
								boolean boolArrayAnswer[][] = cuboidToBuild.createResultantNetAsBoolArray();
								
								if(BasicUniqueCheckImproved.isUnique(cuboidToBuild.createResultantNetAsBoolArray()) ){
									
									cuboidToBuild.createResultantNetAsBoolArray(true);
									
									System.out.println("Unique solution found");
									System.out.println("Num unique solutions found: " + BasicUniqueCheckImproved.uniqList.size());
									
									
									
									cuboidToBuild.printCurrentStateOnOtherCuboidsFlatMap();
									System.out.println("Solution code: " + BasicUniqueCheckImproved.debugLastScore);
									

									if(Utils.isRotSym(boolArrayAnswer)) {
										rotSymSolutions++;
										System.out.println("Found rotationally symmetric net!");
									}
								}
	
								if(ret > 0) {
									System.out.println("Found " + ret + " places for top from this net:");
									
									System.out.println("----");
								}
							}
							
							cuboidToBuild.removePrev1x1CellFast(indexTrail);
							
						} else {
							
							cuboidToBuild.addNew1x1CellFast(sideBump, indexTrail);
							ret += findReallySimpleSolutionsRecursion(cuboidToBuild, layerIndex + 1, numLayers, indexTrail ^ 1);
							cuboidToBuild.removePrev1x1CellFast(indexTrail);
						}
						
					}
				}
				
				
			}
			
			return ret;
		}
		
		for(int sideBump=3; sideBump <10; sideBump++) {
			
			//TODO: Focus on Rot sym
			if(indexTrail == 1 
					//&& Math.abs(sideBump - cuboidToBuild.getLastAdded(0)) > 1
					&& Math.abs(sideBump - cuboidToBuild.getLastAdded(0)) > 0
				) {
				//System.out.println("Latest: " + cuboidToBuild.getLastAdded(0));
				continue;
			}
			//System.out.println("TEST sideBump: " + sideBump);
			//System.out.println("TEST: " + indexTrail + ": " + cuboidToBuild.currentLayerIndex[indexTrail]);
			
			if(cuboidToBuild.isNewLayerValidSimpleFast(sideBump, indexTrail)) {
				cuboidToBuild.addNewLayerFast(sideBump, indexTrail);

				//if(layerIndex == 1) {
				//	System.out.println("MICHAEL TEST!");
				//	cuboidToBuild.printCurrentStateOnOtherCuboidsFlatMap();
				//}
				
				ret += findReallySimpleSolutionsRecursion(cuboidToBuild, layerIndex + 1, numLayers, indexTrail ^ 1);

				cuboidToBuild.removePrevLayerFast(indexTrail);
			}
		}
		
		return ret;
	}
}
