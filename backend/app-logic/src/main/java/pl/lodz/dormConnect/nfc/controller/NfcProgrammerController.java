package pl.lodz.dormConnect.nfc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormConnect.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.dormConnect.nfc.service.NfcProgrammerService;

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
    @PostMapping("/program-card")
    public ResponseEntity<NfcProgramCardDTO> programCard(
            @RequestBody NfcProgramCardDTO nfcProgramCardDTO) {
        try {
            if (nfcProgramCardDTO == null || nfcProgramCardDTO.deviceUuid() == null) {
                throw new IllegalArgumentException("Invalid input: nfcProgramCardDTO or deviceUuid cannot be null");
            }
            NfcProgramCardDTO programmedCard = nfcProgrammerService.programCard(nfcProgramCardDTO);

            return ResponseEntity.ok(programmedCard);
        } catch (Exception e) {
            throw new RuntimeException("Error programming NFC card: " + e.getMessage(), e);
        }
    }
}
