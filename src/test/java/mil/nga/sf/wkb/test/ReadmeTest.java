package mil.nga.sf.wkb.test;

import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryType;
import mil.nga.sf.Point;
import mil.nga.sf.wkb.GeometryReader;
import mil.nga.sf.wkb.GeometryWriter;

/**
 * README example tests
 * 
 * @author osbornb
 */
public class ReadmeTest {

	/**
	 * Geometry
	 */
	private static final Geometry GEOMETRY = new Point(1.0, 1.0);

	/**
	 * {@link #GEOMETRY} bytes
	 */
	private static final byte[] BYTES = new byte[] { 0, 0, 0, 0, 1, 63, -16, 0,
			0, 0, 0, 0, 0, 63, -16, 0, 0, 0, 0, 0, 0 };

	/**
	 * Test read
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testRead() throws IOException {

		Geometry geometry = testRead(BYTES);

		TestCase.assertEquals(GEOMETRY, geometry);

	}

	/**
	 * Test read
	 * 
	 * @param bytes
	 *            bytes
	 * @return geometry
	 * @throws IOException
	 *             upon error
	 */
	private Geometry testRead(byte[] bytes) throws IOException {

		// byte[] bytes = ...

		Geometry geometry = GeometryReader.readGeometry(bytes);
		GeometryType geometryType = geometry.getGeometryType();

		return geometry;
	}

	/**
	 * Test write
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testWrite() throws IOException {

		byte[] bytes = testWrite(GEOMETRY);

		WKBTestUtils.compareByteArrays(BYTES, bytes);

	}

	/**
	 * Test write
	 * 
	 * @param geometry
	 *            geometry
	 * @return bytes
	 * @throws IOException
	 *             upon error
	 */
	private byte[] testWrite(Geometry geometry) throws IOException {

		// Geometry geometry = ...

		byte[] bytes = GeometryWriter.writeGeometry(geometry);

		return bytes;
	}

}
