import java.util.Comparator;
import java.awt.geom.Point2D;
/**
 * enables comparisons 
 * @author sterw
 ***
 * 
 * @author Nikolo and Sterling
 *
 
 *
 */
public class Point2DComparator implements Comparator<Point2D> {
	@Override
	public int compare(Point2D arg0, Point2D arg1) {
		if (arg0.getX() > arg1.getX()) {
			return 1;

		} else if (arg0.getX() < arg1.getX()) {
			return -1;
		}
		return 0;
	}

}
