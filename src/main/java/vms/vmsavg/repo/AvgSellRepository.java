package vms.vmsavg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import vms.vmsavg.entity.SensorProductJpa;

public interface AvgSellRepository extends JpaRepository<SensorProductJpa, Integer> {

}
