package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Neighborhood extends AbstractRecord<Attribute> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String neighborhood;
	
	public Neighborhood(String identifier, String provenance) {
		super(identifier, provenance);
	}
	
	public String getCuisine() {
		return neighborhood;
	}

	public void setCuisine(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 31 + ((neighborhood == null) ? 0 : neighborhood.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Neighborhood other = (Neighborhood) obj;
		if (neighborhood == null) {
			if (other.neighborhood != null)
				return false;
		} else if (!neighborhood.equals(other.neighborhood))
			return false;
		return true;
	}
	
	public static final Attribute NEIGHBORHOOD = new Attribute("Neighborhood");

	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
	 */
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NEIGHBORHOOD)
			return neighborhood!=null;
		return false;
	}
	

}
