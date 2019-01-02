package vms.avgsell.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class SensorData {
	
	public int machineId;
	public int sensorId;
	public int value;
	
	public SensorData(int machineId, int sensorId, int value) {
		this.machineId = machineId;
		this.sensorId = sensorId;
		this.value = value;
	}
	
}
