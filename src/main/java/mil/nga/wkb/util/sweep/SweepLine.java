package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Polygon rings
	 */
	private List<LineString> rings;

	/**
	 * Tree of segments sorted by above-below order
	 */
	private List<Segment> tree = new ArrayList<>();

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

		Segment segment = new Segment();
		segment.setEdge(event.getEdge());
		segment.setRing(event.getRing());

		LineString ring = rings.get(segment.getRing());
		List<Point> points = ring.getPoints();

		Point point1 = points.get(segment.getEdge());
		Point point2 = points.get((segment.getEdge() + 1) % points.size());
		if (xyOrder(point1, point2) < 0) {
			segment.setLeftPoint(point1);
			segment.setRightPoint(point2);
		} else {
			segment.setRightPoint(point1);
			segment.setLeftPoint(point2);
		}

		int index = add(segment, event.getPoint().getX());

		Segment next = getNext(index);
		Segment previous = getPrevious(index);
		if (next != null) {
			segment.setAbove(next);
			next.setBelow(segment);
		}
		if (previous != null) {
			segment.setBelow(previous);
			previous.setAbove(segment);
		}

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
	 * @param event
	 *            event
	 */
	public void remove(Segment segment, Event event) {

		int index = search(segment, event.getPoint().getX());
		if (index >= 0) {
			Segment next = getNext(index);
			if (next != null) {
				next.setBelow(segment.getBelow());
			}
			Segment previous = getPrevious(index);
			if (previous != null) {
				previous.setAbove(segment.getAbove());
			}
			tree.remove(index);
			segments.get(segment.getRing()).remove(segment.getEdge());
		}

	}

	/**
	 * Add the segment to the sweep tree
	 * 
	 * @param segment
	 *            segment
	 * @param x
	 *            current point x value
	 * @return index where added
	 */
	private int add(Segment segment, double x) {

		int index = search(segment, x);
		if (index < 0) {
			index = -index - 1;
		}

		tree.add(index, segment);
		Map<Integer, Segment> edgeMap = segments.get(segment.getRing());
		if (edgeMap == null) {
			edgeMap = new HashMap<>();
			segments.put(segment.getRing(), edgeMap);
		}
		edgeMap.put(segment.getEdge(), segment);

		return index;
	}

	/**
	 * Search the sweep line tree for the segment or insert location
	 * 
	 * @param segment
	 *            segment
	 * @param x
	 *            current point x value
	 * @return binary search result, index if found, (-(insertion point) - 1) if
	 *         not
	 */
	private int search(Segment segment, final double x) {

		Comparator<Segment> comparator = new Comparator<Segment>() {
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
		};

		int index = Collections.binarySearch(tree, segment, comparator);

		return index;
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
	 * Get the segment at the index, null if one does not exist
	 * 
	 * @param index
	 *            index location
	 * @return segment, null if none
	 */
	private Segment get(int index) {
		Segment segment = null;
		if (index >= 0 && index < tree.size()) {
			segment = tree.get(index);
		}
		return segment;
	}

	/**
	 * Get the segment at the previous location from the index
	 * 
	 * @param index
	 *            index location
	 * @return previous segment, or null
	 */
	private Segment getPrevious(int index) {
		return get(index - 1);
	}

	/**
	 * Get the segment at the next location from the index
	 * 
	 * @param index
	 *            index location
	 * @return next segment, or null
	 */
	private Segment getNext(int index) {
		return get(index + 1);
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
