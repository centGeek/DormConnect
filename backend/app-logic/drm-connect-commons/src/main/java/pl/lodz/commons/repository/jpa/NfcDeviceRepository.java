package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.lodz.commons.entity.NfcDeviceEntity;

import java.util.UUID;

public interface NfcDeviceRepository extends JpaRepository<NfcDeviceEntity, Long> {
    NfcDeviceEntity findByUuid(UUID uuid);
}
