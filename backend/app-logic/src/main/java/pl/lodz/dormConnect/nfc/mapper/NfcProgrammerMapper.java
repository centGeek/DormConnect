package pl.lodz.dormConnect.nfc.mapper;

import pl.lodz.commons.entity.NfcProgrammerEntity;
import pl.lodz.dormConnect.nfc.dto.RegisterNfcProgrammerDTO;

public class NfcProgrammerMapper {

    public static RegisterNfcProgrammerDTO toRegisterNfcProgrammerDTO(
        NfcProgrammerEntity nfcProgrammerEntity
    ) {
        return new RegisterNfcProgrammerDTO(
                nfcProgrammerEntity.getUuid(),
                nfcProgrammerEntity.getPort(),
                nfcProgrammerEntity.getIpAddress(),
                nfcProgrammerEntity.getDeviceStatus()
        );
    }
    
}
