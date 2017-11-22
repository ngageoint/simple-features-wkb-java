package mil.nga.wkb.util.sweep;

import java.util.List;

import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;

public class ShamosHoey {

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

	public static boolean simplePolygon(LineString lineString) {
		return simplePolygon(lineString.getPoints());
	}

	public static boolean simplePolygon(List<Point> points) {

		boolean simple = true;

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
				if (sweepLine.intersect(segment.getAbove(), segment.getBelow())) {
					simple = false;
					break;
				}
				sweepLine.remove(segment);
			}
		}

		return simple;
	}

}
