package mil.nga.wkb.geom;

import java.util.List;

import mil.nga.wkb.util.GeometryUtils;

/**
 * A restricted form of MultiSurface where each Surface in the collection must
 * be of type Polygon.
 * 
 * @author osbornb
 */
public class MultiPolygon extends MultiSurface<Polygon> {

	/**
	 * Constructor
	 */
	public MultiPolygon() {
		this(false, false);
	}

	/**
	 * Constructor
	 * 
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	public MultiPolygon(boolean hasZ, boolean hasM) {
		super(GeometryType.MULTIPOLYGON, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param polygons
	 *            list of polygons
	 */
	public MultiPolygon(List<Polygon> polygons) {
		this(GeometryUtils.hasZ(polygons), GeometryUtils.hasM(polygons));
		setPolygons(polygons);
	}

	/**
	 * Constructor
	 * 
	 * @param multiPolygon
	 *            multi polygon to copy
	 */
	public MultiPolygon(MultiPolygon multiPolygon) {
		this(multiPolygon.hasZ(), multiPolygon.hasM());
		for (Polygon polygon : multiPolygon.getPolygons()) {
			addPolygon((Polygon) polygon.copy());
		}
	}

	/**
	 * Get the polygons
	 * 
	 * @return polygons
	 */
	public List<Polygon> getPolygons() {
		return getGeometries();
	}

	/**
	 * Set the polygons
	 * 
	 * @param polygons
	 *            polygons
	 */
	public void setPolygons(List<Polygon> polygons) {
		setGeometries(polygons);
	}

	/**
	 * Add a polygon
	 * 
	 * @param polygon
	 *            polygon
	 */
	public void addPolygon(Polygon polygon) {
		addGeometry(polygon);
	}

	/**
	 * Get the number of polygons
	 * 
	 * @return number of polygons
	 */
	public int numPolygons() {
		return numGeometries();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new MultiPolygon(this);
	}

}
