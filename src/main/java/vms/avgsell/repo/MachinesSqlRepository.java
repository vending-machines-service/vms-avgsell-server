package vms.avgsell.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import vms.avgsell.entity.MachineJPA;

public interface MachinesSqlRepository extends JpaRepository<MachineJPA, Integer> {

}
