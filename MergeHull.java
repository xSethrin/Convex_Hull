
import java.util.*;
import java.awt.geom.*;

/**
 * This class will find the convex hull of a list of Point2D points using the
 * divide and conquer strategy
 * 
 * @author Nikolo Sperberg and Sterling Rohlinger Date: 11-10-2019 Version: 2
 */

/*
 * This entire algorithm assumes that no two points line on the same vertical
 * line That is, no two points have the same x value
 */

public class MergeHull implements ConvexHullFinder {
	public MergeHull() {
		super();
	}

	/*
	 * The originalPoints is a Vector of Point2Ds The return Vector is a list of
	 * Point2Ds, a subset of those in originalPoints, that represent the convex
	 * hull. These points are in ccw order.
	 */
	public List<Point2D> computeHull(List<Point2D> originalPoints) {

		List<Point2D> points = new ArrayList<Point2D>(originalPoints);// Make a copy of the points so we don't destroy
																		// the original
		Collections.sort(points, new Point2DComparator());// sort all points by x value
		if (points.size() == 1) {// recurse until we have a single point- base case
			return points;// return single point
		}
		List<Point2D> leftHalf = computeHull(points.subList(0, (points.size() / 2)));// compute left half hull
		List<Point2D> rightHalf = computeHull(points.subList((points.size() / 2), points.size()));// compute right half
																									// hull
		return recursiveMergeHull(leftHalf, rightHalf);// merge time!
	}

	/**
	 * This method merges two lists
	 * 
	 * @param leftHalf
	 *            the points that would be left of your dividing line
	 * @param rightHalf
	 *            the points that would be right of your dividing line
	 * @return a Point2D arrayList containing the convexHull for the points passed
	 *         in.
	 */
	private List<Point2D> recursiveMergeHull(List<Point2D> leftHalf, List<Point2D> rightHalf) {
		Line2D upperTangent = walkTangent(0, (leftHalf.size() - 1), rightHalf, leftHalf);// get upper tangent
		Line2D lowerTangent = walkTangent((leftHalf.size() - 1), 0, leftHalf, rightHalf);// get lower tangent
		List<Point2D> points = new ArrayList<Point2D>();// create new array
		if (leftHalf.size() == 1 && rightHalf.size() == 1) {// if only single points being merged
			points.add(leftHalf.get(0));// add left side
			points.add(rightHalf.get(0));// add right side

		} else {
			points.add(lowerTangent.getP1());// add 1st point in new list
			points.add(lowerTangent.getP2());// add second point to the new list
			points = makeLine(points, rightHalf, lowerTangent.getP2(), upperTangent.getP1());// get points from right
																								// side
			points = makeLine(points, leftHalf, upperTangent.getP2(), lowerTangent.getP1());// get points from left side

		}
		return points;// return merged list
	}

	/**
	 * This method will search an ArrayList for the point you pass it.
	 * 
	 * @param goal
	 *            the point you are looking for in the list
	 * @param list
	 *            the list of points to search
	 * @return the position(as an int) of the goal point in the list of points.
	 */
	public int findPos(Point2D goal, List<Point2D> list) {
		for (int i = 0; i < list.size(); i++) {// loop though list
			if (list.get(i).equals(goal)) {// if find goal point
				return i++;// return i
			}
		}
		return 0;
	}

	/**
	 * 
	 * This method will add points between the tangent lines to the hull
	 * 
	 * @param points
	 *            a list of points that are on the convex hull
	 * @param toMerge
	 *            the list of points in one of the sublist of the merging sets
	 * @param start
	 *            the point you are starting from
	 * @param end
	 *            the point you want to add to
	 * @return a list of points that is the convex hull
	 */
	public List<Point2D> makeLine(List<Point2D> points, List<Point2D> toMerge, Point2D start, Point2D end) {
		int pos = findPos(start, toMerge);// find start point
		while (!toMerge.get(pos).equals(end)) {// while not at goal
			points.add(toMerge.get(pos));// add point to the to merge list
			pos++;// Increment pos
			if (pos >= toMerge.size()) {
				pos = 0;
			}
		}
		points.add(toMerge.get(pos));// add point to merge
		points = removeDuplicates(points);// remove all duplicates
		return points;// return list
	}

	/**
	 * this method removes all duplicate points from the point list.
	 * 
	 * @param points
	 *            the list of points making the convex hull
	 * @return a list of points that does not contain duplicates
	 */
	public List<Point2D> removeDuplicates(List<Point2D> points) {
		List<Point2D> noDuplicates = new ArrayList<Point2D>();// create new list to be returned
		boolean flag = true;// flag used to prvent adding duplicates
		for (int i = 0; i < points.size(); i++) {// loop through points
			for (int j = 0; j < noDuplicates.size(); j++) {// loop through no duplicates
				if (points.get(i).equals(noDuplicates.get(j))) {// check if two points are the same
					flag = false;// set to false if the same
					j = noDuplicates.size() + 1;// break loop
				}
			}
			if (flag) {// if no duplicate was found
				noDuplicates.add(points.get(i));// add point to new list
			}
			flag = true;// reset flag
		}
		return noDuplicates;// return new list with no duplicates
	}

	/*
	 * This code was given to us by Professor Amthauer
	 * 
	 * Returns true if the given tangent line is the lower tangent to the hull. The
	 * pointIndex of where the lowerTangent connects to the hull is given because it
	 * is unknown which end of the tangent line is connected to the hull (one could
	 * figure it out, but that would require work). A tangent is a lower tangent if
	 * both the neighboring points are higher (ccw) to the tangent. Note that this
	 * method can also be used to find the upper tangent by reversing the direction
	 * of the tangent line.
	 */
	private boolean isCWTangentAtPoint(Line2D tangent, List<Point2D> hull, int pointIndex) {

		return ((tangent.relativeCCW(hull.get((pointIndex + hull.size() - 1) % hull.size())) >= 0)
				&& (tangent.relativeCCW(hull.get((pointIndex + 1) % hull.size())) >= 0));

	}

	/*
	 * This code was given to us by Professor Amthauer
	 * 
	 * This takes the initial tangent line indices and the two hulls and walks It
	 * walks the tangent down so it is a lower tangent Can be used to find the upper
	 * tangent by reversing the input tangent line and hulls
	 */

	private Line2D walkTangent(int leftTangentPointIndex, int rightTangentPointIndex, List<Point2D> leftHull,
			List<Point2D> rightHull) {
	
		Line2D tangentLine = new Line2D.Double(leftHull.get(leftTangentPointIndex),
				rightHull.get(rightTangentPointIndex));

		while (!(isCWTangentAtPoint(tangentLine, leftHull, leftTangentPointIndex)
				&& isCWTangentAtPoint(tangentLine, rightHull, rightTangentPointIndex))) {

			// Walk the left point lower
			while (!(isCWTangentAtPoint(tangentLine, leftHull, leftTangentPointIndex))) {
				leftTangentPointIndex = (leftTangentPointIndex + leftHull.size() - 1) % leftHull.size();
				tangentLine = new Line2D.Double(leftHull.get(leftTangentPointIndex),
						rightHull.get(rightTangentPointIndex));
			}

			// Walk the right point lower
			while (!(isCWTangentAtPoint(tangentLine, rightHull, rightTangentPointIndex))) {
				rightTangentPointIndex = (rightTangentPointIndex + 1) % rightHull.size();
				tangentLine = new Line2D.Double(leftHull.get(leftTangentPointIndex),
						rightHull.get(rightTangentPointIndex));
			}

		}

		return tangentLine;
	}

}
