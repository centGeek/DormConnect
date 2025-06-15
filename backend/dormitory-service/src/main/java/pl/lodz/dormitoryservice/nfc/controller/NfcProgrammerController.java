package pl.lodz.dormitoryservice.nfc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.dormitoryservice.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.ProgrammedCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.dormitoryservice.nfc.service.NfcProgrammerService;


@RestController
@RequestMapping("/api/nfc-programmer")
public class NfcProgrammerController {

   private final NfcProgrammerService nfcProgrammerService;

   public NfcProgrammerController(NfcProgrammerService nfcProgrammerService) {
       this.nfcProgrammerService = nfcProgrammerService;
   }

   // nfc device regisrers here by sending post request
   @PostMapping("/register")
   public ResponseEntity<RegisterNfcProgrammerDTO> registerNfcProgrammerDTO(
           @RequestBody RegisterNfcProgrammerDTO registerNfcProgrammerDTO) {
       try {
           if (registerNfcProgrammerDTO == null) {
               throw new IllegalArgumentException("Invalid input: registerNfcProgrammerDTO cannot be null");
           }
           RegisterNfcProgrammerDTO registeredProgrammer = nfcProgrammerService
                   .registerNfcProgrammer(registerNfcProgrammerDTO);
           return ResponseEntity.ok(registeredProgrammer);

       } catch (Exception e) {
           throw new RuntimeException("Error registering NFC programmer: " + e.getMessage(), e);
       }
   }

   // backend sends post request to nfc device to program the card
   // the nfc device sends response containging:
   // 1. programmer device uuid
   // 2. card uid - unique identifier of the card, each user has a diffrent card uid
   // 3. user uuid
   @PostMapping("/program-card")
   public ResponseEntity<ProgrammedCardDTO> programCard(
           @RequestBody NfcProgramCardDTO nfcProgramCardDTO,
           @RequestHeader("Authorization") String authorizationHeader) {
       try {
           if (nfcProgramCardDTO == null || nfcProgramCardDTO.deviceUuid() == null) {
               throw new IllegalArgumentException("Invalid input: nfcProgramCardDTO or deviceUuid cannot be null");
           }
           ProgrammedCardDTO programmedCard = nfcProgrammerService.programCard(nfcProgramCardDTO);

           return ResponseEntity.ok(programmedCard);
       } catch (Exception e) {
           throw new RuntimeException("Error programming NFC card: " + e.getMessage(), e);
       }
   }
}
