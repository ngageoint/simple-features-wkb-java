package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiguous collection of polygons which share common boundary segments.
 * 
 * @author osbornb
 */
public class PolyhedralSurface extends Surface {

	/**
	 * List of polygons
	 */
	private List<Polygon> polygons = new ArrayList<Polygon>();

	/**
	 * Constructor
	 */
	public PolyhedralSurface() {
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
	public PolyhedralSurface(boolean hasZ, boolean hasM) {
		super(GeometryType.POLYHEDRALSURFACE, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param polyhedralSurface
	 *            polyhedral surface to copy
	 */
	public PolyhedralSurface(PolyhedralSurface polyhedralSurface) {
		this(polyhedralSurface.hasZ(), polyhedralSurface.hasM());
		for (Polygon polygon : polyhedralSurface.getPolygons()) {
			addPolygon((Polygon) polygon.copy());
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
	protected PolyhedralSurface(GeometryType type, boolean hasZ, boolean hasM) {
		super(type, hasZ, hasM);
	}

	/**
	 * Get polygons
	 * 
	 * @return polygons
	 */
	public List<Polygon> getPolygons() {
		return polygons;
	}

	/**
	 * Set polygons
	 * 
	 * @param polygons
	 *            polygons
	 */
	public void setPolygons(List<Polygon> polygons) {
		this.polygons = polygons;
	}

	/**
	 * Add polygon
	 * 
	 * @param polygon
	 *            polygon
	 */
	public void addPolygon(Polygon polygon) {
		polygons.add(polygon);
	}

	/**
	 * Get the number of polygons
	 * 
	 * @return number of polygons
	 */
	public int numPolygons() {
		return polygons.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new PolyhedralSurface(this);
	}

}
