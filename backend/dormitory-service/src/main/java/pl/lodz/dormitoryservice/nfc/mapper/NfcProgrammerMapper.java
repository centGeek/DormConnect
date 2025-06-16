package pl.lodz.dormitoryservice.nfc.mapper;


import pl.lodz.dormitoryservice.entity.NfcProgrammerEntity;
import pl.lodz.dormitoryservice.nfc.dto.NfcProgrammerDTO;
import pl.lodz.dormitoryservice.nfc.dto.RegisterNfcProgrammerDTO;


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

    public static NfcProgrammerDTO entityToNfcProgrammerDTO(
        NfcProgrammerEntity nfcProgrammerEntity
    ) {
        return new NfcProgrammerDTO(
                nfcProgrammerEntity.getUuid().toString(),
                nfcProgrammerEntity.getPort(),
                nfcProgrammerEntity.getIpAddress(),
                nfcProgrammerEntity.getDeviceStatus(),
                nfcProgrammerEntity.getMacAddress()
        );
    }
    
}
