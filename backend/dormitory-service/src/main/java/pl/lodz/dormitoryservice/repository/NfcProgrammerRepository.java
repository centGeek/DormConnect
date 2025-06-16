package pl.lodz.dormitoryservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.lodz.dormitoryservice.entity.NfcProgrammerEntity;



public interface NfcProgrammerRepository extends JpaRepository<NfcProgrammerEntity, Long> {
    NfcProgrammerEntity findByUuid(UUID uuid);
    List<NfcProgrammerEntity> findAll();
}
