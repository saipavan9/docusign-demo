package demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Charges {

	
	@Id
	@GeneratedValue
	private long id;
	
	private String docName;
	
	private long price;
	
}
