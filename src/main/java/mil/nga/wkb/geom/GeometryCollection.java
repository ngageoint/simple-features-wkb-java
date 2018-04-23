package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

import mil.nga.wkb.util.GeometryUtils;
import mil.nga.wkb.util.WkbException;

/**
 * A collection of zero or more Geometry instances.
 * 
 * @author osbornb
 */
public class GeometryCollection<T extends Geometry> extends Geometry {

	/**
	 * List of geometries
	 */
	private List<T> geometries = new ArrayList<T>();

	/**
	 * Constructor
	 */
	public GeometryCollection() {
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
	public GeometryCollection(boolean hasZ, boolean hasM) {
		super(GeometryType.GEOMETRYCOLLECTION, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param geometries
	 *            list of geometries
	 */
	public GeometryCollection(List<T> geometries) {
		this(GeometryUtils.hasZ(geometries), GeometryUtils.hasM(geometries));
		setGeometries(geometries);
	}

	/**
	 * Constructor
	 * 
	 * @param geometryCollection
	 *            geometry collection to copy
	 */
	public GeometryCollection(GeometryCollection<T> geometryCollection) {
		this(geometryCollection.hasZ(), geometryCollection.hasM());
		for (T geometry : geometryCollection.getGeometries()) {
			@SuppressWarnings("unchecked")
			T geometryCopy = (T) geometry.copy();
			addGeometry(geometryCopy);
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
	protected GeometryCollection(GeometryType type, boolean hasZ, boolean hasM) {
		super(type, hasZ, hasM);
	}

	/**
	 * Get the list of geometries
	 * 
	 * @return geometries
	 */
	public List<T> getGeometries() {
		return geometries;
	}

	/**
	 * Set the geometries
	 * 
	 * @param geometries
	 *            geometries
	 */
	public void setGeometries(List<T> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Add a geometry
	 * 
	 * @param geometry
	 *            geometry
	 */
	public void addGeometry(T geometry) {
		geometries.add(geometry);
	}

	/**
	 * Get the number of geometries in the collection
	 * 
	 * @return number of geometries
	 */
	public int numGeometries() {
		return geometries.size();
	}

	/**
	 * Get the collection type by evaluating the geometries
	 * 
	 * @return collection geometry type, one of: {@link GeometryType#MULTIPOINT}
	 *         , {@link GeometryType#MULTILINESTRING},
	 *         {@link GeometryType#MULTIPOLYGON},
	 *         {@link GeometryType#MULTICURVE},
	 *         {@link GeometryType#MULTISURFACE},
	 *         {@link GeometryType#GEOMETRYCOLLECTION}
	 */
	public GeometryType getCollectionType() {

		GeometryType geometryType = getGeometryType();

		switch (geometryType) {
		case MULTIPOINT:
		case MULTILINESTRING:
		case MULTIPOLYGON:
			break;
		case GEOMETRYCOLLECTION:
		case MULTICURVE:
		case MULTISURFACE:
			if (isMultiPoint()) {
				geometryType = GeometryType.MULTIPOINT;
			} else if (isMultiLineString()) {
				geometryType = GeometryType.MULTILINESTRING;
			} else if (isMultiPolygon()) {
				geometryType = GeometryType.MULTIPOLYGON;
			} else if (isMultiCurve()) {
				geometryType = GeometryType.MULTICURVE;
			} else if (isMultiSurface()) {
				geometryType = GeometryType.MULTISURFACE;
			}
			break;
		default:
			throw new WkbException("Unexpected Geometry Collection Type: "
					+ geometryType);
		}

		return geometryType;
	}

	/**
	 * Determine if this geometry collection is a {@link MultiPoint} instance or
	 * contains only {@link Point} geometries
	 * 
	 * @return true if a multi point or contains only points
	 */
	public boolean isMultiPoint() {
		boolean isMultiPoint = this instanceof MultiPoint;
		if (!isMultiPoint) {
			isMultiPoint = isCollectionOfType(Point.class);
		}
		return isMultiPoint;
	}

	/**
	 * Get as a {@link MultiPoint}, either the current instance or newly created
	 * from the {@link Point} geometries
	 * 
	 * @return multi point
	 */
	public MultiPoint getAsMultiPoint() {
		MultiPoint multiPoint;
		if (this instanceof MultiPoint) {
			multiPoint = (MultiPoint) this;
		} else {
			@SuppressWarnings("unchecked")
			MultiPoint newMultiPoint = new MultiPoint((List<Point>) geometries);
			multiPoint = newMultiPoint;
		}
		return multiPoint;
	}

	/**
	 * Determine if this geometry collection is a {@link MultiLineString}
	 * instance or contains only {@link LineString} geometries
	 * 
	 * @return true if a multi line string or contains only line strings
	 */
	public boolean isMultiLineString() {
		boolean isMultiLineString = this instanceof MultiLineString;
		if (!isMultiLineString) {
			isMultiLineString = isCollectionOfType(LineString.class);
		}
		return isMultiLineString;
	}

	/**
	 * Get as a {@link MultiLineString}, either the current instance or newly
	 * created from the {@link LineString} geometries
	 * 
	 * @return multi line string
	 */
	public MultiLineString getAsMultiLineString() {
		MultiLineString multiLineString;
		if (this instanceof MultiLineString) {
			multiLineString = (MultiLineString) this;
		} else {
			@SuppressWarnings("unchecked")
			MultiLineString newMultiLineString = new MultiLineString(
					(List<LineString>) geometries);
			multiLineString = newMultiLineString;
		}
		return multiLineString;
	}

	/**
	 * Determine if this geometry collection is a {@link MultiPolygon} instance
	 * or contains only {@link Polygon} geometries
	 * 
	 * @return true if a multi polygon or contains only polygons
	 */
	public boolean isMultiPolygon() {
		boolean isMultiPolygon = this instanceof MultiPolygon;
		if (!isMultiPolygon) {
			isMultiPolygon = isCollectionOfType(Polygon.class);
		}
		return isMultiPolygon;
	}

	/**
	 * Get as a {@link MultiPolygon}, either the current instance or newly
	 * created from the {@link Polygon} geometries
	 * 
	 * @return multi polygon
	 */
	public MultiPolygon getAsMultiPolygon() {
		MultiPolygon multiPolygon;
		if (this instanceof MultiPolygon) {
			multiPolygon = (MultiPolygon) this;
		} else {
			@SuppressWarnings("unchecked")
			MultiPolygon newMultiPolygon = new MultiPolygon(
					(List<Polygon>) geometries);
			multiPolygon = newMultiPolygon;
		}
		return multiPolygon;
	}

	/**
	 * Determine if this geometry collection contains only {@link Curve}
	 * geometries
	 * 
	 * @return true if contains only curves
	 */
	public boolean isMultiCurve() {
		boolean isMultiCurve = this instanceof MultiLineString;
		if (!isMultiCurve) {
			isMultiCurve = isCollectionOfType(Curve.class);
		}
		return isMultiCurve;
	}

	/**
	 * Get as a Multi Curve, a {@link Curve} typed Geometry Collection
	 * 
	 * @return multi curve
	 */
	public GeometryCollection<Curve> getAsMultiCurve() {
		GeometryCollection<Curve> multiCurve;
		if (this instanceof MultiLineString) {
			@SuppressWarnings("unchecked")
			GeometryCollection<Curve> castMultiCurve = (GeometryCollection<Curve>) new GeometryCollection<>(
					getGeometries());
			multiCurve = castMultiCurve;
		} else {
			@SuppressWarnings("unchecked")
			GeometryCollection<Curve> castMultiCurve = (GeometryCollection<Curve>) this;
			multiCurve = castMultiCurve;
			if (!multiCurve.getGeometries().isEmpty()) {
				@SuppressWarnings("unused")
				Curve curve = multiCurve.getGeometries().get(0);
			}
		}
		return multiCurve;
	}

	/**
	 * Determine if this geometry collection contains only {@link Surface}
	 * geometries
	 * 
	 * @return true if contains only surfaces
	 */
	public boolean isMultiSurface() {
		boolean isMultiSurface = this instanceof MultiPolygon;
		if (!isMultiSurface) {
			isMultiSurface = isCollectionOfType(Surface.class);
		}
		return isMultiSurface;
	}

	/**
	 * Get as a Multi Surface, a {@link Surface} typed Geometry Collection
	 * 
	 * @return multi surface
	 */
	public GeometryCollection<Surface> getAsMultiSurface() {
		GeometryCollection<Surface> multiSurface;
		if (this instanceof MultiPolygon) {
			@SuppressWarnings("unchecked")
			GeometryCollection<Surface> castMultiSurface = (GeometryCollection<Surface>) new GeometryCollection<>(
					getGeometries());
			multiSurface = castMultiSurface;
		} else {
			@SuppressWarnings("unchecked")
			GeometryCollection<Surface> castMultiSurface = (GeometryCollection<Surface>) this;
			multiSurface = castMultiSurface;
			if (!multiSurface.getGeometries().isEmpty()) {
				@SuppressWarnings("unused")
				Surface surface = multiSurface.getGeometries().get(0);
			}
		}
		return multiSurface;
	}

	/**
	 * Get as a top level Geometry Collection
	 * 
	 * @return geometry collection
	 */
	public GeometryCollection<Geometry> getAsGeometryCollection() {
		GeometryCollection<Geometry> geometryCollection;
		if (GeometryCollection.class.equals(this.getClass())) {
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> castGeometryCollection = (GeometryCollection<Geometry>) this;
			geometryCollection = castGeometryCollection;
		} else {
			@SuppressWarnings("unchecked")
			GeometryCollection<Geometry> castGeometryCollection = (GeometryCollection<Geometry>) new GeometryCollection<>(
					getGeometries());
			geometryCollection = castGeometryCollection;
		}
		return geometryCollection;
	}

	/**
	 * Determine if the geometries in this collection are made up only of the
	 * provided geometry class type
	 * 
	 * @param type
	 *            geometry class type
	 * @return true if a collection of the type
	 */
	private <TType extends Geometry> boolean isCollectionOfType(
			Class<TType> type) {

		boolean isType = true;

		for (T geometry : geometries) {
			if (!type.isAssignableFrom(geometry.getClass())) {
				isType = false;
				break;
			}
		}

		return isType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new GeometryCollection<T>(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((geometries == null) ? 0 : geometries.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		GeometryCollection<T> other = (GeometryCollection<T>) obj;
		if (geometries == null) {
			if (other.geometries != null)
				return false;
		} else if (!geometries.equals(other.geometries))
			return false;
		return true;
	}

}
