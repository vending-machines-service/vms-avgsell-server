package vms.vmsavg.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import vms.vmsavg.entity.MachineJPA;

public interface MachinesSqlRepository extends JpaRepository<MachineJPA, Integer> {

}
