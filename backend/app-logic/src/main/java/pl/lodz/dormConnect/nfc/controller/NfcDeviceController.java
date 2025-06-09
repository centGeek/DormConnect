package pl.lodz.dormConnect.nfc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormConnect.nfc.dto.NfcAccessRequestDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormConnect.nfc.service.NfcDeviceService;

@RestController
@RequestMapping("/api/nfc")
public class NfcController {

    private final NfcDeviceService nfcDeviceService;

    @Autowired
    public NfcController(NfcDeviceService nfcDeviceService) {
        this.nfcDeviceService = nfcDeviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<NfcDeviceRegisterDTO> registerDevice(@RequestBody NfcDeviceRegisterDTO deviceDTO) {
        try {
            NfcDeviceRegisterDTO savedEntity = nfcDeviceService.registerDevice(deviceDTO);
            return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/check-access")
    public ResponseEntity<NfcAccessRequestDTO> checkAccess(@RequestBody NfcAccessRequestDTO nfcAccessRequestDTO) {
        try {
            if (nfcDeviceService.checkAccess(nfcAccessRequestDTO)) {
                return new ResponseEntity<>(nfcAccessRequestDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<NfcDeviceUpdateDTO> updateDeviceStatus(@RequestBody NfcDeviceUpdateDTO deviceUpdateDTO) {
        try {
            NfcDeviceUpdateDTO updated = nfcDeviceService.updateDeviceStatus(deviceUpdateDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

}
