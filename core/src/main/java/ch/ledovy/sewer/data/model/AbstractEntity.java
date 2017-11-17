package ch.ledovy.sewer.data.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public class AbstractEntity implements Serializable, HasId<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private int version;
	
	@Override
	@Transient
	public boolean isNew() {
		return this.id == null;
	}
	
	@Override
	@Id
	@GeneratedValue
	public Long getId() {
		return this.id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	@Version
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(final int version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		if (this.id == null) {
			return super.hashCode();
		}
		
		return 31 + this.id.hashCode();
	}
	
	@Override
	public boolean equals(final Object other) {
		if (this.id == null) {
			// New entities are only equal if the instance if the same
			return super.equals(other);
		}
		
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractEntity)) {
			return false;
		}
		return this.id.equals(((AbstractEntity) other).id);
	}
	
}
