package mil.nga.sf.wkt.test;

import java.io.IOException;

import junit.framework.TestCase;
import mil.nga.sf.CircularString;
import mil.nga.sf.CompoundCurve;
import mil.nga.sf.Curve;
import mil.nga.sf.CurvePolygon;
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
import mil.nga.sf.PolyhedralSurface;
import mil.nga.sf.Surface;
import mil.nga.sf.TIN;
import mil.nga.sf.Triangle;
import mil.nga.sf.util.TextReader;
import mil.nga.sf.wkt.GeometryReader;
import mil.nga.sf.wkt.GeometryTypeInfo;
import mil.nga.sf.wkt.GeometryWriter;

/**
 * WKB test utils
 * 
 * @author osbornb
 */
public class WKTTestUtils {

	/**
	 * Compare two geometry envelopes and verify they are equal
	 * 
	 * @param expected
	 *            expected geometry envelope
	 * @param actual
	 *            actual geometry envelope
	 */
	public static void compareEnvelopes(GeometryEnvelope expected,
			GeometryEnvelope actual) {

		if (expected == null) {
			TestCase.assertNull(actual);
		} else {
			TestCase.assertNotNull(actual);

			TestCase.assertEquals(expected.getMinX(), actual.getMinX());
			TestCase.assertEquals(expected.getMaxX(), actual.getMaxX());
			TestCase.assertEquals(expected.getMinY(), actual.getMinY());
			TestCase.assertEquals(expected.getMaxY(), actual.getMaxY());
			TestCase.assertEquals(expected.hasZ(), actual.hasZ());
			TestCase.assertEquals(expected.getMinZ(), actual.getMinZ());
			TestCase.assertEquals(expected.getMaxZ(), actual.getMaxZ());
			TestCase.assertEquals(expected.hasM(), actual.hasM());
			TestCase.assertEquals(expected.getMinM(), actual.getMinM());
			TestCase.assertEquals(expected.getMaxM(), actual.getMaxM());
		}

	}

	/**
	 * Compare two geometries and verify they are equal
	 * 
	 * @param expected
	 *            expected geometry
	 * @param actual
	 *            actual geometry
	 */
	public static void compareGeometries(Geometry expected, Geometry actual) {
		if (expected == null) {
			TestCase.assertNull(actual);
		} else {
			TestCase.assertNotNull(actual);

			GeometryType geometryType = expected.getGeometryType();
			switch (geometryType) {

			case GEOMETRY:
				TestCase.fail("Unexpected Geometry Type of "
						+ geometryType.name() + " which is abstract");
			case POINT:
				comparePoint((Point) actual, (Point) expected);
				break;
			case LINESTRING:
				compareLineString((LineString) expected, (LineString) actual);
				break;
			case POLYGON:
				comparePolygon((Polygon) expected, (Polygon) actual);
				break;
			case MULTIPOINT:
				compareMultiPoint((MultiPoint) expected, (MultiPoint) actual);
				break;
			case MULTILINESTRING:
				compareMultiLineString((MultiLineString) expected,
						(MultiLineString) actual);
				break;
			case MULTIPOLYGON:
				compareMultiPolygon((MultiPolygon) expected,
						(MultiPolygon) actual);
				break;
			case GEOMETRYCOLLECTION:
			case MULTICURVE:
			case MULTISURFACE:
				compareGeometryCollection((GeometryCollection<?>) expected,
						(GeometryCollection<?>) actual);
				break;
			case CIRCULARSTRING:
				compareCircularString((CircularString) expected,
						(CircularString) actual);
				break;
			case COMPOUNDCURVE:
				compareCompoundCurve((CompoundCurve) expected,
						(CompoundCurve) actual);
				break;
			case CURVEPOLYGON:
				compareCurvePolygon((CurvePolygon<?>) expected,
						(CurvePolygon<?>) actual);
				break;
			case CURVE:
				TestCase.fail("Unexpected Geometry Type of "
						+ geometryType.name() + " which is abstract");
			case SURFACE:
				TestCase.fail("Unexpected Geometry Type of "
						+ geometryType.name() + " which is abstract");
			case POLYHEDRALSURFACE:
				comparePolyhedralSurface((PolyhedralSurface) expected,
						(PolyhedralSurface) actual);
				break;
			case TIN:
				compareTIN((TIN) expected, (TIN) actual);
				break;
			case TRIANGLE:
				compareTriangle((Triangle) expected, (Triangle) actual);
				break;
			default:
				throw new RuntimeException(
						"Geometry Type not supported: " + geometryType);
			}
		}

		TestCase.assertEquals(expected, actual);
	}

	/**
	 * Compare to the base attributes of two geometries
	 * 
	 * @param expected
	 *            expected geometry
	 * @param actual
	 *            actual geometry
	 */
	public static void compareBaseGeometryAttributes(Geometry expected,
			Geometry actual) {
		TestCase.assertEquals(expected.getGeometryType(),
				actual.getGeometryType());
		TestCase.assertEquals(expected.hasZ(), actual.hasZ());
		TestCase.assertEquals(expected.hasM(), actual.hasM());
		TestCase.assertEquals(expected.isEmpty(), actual.isEmpty());
		TestCase.assertEquals(expected.getDimension(), actual.getDimension());
		TestCase.assertEquals(expected.is3D(), actual.is3D());
		TestCase.assertEquals(expected.isMeasured(), actual.isMeasured());
		TestCase.assertEquals(expected.getCentroid(), actual.getCentroid());
		TestCase.assertEquals(expected.getEnvelope(), actual.getEnvelope());
	}

	/**
	 * Compare the two points for equality
	 * 
	 * @param expected
	 *            expected point
	 * @param actual
	 *            actual point
	 */
	public static void comparePoint(Point expected, Point actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.getX(), actual.getX());
		TestCase.assertEquals(expected.getY(), actual.getY());
		TestCase.assertEquals(expected.getZ(), actual.getZ());
		TestCase.assertEquals(expected.getM(), actual.getM());
	}

	/**
	 * Compare the two line strings for equality
	 * 
	 * @param expected
	 *            expected line string
	 * @param actual
	 *            actual line string
	 */
	public static void compareLineString(LineString expected,
			LineString actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPoints(), actual.numPoints());
		for (int i = 0; i < expected.numPoints(); i++) {
			comparePoint(expected.getPoints().get(i),
					actual.getPoints().get(i));
		}
	}

	/**
	 * Compare the two polygons for equality
	 * 
	 * @param expected
	 *            expected polygon
	 * @param actual
	 *            actual polygon
	 */
	public static void comparePolygon(Polygon expected, Polygon actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numRings(), actual.numRings());
		for (int i = 0; i < expected.numRings(); i++) {
			compareLineString(expected.getRings().get(i),
					actual.getRings().get(i));
		}
	}

	/**
	 * Compare the two multi points for equality
	 * 
	 * @param expected
	 *            expected multi point
	 * @param actual
	 *            actual multi point
	 */
	public static void compareMultiPoint(MultiPoint expected,
			MultiPoint actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPoints(), actual.numPoints());
		for (int i = 0; i < expected.numPoints(); i++) {
			comparePoint(expected.getPoints().get(i),
					actual.getPoints().get(i));
		}
	}

	/**
	 * Compare the two multi line strings for equality
	 * 
	 * @param expected
	 *            expected multi line string
	 * @param actual
	 *            actual multi line string
	 */
	public static void compareMultiLineString(MultiLineString expected,
			MultiLineString actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numLineStrings(),
				actual.numLineStrings());
		for (int i = 0; i < expected.numLineStrings(); i++) {
			compareLineString(expected.getLineStrings().get(i),
					actual.getLineStrings().get(i));
		}
	}

	/**
	 * Compare the two multi polygons for equality
	 * 
	 * @param expected
	 *            expected multi polygon
	 * @param actual
	 *            actual multi polygon
	 */
	public static void compareMultiPolygon(MultiPolygon expected,
			MultiPolygon actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPolygons(), actual.numPolygons());
		for (int i = 0; i < expected.numPolygons(); i++) {
			comparePolygon(expected.getPolygons().get(i),
					actual.getPolygons().get(i));
		}
	}

	/**
	 * Compare the two geometry collections for equality
	 * 
	 * @param expected
	 *            expected geometry collection
	 * @param actual
	 *            actual geometry collection
	 */
	public static void compareGeometryCollection(GeometryCollection<?> expected,
			GeometryCollection<?> actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numGeometries(), actual.numGeometries());
		for (int i = 0; i < expected.numGeometries(); i++) {
			compareGeometries(expected.getGeometries().get(i),
					actual.getGeometries().get(i));
		}
	}

	/**
	 * Compare the two circular strings for equality
	 * 
	 * @param expected
	 *            expected circular string
	 * @param actual
	 *            actual circular string
	 */
	public static void compareCircularString(CircularString expected,
			CircularString actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPoints(), actual.numPoints());
		for (int i = 0; i < expected.numPoints(); i++) {
			comparePoint(expected.getPoints().get(i),
					actual.getPoints().get(i));
		}
	}

	/**
	 * Compare the two compound curves for equality
	 * 
	 * @param expected
	 *            expected compound curve
	 * @param actual
	 *            actual compound curve
	 */
	public static void compareCompoundCurve(CompoundCurve expected,
			CompoundCurve actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numLineStrings(),
				actual.numLineStrings());
		for (int i = 0; i < expected.numLineStrings(); i++) {
			compareLineString(expected.getLineStrings().get(i),
					actual.getLineStrings().get(i));
		}
	}

	/**
	 * Compare the two curve polygons for equality
	 * 
	 * @param expected
	 *            expected curve polygon
	 * @param actual
	 *            actual curve polygon
	 */
	public static void compareCurvePolygon(CurvePolygon<?> expected,
			CurvePolygon<?> actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numRings(), actual.numRings());
		for (int i = 0; i < expected.numRings(); i++) {
			compareGeometries(expected.getRings().get(i),
					actual.getRings().get(i));
		}
	}

	/**
	 * Compare the two polyhedral surfaces for equality
	 * 
	 * @param expected
	 *            expected polyhedral surface
	 * @param actual
	 *            actual polyhedral surface
	 */
	public static void comparePolyhedralSurface(PolyhedralSurface expected,
			PolyhedralSurface actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPolygons(), actual.numPolygons());
		for (int i = 0; i < expected.numPolygons(); i++) {
			compareGeometries(expected.getPolygons().get(i),
					actual.getPolygons().get(i));
		}
	}

	/**
	 * Compare the two TINs for equality
	 * 
	 * @param expected
	 *            expected TIN
	 * @param actual
	 *            actual TIN
	 */
	public static void compareTIN(TIN expected, TIN actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numPolygons(), actual.numPolygons());
		for (int i = 0; i < expected.numPolygons(); i++) {
			compareGeometries(expected.getPolygons().get(i),
					actual.getPolygons().get(i));
		}
	}

	/**
	 * Compare the two triangles for equality
	 * 
	 * @param expected
	 *            expected triangle
	 * @param actual
	 *            actual triangle
	 */
	public static void compareTriangle(Triangle expected, Triangle actual) {

		compareBaseGeometryAttributes(expected, actual);
		TestCase.assertEquals(expected.numRings(), actual.numRings());
		for (int i = 0; i < expected.numRings(); i++) {
			compareLineString(expected.getRings().get(i),
					actual.getRings().get(i));
		}
	}

	/**
	 * Write and compare the text of the geometries
	 * 
	 * @param expected
	 *            expected geometry
	 * @param actual
	 *            actual geometry
	 * @throws IOException
	 *             upon error
	 */
	public static void compareGeometryText(Geometry expected, Geometry actual)
			throws IOException {

		String expectedText = writeText(expected);
		String actualText = writeText(actual);

		compareText(expectedText, actualText);
	}

	/**
	 * Read and compare the text geometries
	 * 
	 * @param expected
	 *            expected text
	 * @param actual
	 *            actual text
	 * @throws IOException
	 *             upon error
	 */
	public static void compareByteGeometries(String expected, String actual)
			throws IOException {

		Geometry expectedGeometry = readGeometry(expected);
		Geometry actualGeometry = readGeometry(actual);

		compareGeometries(expectedGeometry, actualGeometry);
	}

	/**
	 * Write the geometry to text
	 * 
	 * @param geometry
	 *            geometry
	 * @return text
	 * @throws IOException
	 *             upon error
	 */
	public static String writeText(Geometry geometry) throws IOException {
		return GeometryWriter.writeGeometry(geometry);
	}

	/**
	 * Read a geometry from text
	 * 
	 * @param text
	 *            text
	 * @return geometry
	 * @throws IOException
	 *             upon error
	 */
	public static Geometry readGeometry(String text) throws IOException {
		return readGeometry(text, true);
	}

	/**
	 * Read a geometry from text
	 * 
	 * @param text
	 *            text
	 * @param validateZM
	 *            true to validate the geometry type info z and m values
	 * @return geometry
	 * @throws IOException
	 *             upon error
	 */
	public static Geometry readGeometry(String text, boolean validateZM)
			throws IOException {

		Geometry geometry = GeometryReader.readGeometry(text);

		TextReader reader = new TextReader(text);
		GeometryTypeInfo geometryTypeInfo = GeometryReader
				.readGeometryType(reader);
		reader.close();
		GeometryType expectedGeometryType = geometryTypeInfo.getGeometryType();
		switch (expectedGeometryType) {
		case MULTICURVE:
		case MULTISURFACE:
			expectedGeometryType = GeometryType.GEOMETRYCOLLECTION;
			break;
		default:
		}
		TestCase.assertEquals(expectedGeometryType, geometry.getGeometryType());
		if (validateZM) {
			TestCase.assertEquals(geometryTypeInfo.hasZ(), geometry.hasZ());
			TestCase.assertEquals(geometryTypeInfo.hasM(), geometry.hasM());
		}

		return geometry;
	}

	/**
	 * Compare two text strings and verify they are equal
	 * 
	 * @param expected
	 *            expected text
	 * @param actual
	 *            actual text
	 * @throws IOException
	 *             upon error
	 */
	public static void compareText(String expected, String actual)
			throws IOException {

		TextReader reader1 = new TextReader(expected);
		TextReader reader2 = new TextReader(actual);

		while (reader1.peekToken() != null) {
			String token1 = reader1.readToken();
			String token2 = reader2.readToken();
			if (!token1.equalsIgnoreCase(token2)) {
				try {
					double token1Double = Double.parseDouble(token1);
					double token2Double = Double.parseDouble(token2);
					TestCase.assertEquals(token1Double, token2Double, 0);
				} catch (NumberFormatException e) {
					TestCase.fail(
							"Expected: " + token1 + ", Actual: " + token2);
				}
			}

		}

		TestCase.assertNull(reader1.readToken());
		TestCase.assertNull(reader2.readToken());

		reader1.close();
		reader2.close();
	}

	/**
	 * Create a random point
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return point
	 */
	public static Point createPoint(boolean hasZ, boolean hasM) {

		double x = Math.random() * 180.0 * (Math.random() < .5 ? 1 : -1);
		double y = Math.random() * 90.0 * (Math.random() < .5 ? 1 : -1);

		Point point = new Point(hasZ, hasM, x, y);

		if (hasZ) {
			double z = Math.random() * 1000.0;
			point.setZ(z);
		}

		if (hasM) {
			double m = Math.random() * 1000.0;
			point.setM(m);
		}

		return point;
	}

	/**
	 * Create a random line string
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return line string
	 */
	public static LineString createLineString(boolean hasZ, boolean hasM) {
		return createLineString(hasZ, hasM, false);
	}

	/**
	 * Create a random line string
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @param ring
	 *            ring
	 * @return line string
	 */
	public static LineString createLineString(boolean hasZ, boolean hasM,
			boolean ring) {

		LineString lineString = new LineString(hasZ, hasM);

		int num = 2 + ((int) (Math.random() * 9));

		for (int i = 0; i < num; i++) {
			lineString.addPoint(createPoint(hasZ, hasM));
		}

		if (ring) {
			lineString.addPoint(lineString.getPoints().get(0));
		}

		return lineString;
	}

	/**
	 * Create a random polygon
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return polygon
	 */
	public static Polygon createPolygon(boolean hasZ, boolean hasM) {

		Polygon polygon = new Polygon(hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			polygon.addRing(createLineString(hasZ, hasM, true));
		}

		return polygon;
	}

	/**
	 * Create a random multi point
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return multi point
	 */
	public static MultiPoint createMultiPoint(boolean hasZ, boolean hasM) {

		MultiPoint multiPoint = new MultiPoint(hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			multiPoint.addPoint(createPoint(hasZ, hasM));
		}

		return multiPoint;
	}

	/**
	 * Create a random multi line string
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return multi line string
	 */
	public static MultiLineString createMultiLineString(boolean hasZ,
			boolean hasM) {

		MultiLineString multiLineString = new MultiLineString(hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			multiLineString.addLineString(createLineString(hasZ, hasM));
		}

		return multiLineString;
	}

	/**
	 * Create a random multi polygon
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return multi polygon
	 */
	public static MultiPolygon createMultiPolygon(boolean hasZ, boolean hasM) {

		MultiPolygon multiPolygon = new MultiPolygon(hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			multiPolygon.addPolygon(createPolygon(hasZ, hasM));
		}

		return multiPolygon;
	}

	/**
	 * Create a random geometry collection
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return geometry collection
	 */
	public static GeometryCollection<Geometry> createGeometryCollection(
			boolean hasZ, boolean hasM) {

		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {

			Geometry geometry = null;
			int randomGeometry = (int) (Math.random() * 6);

			switch (randomGeometry) {
			case 0:
				geometry = createPoint(hasZ, hasM);
				break;
			case 1:
				geometry = createLineString(hasZ, hasM);
				break;
			case 2:
				geometry = createPolygon(hasZ, hasM);
				break;
			case 3:
				geometry = createMultiPoint(hasZ, hasM);
				break;
			case 4:
				geometry = createMultiLineString(hasZ, hasM);
				break;
			case 5:
				geometry = createMultiPolygon(hasZ, hasM);
				break;
			}

			geometryCollection.addGeometry(geometry);
		}

		return geometryCollection;
	}

	/**
	 * Create a random compound curve
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return compound curve
	 */
	public static CompoundCurve createCompoundCurve(boolean hasZ,
			boolean hasM) {
		return createCompoundCurve(hasZ, hasM, false);
	}

	/**
	 * Create a random compound curve
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @param ring
	 *            is ring
	 * @return compound curve
	 */
	public static CompoundCurve createCompoundCurve(boolean hasZ, boolean hasM,
			boolean ring) {

		CompoundCurve compoundCurve = new CompoundCurve(hasZ, hasM);

		int num = 2 + ((int) (Math.random() * 9));

		for (int i = 0; i < num; i++) {
			compoundCurve.addLineString(createLineString(hasZ, hasM));
		}

		if (ring) {
			compoundCurve.getLineString(num - 1)
					.addPoint(compoundCurve.getLineString(0).startPoint());
		}

		return compoundCurve;
	}

	/**
	 * Create a random curve polygon
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 * @return polygon
	 */
	public static CurvePolygon<Curve> createCurvePolygon(boolean hasZ,
			boolean hasM) {

		CurvePolygon<Curve> curvePolygon = new CurvePolygon<>(hasZ, hasM);

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			curvePolygon.addRing(createCompoundCurve(hasZ, hasM, true));
		}

		return curvePolygon;
	}

	/**
	 * Create a random multi curve
	 * 
	 * @return multi curve
	 */
	public static GeometryCollection<Curve> createMultiCurve() {

		GeometryCollection<Curve> multiCurve = new GeometryCollection<>();

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			if (i % 2 == 0) {
				multiCurve.addGeometry(WKTTestUtils.createCompoundCurve(
						WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip()));
			} else {
				multiCurve.addGeometry(WKTTestUtils.createLineString(
						WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip()));
			}
		}

		return multiCurve;
	}

	/**
	 * Create a random multi surface
	 * 
	 * @return multi surface
	 */
	public static GeometryCollection<Surface> createMultiSurface() {

		GeometryCollection<Surface> multiSurface = new GeometryCollection<>();

		int num = 1 + ((int) (Math.random() * 5));

		for (int i = 0; i < num; i++) {
			if (i % 2 == 0) {
				multiSurface.addGeometry(WKTTestUtils.createCurvePolygon(
						WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip()));
			} else {
				multiSurface.addGeometry(WKTTestUtils.createPolygon(
						WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip()));
			}
		}

		return multiSurface;
	}

	/**
	 * Randomly return true or false
	 * 
	 * @return true or false
	 */
	public static boolean coinFlip() {
		return Math.random() < 0.5;
	}

}
