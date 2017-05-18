package mil.nga.wkb.geom;

/**
 * A restricted form of CurvePolygon where each ring is defined as a simple,
 * closed LineString.
 * 
 * @author osbornb
 */
public class Polygon extends CurvePolygon<LineString> {

	/**
	 * Constructor
	 */
	public Polygon() {
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
	public Polygon(boolean hasZ, boolean hasM) {
		super(GeometryType.POLYGON, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param polygon
	 *            polygon to copy
	 */
	public Polygon(Polygon polygon) {
		this(polygon.hasZ(), polygon.hasM());
		for (LineString ring : polygon.getRings()) {
			addRing((LineString) ring.copy());
		}
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 *            geometry type
	 * @param hasZ
	 *            has z
	 * @param hasM
	 *            has m
	 */
	protected Polygon(GeometryType type, boolean hasZ, boolean hasM) {
		super(type, hasZ, hasM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new Polygon(this);
	}

}
