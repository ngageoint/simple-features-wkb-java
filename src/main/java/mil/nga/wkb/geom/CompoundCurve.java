package mil.nga.wkb.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Compound Curve, Curve sub type
 * 
 * @author osbornb
 */
public class CompoundCurve extends Curve {

	/**
	 * List of line strings
	 */
	private List<LineString> lineStrings = new ArrayList<LineString>();

	/**
	 * Constructor
	 */
	public CompoundCurve() {
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
	public CompoundCurve(boolean hasZ, boolean hasM) {
		super(GeometryType.COMPOUNDCURVE, hasZ, hasM);
	}

	/**
	 * Constructor
	 * 
	 * @param compoundCurve
	 *            compound Curve to copy
	 */
	public CompoundCurve(CompoundCurve compoundCurve) {
		this(compoundCurve.hasZ(), compoundCurve.hasM());
		for (LineString lineString : compoundCurve.getLineStrings()) {
			addLineString((LineString) lineString.copy());
		}
	}

	/**
	 * Get the line strings
	 * 
	 * @return line strings
	 */
	public List<LineString> getLineStrings() {
		return lineStrings;
	}

	/**
	 * Set the line strings
	 * 
	 * @param lineStrings
	 *            line strings
	 */
	public void setLineStrings(List<LineString> lineStrings) {
		this.lineStrings = lineStrings;
	}

	/**
	 * Add a line string
	 * 
	 * @param lineString
	 *            line string
	 */
	public void addLineString(LineString lineString) {
		lineStrings.add(lineString);
	}

	/**
	 * Get the number of line strings
	 * 
	 * @return number of line strings
	 */
	public int numLineStrings() {
		return lineStrings.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Geometry copy() {
		return new CompoundCurve(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((lineStrings == null) ? 0 : lineStrings.hashCode());
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
		CompoundCurve other = (CompoundCurve) obj;
		if (lineStrings == null) {
			if (other.lineStrings != null)
				return false;
		} else if (!lineStrings.equals(other.lineStrings))
			return false;
		return true;
	}

}
