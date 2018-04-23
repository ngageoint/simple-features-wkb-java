package mil.nga.wkb.test;

import java.io.IOException;
import java.nio.ByteOrder;

import junit.framework.TestCase;
import mil.nga.wkb.geom.CompoundCurve;
import mil.nga.wkb.geom.Curve;
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
import mil.nga.wkb.geom.extended.ExtendedGeometryCollection;
import mil.nga.wkb.util.GeometryEnvelopeBuilder;

import org.junit.Test;

/**
 * Test Well Known Binary Geometries
 * 
 * @author osbornb
 */
public class WKBTest {

	/**
	 * Number of random geometries to create for each test
	 */
	private static final int GEOMETRIES_PER_TEST = 10;

	/**
	 * Constructor
	 */
	public WKBTest() {

	}

	@Test
	public void testPoint() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a point
			Point point = WKBTestUtils.createPoint(WKBTestUtils.coinFlip(),
					WKBTestUtils.coinFlip());
			geometryTester(point);
		}

	}

	@Test
	public void testLineString() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a line string
			LineString lineString = WKBTestUtils.createLineString(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			geometryTester(lineString);
		}

	}

	@Test
	public void testPolygon() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a polygon
			Polygon polygon = WKBTestUtils.createPolygon(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			geometryTester(polygon);
		}

	}

	@Test
	public void testMultiPoint() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi point
			MultiPoint multiPoint = WKBTestUtils.createMultiPoint(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			geometryTester(multiPoint);
		}

	}

	@Test
	public void testMultiLineString() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi line string
			MultiLineString multiLineString = WKBTestUtils
					.createMultiLineString(WKBTestUtils.coinFlip(),
							WKBTestUtils.coinFlip());
			geometryTester(multiLineString);
		}

	}

	@Test
	public void testMultiCurveWithLineStrings() throws IOException {

		// Test a pre-created WKB saved as the abstract MultiCurve type with
		// LineStrings

		byte[] bytes = new byte[] { 0, 0, 0, 0, 11, 0, 0, 0, 2, 0, 0, 0, 0, 2,
				0, 0, 0, 3, 64, 50, -29, -55, -6, 126, -15, 120, -64, 65, -124,
				-86, -46, -62, -60, 94, -64, 66, -31, -40, 124, -2, -47, -5,
				-64, 82, -13, -22, 8, -38, 6, 111, 64, 81, 58, 88, 78, -15, 82,
				111, -64, 86, 20, -18, -37, 3, -99, -86, 0, 0, 0, 0, 2, 0, 0,
				0, 10, 64, 98, 48, -84, 37, -62, 34, 98, -64, 68, -12, -81,
				104, 13, -109, 6, -64, 101, -82, 76, -68, 34, 117, -110, 64,
				39, -125, 83, 1, 50, 86, 8, -64, 83, 127, -93, 42, -89, 54,
				-56, -64, 67, -58, -13, -104, 1, -17, -10, 64, 97, 18, -82,
				-112, 100, -128, 16, 64, 68, -13, -86, -112, 112, 59, -3, 64,
				67, -4, -71, -91, -16, -15, 85, -64, 49, 110, -16, 94, -71, 24,
				-13, -64, 94, 84, 94, -4, -78, -101, -75, -64, 80, 74, -39, 90,
				38, 107, 104, 64, 72, -16, -43, 82, -112, -39, 77, 64, 28, 30,
				97, -26, 64, 102, -110, 64, 92, 63, -14, -103, 99, -67, 63,
				-64, 65, -48, 84, -37, -111, -55, -25, -64, 101, -10, -62,
				-115, 104, -125, 28, -64, 66, 5, 108, -56, -59, 69, -36, -64,
				83, 33, -36, -86, 106, -84, -16, 64, 70, 30, -104, -50, -57,
				15, -7 };

		TestCase.assertEquals(GeometryType.MULTICURVE.getCode(), bytes[4]);

		Geometry geometry = WKBTestUtils.readGeometry(bytes);
		TestCase.assertTrue(geometry instanceof GeometryCollection);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.GEOMETRYCOLLECTION);
		@SuppressWarnings("unchecked")
		GeometryCollection<Curve> multiCurve = (GeometryCollection<Curve>) geometry;
		TestCase.assertEquals(2, multiCurve.numGeometries());
		Geometry geometry1 = multiCurve.getGeometries().get(0);
		Geometry geometry2 = multiCurve.getGeometries().get(1);
		TestCase.assertTrue(geometry1 instanceof LineString);
		TestCase.assertTrue(geometry2 instanceof LineString);
		LineString lineString1 = (LineString) geometry1;
		LineString lineString2 = (LineString) geometry2;
		TestCase.assertEquals(3, lineString1.numPoints());
		TestCase.assertEquals(10, lineString2.numPoints());
		Point point1 = lineString1.getPoints().get(0);
		Point point2 = lineString2.getPoints().get(lineString2.numPoints() - 1);
		TestCase.assertEquals(18.889800697319032, point1.getX());
		TestCase.assertEquals(-35.036463112927535, point1.getY());
		TestCase.assertEquals(-76.52909336488278, point2.getX());
		TestCase.assertEquals(44.2390383216843, point2.getY());

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		geometryTester(extendedMultiCurve, multiCurve);

		byte[] bytes2 = WKBTestUtils.writeBytes(extendedMultiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE.getCode(), bytes2[4]);
		WKBTestUtils.compareByteArrays(bytes, bytes2);

	}

	@Test
	public void testMultiCurveWithCompoundCurve() throws IOException {

		// Test a pre-created WKB saved as the abstract MultiCurve type with a
		// CompoundCurve

		byte[] bytes = new byte[] { 0, 0, 0, 0, 11, 0, 0, 0, 1, 0, 0, 0, 0, 9,
				0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 3, 65, 74, 85, 13, 0, -60,
				-101, -90, 65, 84, -23, 84, 60, -35, 47, 27, 65, 74, 85, 12,
				-28, -68, 106, 127, 65, 84, -23, 84, 123, 83, -9, -49, 65, 74,
				85, 8, -1, 92, 40, -10, 65, 84, -23, 83, -81, -99, -78, 45, 0,
				0, 0, 0, 2, 0, 0, 0, 2, 65, 74, 85, 8, -1, 92, 40, -10, 65, 84,
				-23, 83, -81, -99, -78, 45, 65, 74, 85, 13, 0, -60, -101, -90,
				65, 84, -23, 84, 60, -35, 47, 27 };

		TestCase.assertEquals(GeometryType.MULTICURVE.getCode(), bytes[4]);

		Geometry geometry = WKBTestUtils.readGeometry(bytes);
		TestCase.assertTrue(geometry instanceof GeometryCollection);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.GEOMETRYCOLLECTION);
		@SuppressWarnings("unchecked")
		GeometryCollection<Curve> multiCurve = (GeometryCollection<Curve>) geometry;
		TestCase.assertEquals(1, multiCurve.numGeometries());
		Geometry geometry1 = multiCurve.getGeometries().get(0);
		TestCase.assertTrue(geometry1 instanceof CompoundCurve);
		CompoundCurve compoundCurve1 = (CompoundCurve) geometry1;
		TestCase.assertEquals(2, compoundCurve1.numLineStrings());
		LineString lineString1 = compoundCurve1.getLineStrings().get(0);
		LineString lineString2 = compoundCurve1.getLineStrings().get(1);
		TestCase.assertEquals(3, lineString1.numPoints());
		TestCase.assertEquals(2, lineString2.numPoints());

		TestCase.assertEquals(3451418.006, lineString1.getPoints().get(0)
				.getX());
		TestCase.assertEquals(5481808.951, lineString1.getPoints().get(0)
				.getY());
		TestCase.assertEquals(3451417.787, lineString1.getPoints().get(1)
				.getX());
		TestCase.assertEquals(5481809.927, lineString1.getPoints().get(1)
				.getY());
		TestCase.assertEquals(3451409.995, lineString1.getPoints().get(2)
				.getX());
		TestCase.assertEquals(5481806.744, lineString1.getPoints().get(2)
				.getY());

		TestCase.assertEquals(3451409.995, lineString2.getPoints().get(0)
				.getX());
		TestCase.assertEquals(5481806.744, lineString2.getPoints().get(0)
				.getY());
		TestCase.assertEquals(3451418.006, lineString2.getPoints().get(1)
				.getX());
		TestCase.assertEquals(5481808.951, lineString2.getPoints().get(1)
				.getY());

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		geometryTester(extendedMultiCurve, multiCurve);

		byte[] bytes2 = WKBTestUtils.writeBytes(extendedMultiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE.getCode(), bytes2[4]);
		WKBTestUtils.compareByteArrays(bytes, bytes2);
	}

	@Test
	public void testMultiPolygon() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi polygon
			MultiPolygon multiPolygon = WKBTestUtils.createMultiPolygon(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip());
			geometryTester(multiPolygon);
		}

	}

	@Test
	public void testGeometryCollection() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a geometry collection
			GeometryCollection<Geometry> geometryCollection = WKBTestUtils
					.createGeometryCollection(WKBTestUtils.coinFlip(),
							WKBTestUtils.coinFlip());
			geometryTester(geometryCollection);
		}

	}

	/**
	 * Test the geometry writing to and reading from bytes
	 * 
	 * @param geometry
	 *            geometry
	 * @throws IOException
	 */
	private void geometryTester(Geometry geometry) throws IOException {

		geometryTester(geometry, geometry);

	}

	/**
	 * Test the geometry writing to and reading from bytes, compare with the
	 * provided geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @param compareGeometry
	 *            compare geometry
	 * @throws IOException
	 */
	private void geometryTester(Geometry geometry, Geometry compareGeometry)
			throws IOException {

		// Write the geometries to bytes
		byte[] bytes1 = WKBTestUtils.writeBytes(geometry, ByteOrder.BIG_ENDIAN);
		byte[] bytes2 = WKBTestUtils.writeBytes(geometry,
				ByteOrder.LITTLE_ENDIAN);

		TestCase.assertFalse(WKBTestUtils.equalByteArrays(bytes1, bytes2));

		// Test that the bytes are read using their written byte order, not
		// the specified
		Geometry geometry1opposite = WKBTestUtils.readGeometry(bytes1,
				ByteOrder.LITTLE_ENDIAN);
		Geometry geometry2opposite = WKBTestUtils.readGeometry(bytes2,
				ByteOrder.BIG_ENDIAN);
		WKBTestUtils.compareByteArrays(
				WKBTestUtils.writeBytes(compareGeometry),
				WKBTestUtils.writeBytes(geometry1opposite));
		WKBTestUtils.compareByteArrays(
				WKBTestUtils.writeBytes(compareGeometry),
				WKBTestUtils.writeBytes(geometry2opposite));

		Geometry geometry1 = WKBTestUtils.readGeometry(bytes1,
				ByteOrder.BIG_ENDIAN);
		Geometry geometry2 = WKBTestUtils.readGeometry(bytes2,
				ByteOrder.LITTLE_ENDIAN);

		WKBTestUtils.compareGeometries(compareGeometry, geometry1);
		WKBTestUtils.compareGeometries(compareGeometry, geometry2);
		WKBTestUtils.compareGeometries(geometry1, geometry2);

		GeometryEnvelope envelope = GeometryEnvelopeBuilder
				.buildEnvelope(compareGeometry);
		GeometryEnvelope envelope1 = GeometryEnvelopeBuilder
				.buildEnvelope(geometry1);
		GeometryEnvelope envelope2 = GeometryEnvelopeBuilder
				.buildEnvelope(geometry2);

		WKBTestUtils.compareEnvelopes(envelope, envelope1);
		WKBTestUtils.compareEnvelopes(envelope1, envelope2);
	}

}
