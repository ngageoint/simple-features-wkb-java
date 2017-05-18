package mil.nga.wkb.test;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryEnvelope;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.util.GeometryEnvelopeBuilder;
import mil.nga.wkb.util.GeometryUtils;

import org.junit.Test;

/**
 * Test Well Known Binary Geometry Utilities
 * 
 * @author osbornb
 */
public class GeometryUtilsTest {

	/**
	 * Number of random geometries to create for each test
	 */
	private static final int GEOMETRIES_PER_TEST = 10;

	/**
	 * Constructor
	 */
	public GeometryUtilsTest() {

	}

	@Test
	public void testPointCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a point
			Point point = WKBTestUtils.createPoint(WKBTestUtils.coinFlip(),
					WKBTestUtils.coinFlip());
			TestCase.assertEquals(0, GeometryUtils.getDimension(point));
			geometryCentroidTester(point);
		}

	}

	@Test
	public void testLineStringCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a line string
			LineString lineString = WKBTestUtils.createLineString(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			TestCase.assertEquals(1, GeometryUtils.getDimension(lineString));
			geometryCentroidTester(lineString);
		}

	}

	@Test
	public void testPolygonCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a polygon
			Polygon polygon = createPolygon();
			TestCase.assertEquals(2, GeometryUtils.getDimension(polygon));
			geometryCentroidTester(polygon);
		}

	}

	@Test
	public void testMultiPointCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi point
			MultiPoint multiPoint = WKBTestUtils.createMultiPoint(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			TestCase.assertEquals(0, GeometryUtils.getDimension(multiPoint));
			geometryCentroidTester(multiPoint);
		}

	}

	@Test
	public void testMultiLineStringCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi line string
			MultiLineString multiLineString = WKBTestUtils
					.createMultiLineString(WKBTestUtils.coinFlip(),
							WKBTestUtils.coinFlip());
			TestCase.assertEquals(1,
					GeometryUtils.getDimension(multiLineString));
			geometryCentroidTester(multiLineString);
		}

	}

	@Test
	public void testMultiPolygonCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi polygon
			MultiPolygon multiPolygon = createMultiPolygon();
			TestCase.assertEquals(2, GeometryUtils.getDimension(multiPolygon));
			geometryCentroidTester(multiPolygon);
		}

	}

	@Test
	public void testGeometryCollectionCentroid() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a geometry collection
			GeometryCollection<Geometry> geometryCollection = createGeometryCollection(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			geometryCentroidTester(geometryCollection);
		}

	}

	@Test
	public void testPolygonCentroidWithAndWithoutHole() throws IOException {

		Polygon polygon = new Polygon();
		LineString lineString = new LineString();
		lineString.addPoint(new Point(-90, 45));
		lineString.addPoint(new Point(-90, -45));
		lineString.addPoint(new Point(90, -45));
		lineString.addPoint(new Point(90, 45));
		polygon.addRing(lineString);

		TestCase.assertEquals(2, GeometryUtils.getDimension(polygon));
		Point centroid = geometryCentroidTester(polygon);

		TestCase.assertEquals(0.0, centroid.getX());
		TestCase.assertEquals(0.0, centroid.getY());

		LineString holeLineString = new LineString();
		holeLineString.addPoint(new Point(0, 45));
		holeLineString.addPoint(new Point(0, 0));
		holeLineString.addPoint(new Point(90, 0));
		holeLineString.addPoint(new Point(90, 45));
		polygon.addRing(holeLineString);

		TestCase.assertEquals(2, GeometryUtils.getDimension(polygon));
		centroid = geometryCentroidTester(polygon);

		TestCase.assertEquals(-15.0, centroid.getX());
		TestCase.assertEquals(-7.5, centroid.getY());
	}

	/**
	 * Test the geometry centroid
	 * 
	 * @param geometry
	 * @throws IOException
	 */
	private Point geometryCentroidTester(Geometry geometry) throws IOException {

		Point point = GeometryUtils.getCentroid(geometry);

		GeometryEnvelope envelope = GeometryEnvelopeBuilder
				.buildEnvelope(geometry);

		if (geometry.getGeometryType() == GeometryType.POINT) {
			TestCase.assertEquals(envelope.getMinX(), point.getX());
			TestCase.assertEquals(envelope.getMaxX(), point.getX());
			TestCase.assertEquals(envelope.getMinY(), point.getY());
			TestCase.assertEquals(envelope.getMaxY(), point.getY());
		}

		TestCase.assertTrue(point.getX() >= envelope.getMinX());
		TestCase.assertTrue(point.getX() <= envelope.getMaxX());
		TestCase.assertTrue(point.getY() >= envelope.getMinY());
		TestCase.assertTrue(point.getY() <= envelope.getMaxY());

		return point;
	}

	private static Polygon createPolygon() {

		Polygon polygon = new Polygon();
		LineString lineString = new LineString();
		lineString.addPoint(createPoint(-180.0, 45.0, 90.0, 45.0));
		lineString.addPoint(createPoint(-180.0, -90.0, 90.0, 45.0));
		lineString.addPoint(createPoint(90.0, -90.0, 90.0, 45.0));
		lineString.addPoint(createPoint(90.0, 45.0, 90.0, 45.0));
		polygon.addRing(lineString);

		LineString holeLineString = new LineString();
		holeLineString.addPoint(createPoint(-90.0, 0.0, 90.0, 45.0));
		holeLineString.addPoint(createPoint(-90.0, -45.0, 90.0, 45.0));
		holeLineString.addPoint(createPoint(0.0, -45.0, 90.0, 45.0));
		holeLineString.addPoint(createPoint(0.0, 0.0, 90.0, 45.0));
		polygon.addRing(holeLineString);

		return polygon;
	}

	private static Point createPoint(double minX, double minY, double xRange,
			double yRange) {

		double x = minX + (Math.random() * xRange);
		double y = minY + (Math.random() * yRange);

		Point point = new Point(x, y);

		return point;
	}

	private static MultiPolygon createMultiPolygon() {

		MultiPolygon multiPolygon = new MultiPolygon();

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			multiPolygon.addPolygon(createPolygon());
		}

		return multiPolygon;
	}

	private static GeometryCollection<Geometry> createGeometryCollection(
			boolean hasZ, boolean hasM) {

		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {

			Geometry geometry = null;
			int randomGeometry = (int) (Math.random() * 6);

			switch (randomGeometry) {
			case 0:
				geometry = WKBTestUtils.createPoint(hasZ, hasM);
				break;
			case 1:
				geometry = WKBTestUtils.createLineString(hasZ, hasM);
				break;
			case 2:
				geometry = createPolygon();
				break;
			case 3:
				geometry = WKBTestUtils.createMultiPoint(hasZ, hasM);
				break;
			case 4:
				geometry = WKBTestUtils.createMultiLineString(hasZ, hasM);
				break;
			case 5:
				geometry = createMultiPolygon();
				break;
			}

			geometryCollection.addGeometry(geometry);
		}

		return geometryCollection;
	}

	@Test
	public void testCopyMinimizeAndNormalize() {

		Polygon polygon = new Polygon();
		LineString ring = new LineString();
		double random = Math.random();
		if (random < .5) {
			ring.addPoint(createPoint(90.0, 0.0, 90.0, 90.0));
			ring.addPoint(createPoint(90.0, -90.0, 90.0, 90.0));
			ring.addPoint(createPoint(-180.0, -90.0, 89.0, 90.0));
			ring.addPoint(createPoint(-180.0, 0.0, 89.0, 90.0));
		} else {
			ring.addPoint(createPoint(-180.0, 0.0, 89.0, 90.0));
			ring.addPoint(createPoint(-180.0, -90.0, 89.0, 90.0));
			ring.addPoint(createPoint(90.0, -90.0, 90.0, 90.0));
			ring.addPoint(createPoint(90.0, 0.0, 90.0, 90.0));
		}
		polygon.addRing(ring);

		Polygon polygon2 = (Polygon) polygon.copy();
		GeometryUtils.minimizeGeometry(polygon2, 180.0);

		Polygon polygon3 = (Polygon) polygon2.copy();
		GeometryUtils.normalizeGeometry(polygon3, 180.0);

		List<Point> points = ring.getPoints();
		LineString ring2 = polygon2.getRings().get(0);
		List<Point> points2 = ring2.getPoints();
		LineString ring3 = polygon3.getRings().get(0);
		List<Point> points3 = ring3.getPoints();

		for (int i = 0; i < points.size(); i++) {

			Point point = points.get(i);
			Point point2 = points2.get(i);
			Point point3 = points3.get(i);

			TestCase.assertEquals(point.getY(), point2.getY(), .0000000001);
			TestCase.assertEquals(point.getY(), point3.getY(), .0000000001);
			TestCase.assertEquals(point.getX(), point3.getX(), .0000000001);
			if (i < 2) {
				TestCase.assertEquals(point.getX(), point2.getX(), .0000000001);
			} else {
				double point2Value = point2.getX();
				if (random < .5) {
					point2Value -= 360.0;
				} else {
					point2Value += 360.0;
				}
				TestCase.assertEquals(point.getX(), point2Value, .0000000001);
			}
		}

	}

}
