package mil.nga.sf.wkb.test;

import java.io.IOException;
import java.nio.ByteOrder;

import junit.framework.TestCase;
import mil.nga.sf.CompoundCurve;
import mil.nga.sf.Curve;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryCollection;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.GeometryType;
import mil.nga.sf.LineString;
import mil.nga.sf.MultiLineString;
import mil.nga.sf.MultiPoint;
import mil.nga.sf.MultiPolygon;
import mil.nga.sf.Point;
import mil.nga.sf.Polygon;
import mil.nga.sf.Surface;
import mil.nga.sf.extended.ExtendedGeometryCollection;
import mil.nga.sf.util.GeometryEnvelopeBuilder;
import mil.nga.sf.wkb.GeometryCodes;

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

		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTICURVE),
				bytes[4]);

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
		Point point1 = lineString1.startPoint();
		Point point2 = lineString2.endPoint();
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
		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTICURVE),
				bytes2[4]);
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

		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTICURVE),
				bytes[4]);

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

		TestCase.assertEquals(new Point(3451418.006, 5481808.951),
				lineString1.getPoint(0));
		TestCase.assertEquals(new Point(3451417.787, 5481809.927),
				lineString1.getPoint(1));
		TestCase.assertEquals(new Point(3451409.995, 5481806.744),
				lineString1.getPoint(2));

		TestCase.assertEquals(new Point(3451409.995, 5481806.744),
				lineString2.getPoint(0));
		TestCase.assertEquals(new Point(3451418.006, 5481808.951),
				lineString2.getPoint(1));

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		geometryTester(extendedMultiCurve, multiCurve);

		byte[] bytes2 = WKBTestUtils.writeBytes(extendedMultiCurve);
		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTICURVE),
				bytes2[4]);
		WKBTestUtils.compareByteArrays(bytes, bytes2);

	}

	@Test
	public void testMultiCurve() throws IOException {

		// Test the abstract MultiCurve type

		GeometryCollection<Curve> multiCurve = WKBTestUtils.createMultiCurve();

		byte[] bytes = WKBTestUtils.writeBytes(multiCurve);

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		byte[] extendedBytes = WKBTestUtils.writeBytes(extendedMultiCurve);

		TestCase.assertEquals(
				GeometryCodes.getCode(GeometryType.GEOMETRYCOLLECTION),
				bytes[4]);
		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTICURVE),
				extendedBytes[4]);

		Geometry geometry1 = WKBTestUtils.readGeometry(bytes);
		Geometry geometry2 = WKBTestUtils.readGeometry(extendedBytes);

		TestCase.assertTrue(geometry1 instanceof GeometryCollection);
		TestCase.assertTrue(geometry2 instanceof GeometryCollection);
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry1.getGeometryType());
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry2.getGeometryType());

		TestCase.assertEquals(multiCurve, geometry1);
		TestCase.assertEquals(geometry1, geometry2);

		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection1 = (GeometryCollection<Geometry>) geometry1;
		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection2 = (GeometryCollection<Geometry>) geometry2;
		TestCase.assertTrue(geometryCollection1.isMultiCurve());
		TestCase.assertTrue(geometryCollection2.isMultiCurve());

		geometryTester(multiCurve);
		geometryTester(extendedMultiCurve, multiCurve);
	}

	@Test
	public void testMultiSurface() throws IOException {

		// Test the abstract MultiSurface type

		GeometryCollection<Surface> multiSurface = WKBTestUtils
				.createMultiSurface();

		byte[] bytes = WKBTestUtils.writeBytes(multiSurface);

		ExtendedGeometryCollection<Surface> extendedMultiSurface = new ExtendedGeometryCollection<>(
				multiSurface);
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				extendedMultiSurface.getGeometryType());

		byte[] extendedBytes = WKBTestUtils.writeBytes(extendedMultiSurface);

		TestCase.assertEquals(
				GeometryCodes.getCode(GeometryType.GEOMETRYCOLLECTION),
				bytes[4]);
		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTISURFACE),
				extendedBytes[4]);

		Geometry geometry1 = WKBTestUtils.readGeometry(bytes);
		Geometry geometry2 = WKBTestUtils.readGeometry(extendedBytes);

		TestCase.assertTrue(geometry1 instanceof GeometryCollection);
		TestCase.assertTrue(geometry2 instanceof GeometryCollection);
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry1.getGeometryType());
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry2.getGeometryType());

		TestCase.assertEquals(multiSurface, geometry1);
		TestCase.assertEquals(geometry1, geometry2);

		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection1 = (GeometryCollection<Geometry>) geometry1;
		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection2 = (GeometryCollection<Geometry>) geometry2;
		TestCase.assertTrue(geometryCollection1.isMultiSurface());
		TestCase.assertTrue(geometryCollection2.isMultiSurface());

		geometryTester(multiSurface);
		geometryTester(extendedMultiSurface, multiSurface);
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

	@Test
	public void testMultiPolygon25() throws IOException {

		// Test a pre-created WKB hex saved as a 2.5D MultiPolygon

		byte[] bytes = hexStringToByteArray("0106000080010000000103000080010000000F0000007835454789C456C0DFDB63124D3F2C4000000000000000004CE4512E89C456C060BF20D13F3F2C400000000000000000A42EC6388CC456C0E0A50400423F2C400000000000000000B4E3B1608CC456C060034E67433F2C400000000000000000F82138508DC456C09FD015C5473F2C400000000000000000ECD6591B8CC456C000C305BC5B3F2C4000000000000000001002AD0F8CC456C060DB367D5C3F2C40000000000000000010996DEF8AC456C0BF01756A6C3F2C4000000000000000007054A08B8AC456C0806A0C1F733F2C4000000000000000009422D81D8AC456C041CA3C5B8A3F2C4000000000000000003CCB05C489C456C03FC4FC52AA3F2C400000000000000000740315A689C456C0BFC8635EB33F2C400000000000000000E4A5630B89C456C0DFE726D6B33F2C400000000000000000F45A4F3389C456C000B07950703F2C4000000000000000007835454789C456C0DFDB63124D3F2C400000000000000000");

		TestCase.assertEquals(1, bytes[0]); // little endian
		TestCase.assertEquals(GeometryCodes.getCode(GeometryType.MULTIPOLYGON),
				bytes[1]);
		TestCase.assertEquals(0, bytes[2]);
		TestCase.assertEquals(0, bytes[3]);
		TestCase.assertEquals(-128, bytes[4]);

		Geometry geometry = WKBTestUtils.readGeometry(bytes);
		TestCase.assertTrue(geometry instanceof MultiPolygon);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.MULTIPOLYGON);
		MultiPolygon multiPolygon = (MultiPolygon) geometry;
		TestCase.assertTrue(multiPolygon.hasZ());
		TestCase.assertFalse(multiPolygon.hasM());
		TestCase.assertEquals(1, multiPolygon.numGeometries());
		Polygon polygon = multiPolygon.getPolygons().get(0);
		TestCase.assertTrue(polygon.hasZ());
		TestCase.assertFalse(polygon.hasM());
		TestCase.assertEquals(1, polygon.numRings());
		LineString ring = polygon.getRings().get(0);
		TestCase.assertTrue(ring.hasZ());
		TestCase.assertFalse(ring.hasM());
		TestCase.assertEquals(15, ring.numPoints());
		for (Point point : ring.getPoints()) {
			TestCase.assertTrue(point.hasZ());
			TestCase.assertFalse(point.hasM());
			TestCase.assertNotNull(point.getZ());
			TestCase.assertNull(point.getM());
		}

		byte[] multiPolygonBytes = WKBTestUtils.writeBytes(multiPolygon,
				ByteOrder.LITTLE_ENDIAN);
		Geometry geometry2 = WKBTestUtils.readGeometry(multiPolygonBytes);

		geometryTester(geometry, geometry2);

		TestCase.assertEquals(bytes.length, multiPolygonBytes.length);
		int equalBytes = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == multiPolygonBytes[i]) {
				equalBytes++;
			}
		}
		TestCase.assertEquals(bytes.length - 6, equalBytes);
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

	/**
	 * Convert the hex string to a byte array
	 * 
	 * @param hex
	 *            hex string
	 * @return byte array
	 */
	private static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character
					.digit(hex.charAt(i + 1), 16));
		}
		return bytes;
	}

}
