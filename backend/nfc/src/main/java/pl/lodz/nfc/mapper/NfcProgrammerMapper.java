package pl.lodz.nfc.mapper;


import pl.lodz.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.nfc.entity.NfcProgrammerEntity;

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
