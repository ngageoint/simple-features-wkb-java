package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.List;

import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.util.GeometryUtils;

/**
 * Shamos-Hoey simple polygon detection
 * 
 * Based upon C++ implementation:
 * http://geomalgorithms.com/a09-_intersect-3.html
 * 
 * C++ implementation license:
 * 
 * Copyright 2001 softSurfer, 2012 Dan Sunday This code may be freely used and
 * modified for any purpose providing that this copyright notice is included
 * with it. SoftSurfer makes no warranty for this code, and cannot be held
 * liable for any real or imagined damage resulting from its use. Users of this
 * code must verify correctness for their application.
 * 
 * @author osbornb
 * @since 1.0.5
 */
public class ShamosHoey {

	/**
	 * Determine if the polygon is simple
	 * 
	 * @param polygon
	 *            polygon
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygon(Polygon polygon) {
		return simplePolygon(polygon.getRings());
	}

	/**
	 * Determine if the polygon points are simple
	 * 
	 * @param points
	 *            polygon as points
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygonPoints(List<Point> points) {
		LineString ring = new LineString();
		ring.setPoints(points);
		return simplePolygon(ring);
	}

	/**
	 * Determine if the polygon point rings are simple
	 * 
	 * @param pointRings
	 *            polygon point rings
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygonRingPoints(List<List<Point>> pointRings) {
		List<LineString> rings = new ArrayList<>();
		for (List<Point> points : pointRings) {
			LineString ring = new LineString();
			ring.setPoints(points);
			rings.add(ring);
		}
		return simplePolygon(rings);
	}

	/**
	 * Determine if the polygon line string ring is simple
	 * 
	 * @param ring
	 *            polygon ring as a line string
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygon(LineString ring) {
		List<LineString> rings = new ArrayList<>();
		rings.add(ring);
		return simplePolygon(rings);
	}

	/**
	 * Determine if the polygon rings are simple
	 * 
	 * @param rings
	 *            polygon rings
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygon(List<LineString> rings) {

		boolean simple = !rings.isEmpty();

		List<LineString> ringCopies = new ArrayList<>();
		for (int i = 0; i < rings.size(); i++) {

			LineString ring = rings.get(i);

			// Copy the ring
			LineString ringCopy = new LineString();
			ringCopy.setPoints(new ArrayList<>(ring.getPoints()));
			ringCopies.add(ringCopy);

			// Remove the last point when identical to the first
			List<Point> ringCopyPoints = ringCopy.getPoints();
			if (ringCopyPoints.size() >= 3) {
				Point first = ringCopyPoints.get(0);
				Point last = ringCopyPoints.get(ringCopyPoints.size() - 1);
				if (first.getX() == last.getX() && first.getY() == last.getY()) {
					ringCopyPoints.remove(ringCopyPoints.size() - 1);
				}
			}

			// Verify enough ring points
			if (ringCopyPoints.size() < 3) {
				simple = false;
				break;
			}

			// Check holes to make sure the first point is in the polygon
			if (i > 0) {
				Point firstPoint = ringCopyPoints.get(0);
				if (!GeometryUtils.pointInPolygon(firstPoint, rings.get(0))) {
					simple = false;
					break;
				}
				// Make sure the hole first points are not inside of one another
				for (int j = 1; j < i; j++) {
					List<Point> holePoints = rings.get(j).getPoints();
					if (GeometryUtils.pointInPolygon(firstPoint, holePoints)
							|| GeometryUtils.pointInPolygon(holePoints.get(0),
									ringCopyPoints)) {
						simple = false;
						break;
					}
				}
				if (!simple) {
					break;
				}
			}
		}

		// If valid polygon rings
		if (simple) {

			EventQueue eventQueue = new EventQueue(ringCopies);
			SweepLine sweepLine = new SweepLine(ringCopies);

			for (Event event : eventQueue) {
				if (event.getType() == EventType.LEFT) {
					Segment segment = sweepLine.add(event);
					if (sweepLine.intersect(segment, segment.getAbove())
							|| sweepLine.intersect(segment, segment.getBelow())) {
						simple = false;
						break;
					}
				} else {
					Segment segment = sweepLine.find(event);
					if (sweepLine.intersect(segment.getAbove(),
							segment.getBelow())) {
						simple = false;
						break;
					}
					sweepLine.remove(segment);
				}
			}
		}

		return simple;
	}

}
