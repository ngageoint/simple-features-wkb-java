package mil.nga.wkb.util.sweep;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;

/**
 * Sweep Line algorithm
 * 
 * @author osbornb
 * @since 1.0.5
 */
public class SweepLine {

	/**
	 * Segment comparator for adding segments to the sweep line in above-below
	 * order
	 */
	private class SegmentComparator implements Comparator<Segment> {

		/**
		 * Current sweep x value
		 */
		private double x;

		/**
		 * Set the current sweep x value
		 * 
		 * @param x
		 *            x value
		 */
		public void setX(double x) {
			this.x = x;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Segment segment1, Segment segment2) {

			double y1 = yValueAtX(segment1, x);
			double y2 = yValueAtX(segment2, x);

			int compare;
			if (y1 < y2) {
				compare = -1;
			} else if (y2 < y1) {
				compare = 1;
			} else if (segment1.getRing() < segment2.getRing()) {
				compare = -1;
			} else if (segment2.getRing() < segment1.getRing()) {
				compare = 1;
			} else if (segment1.getEdge() < segment2.getEdge()) {
				compare = -1;
			} else if (segment2.getEdge() < segment1.getEdge()) {
				compare = 1;
			} else {
				compare = 0;
			}

			return compare;
		}

	}

	/**
	 * Polygon rings
	 */
	private List<LineString> rings;

	/**
	 * Comparator for ordering segments in above-below order
	 */
	private SegmentComparator comparator = new SegmentComparator();

	/**
	 * Tree of segments sorted by above-below order
	 */
	private TreeSet<Segment> tree = new TreeSet<>(comparator);

	/**
	 * Mapping between ring, edges, and segments
	 */
	private Map<Integer, Map<Integer, Segment>> segments = new HashMap<>();

	/**
	 * Constructor
	 * 
	 * @param rings
	 *            polygon rings
	 */
	public SweepLine(List<LineString> rings) {
		this.rings = rings;
	}

	/**
	 * Add the event to the sweep line
	 * 
	 * @param event
	 *            event
	 * @return added segment
	 */
	public Segment add(Event event) {

		Segment segment = createSegment(event);

		// Add to the tree
		comparator.setX(event.getPoint().getX());
		tree.add(segment);

		// Update the above and below pointers
		Segment next = tree.higher(segment);
		Segment previous = tree.lower(segment);
		if (next != null) {
			segment.setAbove(next);
			next.setBelow(segment);
		}
		if (previous != null) {
			segment.setBelow(previous);
			previous.setAbove(segment);
		}

		// Add to the segments map
		Map<Integer, Segment> edgeMap = segments.get(segment.getRing());
		if (edgeMap == null) {
			edgeMap = new HashMap<>();
			segments.put(segment.getRing(), edgeMap);
		}
		edgeMap.put(segment.getEdge(), segment);

		return segment;
	}

	/**
	 * Create a segment from the event
	 * 
	 * @param event
	 *            event
	 * @return segment
	 */
	private Segment createSegment(Event event) {

		int edgeNumber = event.getEdge();
		int ringNumber = event.getRing();

		LineString ring = rings.get(ringNumber);
		List<Point> points = ring.getPoints();

		Point point1 = points.get(edgeNumber);
		Point point2 = points.get((edgeNumber + 1) % points.size());

		Point left = null;
		Point right = null;
		if (xyOrder(point1, point2) < 0) {
			left = point1;
			right = point2;
		} else {
			right = point1;
			left = point2;
		}

		Segment segment = new Segment(edgeNumber, ringNumber, left, right);

		return segment;
	}

	/**
	 * Find the existing event segment
	 * 
	 * @param event
	 *            event
	 * @return segment
	 */
	public Segment find(Event event) {
		return segments.get(event.getRing()).get(event.getEdge());
	}

	/**
	 * Determine if the two segments intersect
	 * 
	 * @param segment1
	 *            segment 1
	 * @param segment2
	 *            segment 2
	 * @return true if intersection, false if not
	 */
	public boolean intersect(Segment segment1, Segment segment2) {

		boolean intersect = false;

		if (segment1 != null && segment2 != null) {

			int ring1 = segment1.getRing();
			int ring2 = segment2.getRing();

			boolean consecutive = ring1 == ring2;
			if (consecutive) {
				int edge1 = segment1.getEdge();
				int edge2 = segment2.getEdge();
				int ringPoints = rings.get(ring1).numPoints();
				consecutive = (edge1 + 1) % ringPoints == edge2
						|| edge1 == (edge2 + 1) % ringPoints;
			}

			if (!consecutive) {

				double left = isLeft(segment1, segment2.getLeftPoint());
				double right = isLeft(segment1, segment2.getRightPoint());

				if (left * right <= 0) {

					left = isLeft(segment2, segment1.getLeftPoint());
					right = isLeft(segment2, segment1.getRightPoint());

					if (left * right <= 0) {
						intersect = true;
					}
				}
			}
		}

		return intersect;
	}

	/**
	 * Remove the segment from the sweep line
	 * 
	 * @param segment
	 *            segment
	 */
	public void remove(Segment segment) {

		if (tree.remove(segment)) {

			Segment above = segment.getAbove();
			Segment below = segment.getBelow();
			if (above != null) {
				above.setBelow(below);
			}
			if (below != null) {
				below.setAbove(above);
			}

			segments.get(segment.getRing()).remove(segment.getEdge());
		}

	}

	/**
	 * Get the segment y value at the x location by calculating the line slope
	 * 
	 * @param segment
	 *            segment
	 * @param x
	 *            current point x value
	 * @return segment y value
	 */
	private double yValueAtX(Segment segment, double x) {

		Point left = segment.getLeftPoint();
		Point right = segment.getRightPoint();

		double m = (right.getY() - left.getY()) / (right.getX() - left.getX());
		double b = left.getY() - (m * left.getX());

		double y = (m * x) + b;

		return y;
	}

	/**
	 * XY order of two points
	 * 
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @return +1 if p1 > p2, -1 if p1 < p2, 0 if equal
	 */
	public static int xyOrder(Point point1, Point point2) {
		int value = 0;
		if (point1.getX() > point2.getX()) {
			value = 1;
		} else if (point1.getX() < point2.getX()) {
			value = -1;
		} else if (point1.getY() > point2.getY()) {
			value = 1;
		} else if (point1.getY() < point2.getY()) {
			value = -1;
		}
		return value;
	}

	/**
	 * Check where the point is (left, on, right) relative to the line segment
	 * 
	 * @param segment
	 *            segment
	 * @param point
	 *            point
	 * @return > 0 if left, 0 if on, < 0 if right
	 */
	public static double isLeft(Segment segment, Point point) {
		return isLeft(segment.getLeftPoint(), segment.getRightPoint(), point);
	}

	/**
	 * Check where point 2 is (left, on, right) relative to the line from point
	 * 
	 * @param point0
	 *            point 0
	 * @param point1
	 *            point 1
	 * @param point2
	 *            point 2
	 * @return > 0 if left, 0 if on, < 0 if right
	 */
	public static double isLeft(Point point0, Point point1, Point point2) {
		return (point1.getX() - point0.getX())
				* (point2.getY() - point0.getY())
				- (point2.getX() - point0.getX())
				* (point1.getY() - point0.getY());
	}

}
