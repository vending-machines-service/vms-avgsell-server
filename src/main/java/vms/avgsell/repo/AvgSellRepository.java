package vms.avgsell.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import vms.avgsell.entity.SensorProductJpa;

public interface AvgSellRepository extends JpaRepository<SensorProductJpa, Integer> {

}
