package vms.vmsavg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vms.vmsavg.entity.MachineProductSensorJPA;

public interface SensorProductRepository extends JpaRepository<MachineProductSensorJPA, Integer> {

	@Query(value = "SELECT * FROM machine_sensor_product  WHERE machine_id = :machineId", nativeQuery = true)
	List<MachineProductSensorJPA> selectProductInMachine(@Param("machineId") int machineId);
}
