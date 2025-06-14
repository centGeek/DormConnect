package pl.lodz.dormitoryservice.nfc.mapper;


import pl.lodz.dormitoryservice.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.dormitoryservice.nfc.entity.NfcProgrammerEntity;

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
