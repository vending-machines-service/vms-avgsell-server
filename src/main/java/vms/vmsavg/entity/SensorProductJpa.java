package vms.vmsavg.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "selling")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SensorProductJpa {
	@Id
	@GeneratedValue
	int id;
	@Column(name = "date")
	public LocalDate date;
	@Column(name = "machine_id")
	public int machineId;
	@Column(name = "product_id")
	public int productId;
	public int quantity;

	public SensorProductJpa(LocalDate date, int machineId, int productId, int quantity) {
		super();
		this.date = date;
		this.machineId = machineId;
		this.productId = productId;
		this.quantity = quantity;
	}

}
