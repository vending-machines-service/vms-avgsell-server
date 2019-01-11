package vms.vmsavg.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vms.vmsavg.dto.MachineDTO;
import vms.vmsavg.dto.PersistanceConstants;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = PersistanceConstants.MACHINES_TABLE)
public class MachineJPA {

  @Id
  @Column(name = "machine_id")
  public int machineId;

  @Column(name = "firm_name")
  public String firmName;

  public String location;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "machine_id", referencedColumnName = "machine_id")
  List<MachineProductSensorJPA> products;

  public MachineJPA(int machineId, String firmName, String location) {
    super();
    this.machineId = machineId;
    this.firmName = firmName;
    this.location = location;
  }

  public MachineDTO convertJPAtoDTO() {

    Map<Integer, Integer> productSensor = new HashMap<>();
    for (MachineProductSensorJPA set : products) {
      productSensor.put(set.getSensorId(), set.getProductId());
    }
    return new MachineDTO(machineId, firmName, location, productSensor);
  }

}
