package vms.vmsavg.dto;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MachineDTO {

  public int machineId;
  public String firmName;
  public String location;
  public Map<Integer, Integer> sensorProduct;

  public MachineDTO(int machineId, String firmName, String location, Map<Integer, Integer> sensorProduct) {
    super();
    this.machineId = machineId;
    this.firmName = firmName;
    this.location = location;
    this.sensorProduct = sensorProduct;
  }
}
