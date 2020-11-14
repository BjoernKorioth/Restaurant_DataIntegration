package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Neighborhood extends AbstractRecord<Attribute> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	
	public Neighborhood(String identifier, String provenance) {
		super(identifier, provenance);
	}
	
	public String getNeighborhood() {
		return name;
	}

	public void setNeighborhood(String name) {
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 31 + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public static final Attribute NAME = new Attribute("Name");

	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
	 */
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return name!=null;
		return false;
	}
	

}
