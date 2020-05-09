package demo.data;

import org.springframework.data.repository.CrudRepository;

import demo.model.Charges;

public interface ChargeRepository extends CrudRepository<Charges, Long> {

	Charges findBydocName(String name);
}
