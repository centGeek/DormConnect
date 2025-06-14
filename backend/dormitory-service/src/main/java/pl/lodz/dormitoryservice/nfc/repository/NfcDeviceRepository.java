package pl.lodz.dormitoryservice.nfc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormitoryservice.nfc.entity.NfcDeviceEntity;

import java.util.UUID;

public interface NfcDeviceRepository extends JpaRepository<NfcDeviceEntity, Long> {
    NfcDeviceEntity findByUuid(UUID uuid);
}
