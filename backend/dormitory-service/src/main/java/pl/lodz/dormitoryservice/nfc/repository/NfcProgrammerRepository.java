package pl.lodz.dormitoryservice.nfc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormitoryservice.nfc.entity.NfcProgrammerEntity;


public interface NfcProgrammerRepository extends JpaRepository<NfcProgrammerEntity, Long> {
    NfcProgrammerEntity findByUuid(UUID uuid);
}
