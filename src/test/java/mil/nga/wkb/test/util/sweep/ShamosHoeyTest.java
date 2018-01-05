package mil.nga.wkb.test.util.sweep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.util.sweep.ShamosHoey;

import org.junit.Test;

/**
 * Test Shamos-Hoey simple polygon methods
 * 
 * @author osbornb
 */
public class ShamosHoeyTest {

	@Test
	public void testSimple() throws IOException {

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 1, 0);
		addPoint(points, .5, 1);

		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(3, points.size());

		addPoint(points, 0, 0);

		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(4, points.size());

		points.clear();

		addPoint(points, 0, 100);
		addPoint(points, 100, 0);
		addPoint(points, 200, 100);
		addPoint(points, 100, 200);
		addPoint(points, 0, 100);

		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(5, points.size());

		points.clear();

		addPoint(points, -104.8384094, 39.753657);
		addPoint(points, -104.8377228, 39.7354422);
		addPoint(points, -104.7930908, 39.7364983);
		addPoint(points, -104.8233891, 39.7440222);
		addPoint(points, -104.7930908, 39.7369603);
		addPoint(points, -104.808197, 39.7541849);
		addPoint(points, -104.8383236, 39.753723);

		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(7, points.size());

		points.clear();

		addPoint(points, -106.3256836, 40.2962865);
		addPoint(points, -105.6445313, 38.5911138);
		addPoint(points, -105.0842285, 40.3046654);
		addPoint(points, -105.6445313, 38.5911139);

		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(4, points.size());

	}

	@Test
	public void testNonSimple() throws IOException {

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(1, points.size());

		addPoint(points, 1, 0);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(2, points.size());

		addPoint(points, 0, 0);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(3, points.size());

		points.clear();

		addPoint(points, 0, 100);
		addPoint(points, 100, 0);
		addPoint(points, 200, 100);
		addPoint(points, 100, 200);
		addPoint(points, 100.01, 200);
		addPoint(points, 0, 100);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(6, points.size());

		points.clear();

		addPoint(points, -104.8384094, 39.753657);
		addPoint(points, -104.8377228, 39.7354422);
		addPoint(points, -104.7930908, 39.7364983);
		addPoint(points, -104.8233891, 39.7440222);
		addPoint(points, -104.8034763, 39.7387424);
		addPoint(points, -104.7930908, 39.7369603);
		addPoint(points, -104.808197, 39.7541849);
		addPoint(points, -104.8383236, 39.753723);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(8, points.size());

		points.clear();

		addPoint(points, -106.3256836, 40.2962865);
		addPoint(points, -105.6445313, 38.5911138);
		addPoint(points, -105.0842285, 40.3046654);
		addPoint(points, -105.6445313, 38.5911138);

		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		TestCase.assertEquals(4, points.size());

	}

	@Test
	public void testSimpleHole() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints = new ArrayList<>();

		addPoint(holePoints, 1, 1);
		addPoint(holePoints, 9, 1);
		addPoint(holePoints, 5, 9);

		LineString hole = new LineString();
		hole.setPoints(holePoints);

		polygon.addRing(hole);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());
	}

	@Test
	public void testNonSimpleHole() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints = new ArrayList<>();

		addPoint(holePoints, 1, 1);
		addPoint(holePoints, 9, 1);
		addPoint(holePoints, 5, 9);
		addPoint(holePoints, 5.000001, 9);

		LineString hole = new LineString();
		hole.setPoints(holePoints);

		polygon.addRing(hole);

		TestCase.assertFalse(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(4, polygon.getRings().get(1).numPoints());
	}

	@Test
	public void testIntersectingHole() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints = new ArrayList<>();

		addPoint(holePoints, 1, 1);
		addPoint(holePoints, 9, 1);
		addPoint(holePoints, 5, 10);

		LineString hole = new LineString();
		hole.setPoints(holePoints);

		polygon.addRing(hole);

		TestCase.assertFalse(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());
	}

	@Test
	public void testIntersectingHoles() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints1 = new ArrayList<>();

		addPoint(holePoints1, 1, 1);
		addPoint(holePoints1, 9, 1);
		addPoint(holePoints1, 5, 9);

		LineString hole1 = new LineString();
		hole1.setPoints(holePoints1);

		polygon.addRing(hole1);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());

		List<Point> holePoints2 = new ArrayList<>();

		addPoint(holePoints2, 5.0, 0.1);
		addPoint(holePoints2, 6.0, 0.1);
		addPoint(holePoints2, 5.5, 1.00001);

		LineString hole2 = new LineString();
		hole2.setPoints(holePoints2);

		polygon.addRing(hole2);

		TestCase.assertFalse(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(3, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(2).numPoints());
	}

	@Test
	public void testHoleInsideHole() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints1 = new ArrayList<>();

		addPoint(holePoints1, 1, 1);
		addPoint(holePoints1, 9, 1);
		addPoint(holePoints1, 5, 9);

		LineString hole1 = new LineString();
		hole1.setPoints(holePoints1);

		polygon.addRing(hole1);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());

		List<Point> holePoints2 = new ArrayList<>();

		addPoint(holePoints2, 2, 2);
		addPoint(holePoints2, 8, 2);
		addPoint(holePoints2, 5, 8);

		LineString hole2 = new LineString();
		hole2.setPoints(holePoints2);

		polygon.addRing(hole2);

		TestCase.assertFalse(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(3, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(2).numPoints());
	}

	@Test
	public void testExternalHole() throws IOException {

		Polygon polygon = new Polygon();

		List<Point> points = new ArrayList<>();

		addPoint(points, 0, 0);
		addPoint(points, 10, 0);
		addPoint(points, 5, 10);

		LineString ring = new LineString();
		ring.setPoints(points);

		polygon.addRing(ring);

		TestCase.assertTrue(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(1, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());

		List<Point> holePoints = new ArrayList<>();

		addPoint(holePoints, -1, 1);
		addPoint(holePoints, -1, 3);
		addPoint(holePoints, -2, 1);

		LineString hole = new LineString();
		hole.setPoints(holePoints);

		polygon.addRing(hole);

		TestCase.assertFalse(ShamosHoey.simplePolygon(polygon));
		TestCase.assertEquals(2, polygon.numRings());
		TestCase.assertEquals(3, polygon.getRings().get(0).numPoints());
		TestCase.assertEquals(3, polygon.getRings().get(1).numPoints());
	}

	@Test
	public void testLargeSimple() throws IOException {

		double increment = .001;
		double radius = 1250;
		double x = -radius + increment;
		double y = 0;

		List<Point> points = new ArrayList<>();

		while (x <= radius) {
			if (x <= 0) {
				y -= increment;
			} else {
				y += increment;
			}
			addPoint(points, x, y);
			x += increment;
		}

		x = radius - increment;
		while (x >= -radius) {
			if (x >= 0) {
				y += increment;
			} else {
				y -= increment;
			}
			addPoint(points, x, y);
			x -= increment;
		}

		// Date before = new Date();
		TestCase.assertTrue(ShamosHoey.simplePolygonPoints(points));
		// Date after = new Date();
		// long time = after.getTime() - before.getTime();
		// System.out.println("Points: " + points.size() + ", Time: " + time);
		TestCase.assertEquals((int) (radius / increment * 4), points.size());

	}

	@Test
	public void testLargeNonSimple() throws IOException {

		double increment = .001;
		double radius = 1250;
		double x = -radius + increment;
		double y = 0;

		List<Point> points = new ArrayList<>();

		while (x <= radius) {
			if (x <= 0) {
				y -= increment;
			} else {
				y += increment;
			}
			addPoint(points, x, y);
			x += increment;
		}

		Point previousPoint = points.get(points.size() - 2);
		int invalidIndex = points.size();
		addPoint(points, previousPoint.getX(),
				previousPoint.getY() - .000000000000001);

		x = radius - increment;
		while (x >= -radius) {
			if (x >= 0) {
				y += increment;
			} else {
				y -= increment;
			}
			addPoint(points, x, y);
			x -= increment;
		}

		// Date before = new Date();
		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		// Date after = new Date();
		// long time = after.getTime() - before.getTime();
		// System.out.println("Points: " + points.size() + ", Time: " + time);
		TestCase.assertEquals(1 + (int) (radius / increment * 4), points.size());

		points.remove(invalidIndex);
		previousPoint = points.get(points.size() - 3);
		addPoint(points, previousPoint.getX(),
				previousPoint.getY() + .000000000000001);

		// Date before2 = new Date();
		TestCase.assertFalse(ShamosHoey.simplePolygonPoints(points));
		// Date after2 = new Date();
		// long time2 = after2.getTime() - before2.getTime();
		// System.out.println("Points: " + points.size() + ", Time: " + time2);
		TestCase.assertEquals(1 + (int) (radius / increment * 4), points.size());
	}

	private void addPoint(List<Point> points, double x, double y) {
		points.add(new Point(x, y));
	}

}
