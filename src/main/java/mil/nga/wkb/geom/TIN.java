package mil.nga.wkb.geom;

/**
 * A tetrahedron (4 triangular faces), corner at the origin and each unit
 * coordinate digit.
 * 
 * @author osbornb
 */
public class TIN extends PolyhedralSurface {

	/**
	 * Constructor
	 */
	public TIN() {
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
	public TIN(boolean hasZ, boolean hasM) {
		super(GeometryType.TIN, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param tin
	 *            tin to copy
	 */
	public TIN(TIN tin) {
		this(tin.hasZ(), tin.hasM());
		for (Polygon polygon : tin.getPolygons()) {
			addPolygon((Polygon) polygon.copy());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new TIN(this);
	}

}
