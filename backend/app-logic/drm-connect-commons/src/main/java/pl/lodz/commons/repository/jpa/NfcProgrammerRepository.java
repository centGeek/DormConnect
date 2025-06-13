package pl.lodz.commons.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.lodz.commons.entity.NfcProgrammerEntity;

public interface NfcProgrammerRepository extends JpaRepository<NfcProgrammerEntity, Long> {
    NfcProgrammerEntity findByUuid(UUID uuid);    
}
