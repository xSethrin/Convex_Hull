AUTHOR: 
	Nikolo Sperberg and Sterling Rohlinger

VERSION:
	1.0

CONTENTS: 
	ConvexHullFinder.java
	ConvexHullGUI.java
	ConvexHullPanel.java
	MergeHull.java
	Pount2DComparator.java
	

DISCRIPTION: 
	This program prompts a user for a number to generate points. Then,
	the user can generate a convex hull based on these points
	
COMPILE AND RUN:
	To run this program, you first must compile the .java files.  
	Use command prompt and type the following: javac *.java
	This will compile the code.
	To run the code, use the command prompt again. Type: java ConvexHullGUI
	
OTHER:
	We encountered issues with properly generating tangent lines.  We 
	discovered that this issue came from improperly creating list of points.
	Our lists had duplicate points, so we wrote a method to remove these points.
	We have also observed that sometimes our code does not create convex hull.
	We are unable to determine why, though this issue happens rarely and 
	only with large numbers.  If you encounter this issue, please reload the 
	GUI and try again.

