package vms.vmsavg.entity;

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

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "machine_sensor_product")
public class MachineProductSensorJPA {

  @Id
  @GeneratedValue
  int id;

  @Column(name = "machine_id")
  int machineId;

  @Column(name = "product_id")
  int productId;

  @Column(name = "sensor_id")
  int sensorId;

  @Column(name = "product_name")
  String productName;

  public MachineProductSensorJPA(int machineId, int sensorId, int productId, String productName) {
    super();
    this.machineId = machineId;
    this.productId = productId;
    this.sensorId = sensorId;
    this.productName = productName;
  }

}