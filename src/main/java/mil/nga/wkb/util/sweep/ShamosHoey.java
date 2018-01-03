package mil.nga.wkb.util.sweep;

import java.util.ArrayList;
import java.util.List;

import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;

/**
 * Shamos Hoey simple polygon detection
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

		boolean simple = true;

		// TODO handle holes not contained within the polygon and overlapping
		// holes

		for (LineString ring : polygon.getRings()) {
			if (!simplePolygon(ring)) {
				simple = false;
				break;
			}
		}

		return simple;
	}

	/**
	 * Determine if the polygon line string is simple
	 * 
	 * @param lineString
	 *            polygon as a line string
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygon(LineString lineString) {
		return simplePolygon(lineString.getPoints());
	}

	/**
	 * Determine if the polygon points are simple
	 * 
	 * @param points
	 *            polygon as poinst
	 * @return true if simple, false if intersects
	 */
	public static boolean simplePolygon(List<Point> points) {

		boolean simple = false;

		// Copy the points
		points = new ArrayList<>(points);

		// Remove the last point is same as the first
		if (points.size() > 1) {
			Point first = points.get(0);
			Point last = points.get(points.size() - 1);
			if (first.getX() == last.getX() || first.getY() == last.getY()) {
				points.remove(points.size() - 1);
			}
		}

		// If valid polygon
		if (points.size() > 2) {

			simple = true;

			EventQueue eventQueue = new EventQueue(points);
			SweepLine sweepLine = new SweepLine(points);

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
					sweepLine.remove(segment, event);
				}
			}
		}

		return simple;
	}

}
