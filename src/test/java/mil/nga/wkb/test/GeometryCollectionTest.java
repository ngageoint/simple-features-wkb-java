package mil.nga.wkb.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mil.nga.wkb.geom.Curve;
import mil.nga.wkb.geom.Geometry;
import mil.nga.wkb.geom.GeometryCollection;
import mil.nga.wkb.geom.GeometryType;
import mil.nga.wkb.geom.LineString;
import mil.nga.wkb.geom.MultiLineString;
import mil.nga.wkb.geom.MultiPoint;
import mil.nga.wkb.geom.MultiPolygon;
import mil.nga.wkb.geom.Point;
import mil.nga.wkb.geom.Polygon;
import mil.nga.wkb.geom.Surface;
import mil.nga.wkb.geom.extended.ExtendedGeometryCollection;

import org.junit.Test;

/**
 * Geometry Collection tests
 * 
 * @author osbornb
 */
public class GeometryCollectionTest {

	/**
	 * Constructor
	 */
	public GeometryCollectionTest() {

	}

	@Test
	public void testMultiPoint() throws IOException {

		List<Point> points = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			points.add(WKBTestUtils.createPoint(WKBTestUtils.coinFlip(),
					WKBTestUtils.coinFlip()));
		}

		List<Geometry> geometries = new ArrayList<>();
		geometries.addAll(points);

		MultiPoint multiPoint = new MultiPoint(points);
		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				geometries);

		TestCase.assertEquals(multiPoint.numPoints(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiPoint.numGeometries(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiPoint.getGeometries(),
				geometryCollection.getGeometries());

		TestCase.assertTrue(multiPoint.isMultiPoint());
		TestCase.assertEquals(GeometryType.MULTIPOINT,
				multiPoint.getCollectionType());
		TestCase.assertFalse(multiPoint.isMultiLineString());
		TestCase.assertFalse(multiPoint.isMultiCurve());
		TestCase.assertFalse(multiPoint.isMultiPolygon());
		TestCase.assertFalse(multiPoint.isMultiSurface());

		TestCase.assertTrue(geometryCollection.isMultiPoint());
		TestCase.assertEquals(GeometryType.MULTIPOINT,
				geometryCollection.getCollectionType());
		TestCase.assertFalse(geometryCollection.isMultiLineString());
		TestCase.assertFalse(geometryCollection.isMultiCurve());
		TestCase.assertFalse(geometryCollection.isMultiPolygon());
		TestCase.assertFalse(geometryCollection.isMultiSurface());

		MultiPoint multiPoint2 = geometryCollection.getAsMultiPoint();
		TestCase.assertEquals(multiPoint, multiPoint2);
		TestCase.assertEquals(multiPoint2, multiPoint.getAsMultiPoint());

		GeometryCollection<Geometry> geometryCollection2 = multiPoint
				.getAsGeometryCollection();
		TestCase.assertEquals(geometryCollection, geometryCollection2);
		TestCase.assertEquals(geometryCollection2,
				geometryCollection.getAsGeometryCollection());

		ExtendedGeometryCollection<Geometry> extendedGeometryCollection = new ExtendedGeometryCollection<>(
				geometryCollection);
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				extendedGeometryCollection.getGeometryType());
		TestCase.assertEquals(GeometryType.MULTIPOINT,
				extendedGeometryCollection.getCollectionType());
		TestCase.assertEquals(extendedGeometryCollection,
				new ExtendedGeometryCollection<>(geometryCollection));

	}

	@Test
	public void testMultiLineString() throws IOException {

		List<LineString> lineStrings = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lineStrings.add(WKBTestUtils.createLineString(
					WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip()));
		}

		List<Geometry> geometries = new ArrayList<>();
		geometries.addAll(lineStrings);

		MultiLineString multiLineString = new MultiLineString(lineStrings);
		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				geometries);

		TestCase.assertEquals(multiLineString.numLineStrings(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiLineString.numGeometries(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiLineString.getGeometries(),
				geometryCollection.getGeometries());

		TestCase.assertTrue(multiLineString.isMultiLineString());
		TestCase.assertTrue(multiLineString.isMultiCurve());
		TestCase.assertEquals(GeometryType.MULTILINESTRING,
				multiLineString.getCollectionType());
		TestCase.assertFalse(multiLineString.isMultiPoint());
		TestCase.assertFalse(multiLineString.isMultiPolygon());
		TestCase.assertFalse(multiLineString.isMultiSurface());

		TestCase.assertTrue(geometryCollection.isMultiLineString());
		TestCase.assertTrue(geometryCollection.isMultiCurve());
		TestCase.assertEquals(GeometryType.MULTILINESTRING,
				geometryCollection.getCollectionType());
		TestCase.assertFalse(geometryCollection.isMultiPoint());
		TestCase.assertFalse(geometryCollection.isMultiPolygon());
		TestCase.assertFalse(geometryCollection.isMultiSurface());

		MultiLineString multiLineString2 = geometryCollection
				.getAsMultiLineString();
		TestCase.assertEquals(multiLineString, multiLineString2);
		TestCase.assertEquals(multiLineString2,
				multiLineString.getAsMultiLineString());

		GeometryCollection<Geometry> geometryCollection2 = multiLineString
				.getAsGeometryCollection();
		TestCase.assertEquals(geometryCollection, geometryCollection2);
		TestCase.assertEquals(geometryCollection2,
				geometryCollection.getAsGeometryCollection());

		GeometryCollection<Curve> multiCurve = geometryCollection
				.getAsMultiCurve();
		TestCase.assertEquals(multiLineString.getGeometries(),
				multiCurve.getGeometries());
		GeometryCollection<Curve> multiCurve2 = multiLineString
				.getAsMultiCurve();
		TestCase.assertEquals(multiCurve, multiCurve2);

		ExtendedGeometryCollection<Geometry> extendedGeometryCollection = new ExtendedGeometryCollection<>(
				geometryCollection);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedGeometryCollection.getGeometryType());
		TestCase.assertEquals(GeometryType.MULTILINESTRING,
				extendedGeometryCollection.getCollectionType());
		TestCase.assertEquals(extendedGeometryCollection,
				new ExtendedGeometryCollection<>(geometryCollection));

	}

	@Test
	public void testMultiPolygon() throws IOException {

		List<Polygon> polygons = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			polygons.add(WKBTestUtils.createPolygon(WKBTestUtils.coinFlip(),
					WKBTestUtils.coinFlip()));
		}

		List<Geometry> geometries = new ArrayList<>();
		geometries.addAll(polygons);

		MultiPolygon multiPolygon = new MultiPolygon(polygons);
		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				geometries);

		TestCase.assertEquals(multiPolygon.numPolygons(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiPolygon.numGeometries(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiPolygon.getGeometries(),
				geometryCollection.getGeometries());

		TestCase.assertTrue(multiPolygon.isMultiPolygon());
		TestCase.assertTrue(multiPolygon.isMultiSurface());
		TestCase.assertEquals(GeometryType.MULTIPOLYGON,
				multiPolygon.getCollectionType());
		TestCase.assertFalse(multiPolygon.isMultiPoint());
		TestCase.assertFalse(multiPolygon.isMultiLineString());
		TestCase.assertFalse(multiPolygon.isMultiCurve());

		TestCase.assertTrue(geometryCollection.isMultiPolygon());
		TestCase.assertTrue(geometryCollection.isMultiSurface());
		TestCase.assertEquals(GeometryType.MULTIPOLYGON,
				geometryCollection.getCollectionType());
		TestCase.assertFalse(geometryCollection.isMultiPoint());
		TestCase.assertFalse(geometryCollection.isMultiLineString());
		TestCase.assertFalse(geometryCollection.isMultiCurve());

		MultiPolygon multiPolygon2 = geometryCollection.getAsMultiPolygon();
		TestCase.assertEquals(multiPolygon, multiPolygon2);
		TestCase.assertEquals(multiPolygon2, multiPolygon.getAsMultiPolygon());

		GeometryCollection<Geometry> geometryCollection2 = multiPolygon
				.getAsGeometryCollection();
		TestCase.assertEquals(geometryCollection, geometryCollection2);
		TestCase.assertEquals(geometryCollection2,
				geometryCollection.getAsGeometryCollection());

		GeometryCollection<Surface> multiSurface = geometryCollection
				.getAsMultiSurface();
		TestCase.assertEquals(multiPolygon.getGeometries(),
				multiSurface.getGeometries());
		GeometryCollection<Surface> multiSurface2 = multiPolygon
				.getAsMultiSurface();
		TestCase.assertEquals(multiSurface, multiSurface2);

		ExtendedGeometryCollection<Geometry> extendedGeometryCollection = new ExtendedGeometryCollection<>(
				geometryCollection);
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				extendedGeometryCollection.getGeometryType());
		TestCase.assertEquals(GeometryType.MULTIPOLYGON,
				extendedGeometryCollection.getCollectionType());
		TestCase.assertEquals(extendedGeometryCollection,
				new ExtendedGeometryCollection<>(geometryCollection));

	}

	@Test
	public void testMultiCurve() throws IOException {

		List<Curve> curves = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			if (i % 2 == 0) {
				curves.add(WKBTestUtils.createCompoundCurve(
						WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip()));
			} else {
				curves.add(WKBTestUtils.createLineString(
						WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip()));
			}
		}

		List<Geometry> geometries = new ArrayList<>();
		geometries.addAll(curves);

		GeometryCollection<Curve> multiCurve = new GeometryCollection<>(curves);
		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				geometries);

		TestCase.assertEquals(multiCurve.numGeometries(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiCurve.getGeometries(),
				geometryCollection.getGeometries());

		TestCase.assertTrue(multiCurve.isMultiCurve());
		TestCase.assertEquals(GeometryType.MULTICURVE,
				multiCurve.getCollectionType());
		TestCase.assertFalse(multiCurve.isMultiPoint());
		TestCase.assertFalse(multiCurve.isMultiLineString());
		TestCase.assertFalse(multiCurve.isMultiPolygon());
		TestCase.assertFalse(multiCurve.isMultiSurface());

		TestCase.assertTrue(geometryCollection.isMultiCurve());
		TestCase.assertEquals(GeometryType.MULTICURVE,
				geometryCollection.getCollectionType());
		TestCase.assertFalse(geometryCollection.isMultiPoint());
		TestCase.assertFalse(geometryCollection.isMultiLineString());
		TestCase.assertFalse(geometryCollection.isMultiPolygon());
		TestCase.assertFalse(geometryCollection.isMultiSurface());

		GeometryCollection<Curve> multiCurve2 = geometryCollection
				.getAsMultiCurve();
		TestCase.assertEquals(multiCurve, multiCurve2);
		TestCase.assertEquals(multiCurve2, multiCurve.getAsMultiCurve());

		GeometryCollection<Geometry> geometryCollection2 = multiCurve
				.getAsGeometryCollection();
		TestCase.assertEquals(geometryCollection, geometryCollection2);
		TestCase.assertEquals(geometryCollection2,
				geometryCollection.getAsGeometryCollection());

		ExtendedGeometryCollection<Geometry> extendedGeometryCollection = new ExtendedGeometryCollection<>(
				geometryCollection);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedGeometryCollection.getGeometryType());
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedGeometryCollection.getCollectionType());
		TestCase.assertEquals(extendedGeometryCollection,
				new ExtendedGeometryCollection<>(geometryCollection));

	}

	@Test
	public void testMultiSurface() throws IOException {

		List<Surface> surfaces = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			if (i % 2 == 0) {
				surfaces.add(WKBTestUtils.createCurvePolygon(
						WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip()));
			} else {
				surfaces.add(WKBTestUtils.createPolygon(
						WKBTestUtils.coinFlip(), WKBTestUtils.coinFlip()));
			}
		}

		List<Geometry> geometries = new ArrayList<>();
		geometries.addAll(surfaces);

		GeometryCollection<Surface> multiSurface = new GeometryCollection<>(
				surfaces);
		GeometryCollection<Geometry> geometryCollection = new GeometryCollection<Geometry>(
				geometries);

		TestCase.assertEquals(multiSurface.numGeometries(),
				geometryCollection.numGeometries());
		TestCase.assertEquals(multiSurface.getGeometries(),
				geometryCollection.getGeometries());

		TestCase.assertTrue(multiSurface.isMultiSurface());
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				multiSurface.getCollectionType());
		TestCase.assertFalse(multiSurface.isMultiPoint());
		TestCase.assertFalse(multiSurface.isMultiLineString());
		TestCase.assertFalse(multiSurface.isMultiCurve());
		TestCase.assertFalse(multiSurface.isMultiPolygon());

		TestCase.assertTrue(geometryCollection.isMultiSurface());
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				geometryCollection.getCollectionType());
		TestCase.assertFalse(geometryCollection.isMultiPoint());
		TestCase.assertFalse(geometryCollection.isMultiLineString());
		TestCase.assertFalse(geometryCollection.isMultiCurve());
		TestCase.assertFalse(geometryCollection.isMultiPolygon());

		GeometryCollection<Surface> multiSurface2 = geometryCollection
				.getAsMultiSurface();
		TestCase.assertEquals(multiSurface, multiSurface2);
		TestCase.assertEquals(multiSurface2, multiSurface.getAsMultiSurface());

		GeometryCollection<Geometry> geometryCollection2 = multiSurface
				.getAsGeometryCollection();
		TestCase.assertEquals(geometryCollection, geometryCollection2);
		TestCase.assertEquals(geometryCollection2,
				geometryCollection.getAsGeometryCollection());

		ExtendedGeometryCollection<Surface> extendedGeometryCollection = new ExtendedGeometryCollection<>(
				multiSurface);
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				extendedGeometryCollection.getGeometryType());
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				extendedGeometryCollection.getCollectionType());
		TestCase.assertEquals(extendedGeometryCollection,
				new ExtendedGeometryCollection<>(geometryCollection));

	}

}
