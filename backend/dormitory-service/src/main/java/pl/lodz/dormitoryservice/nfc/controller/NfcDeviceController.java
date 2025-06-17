package pl.lodz.dormitoryservice.nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.dormitoryservice.nfc.dto.GetNfcDeviceDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcAccessRequestDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormitoryservice.nfc.service.NfcDeviceService;


@RestController
@RequestMapping("/api/nfc")
public class NfcDeviceController {

   private final NfcDeviceService nfcDeviceService;

   @Autowired
   public NfcDeviceController(NfcDeviceService nfcDeviceService) {
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

   @PostMapping("/room/check-access")
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


   @PostMapping("/common-room/check-access")
   public ResponseEntity<NfcAccessRequestDTO> checkCommonRoomAccess(
       @RequestBody NfcAccessRequestDTO nfcAccessRequestDTO) {
       try {
           if (nfcDeviceService.checkCommonRoomAccess(nfcAccessRequestDTO)) {
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

   @GetMapping("/get-devices")
   public ResponseEntity<List<GetNfcDeviceDTO>> getMethodName(@RequestParam String param) {
       try {
           List<GetNfcDeviceDTO> devices = nfcDeviceService.getNfcDevices();
           return ResponseEntity.ok(devices);
       } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }
}
