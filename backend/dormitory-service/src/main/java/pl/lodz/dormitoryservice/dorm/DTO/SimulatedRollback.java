package pl.lodz.dormitoryservice.dorm.DTO;

public class SimulatedRollback extends RuntimeException {
    private final DeleteRoomImpactPreviewDTO previewDTO;

    public SimulatedRollback(DeleteRoomImpactPreviewDTO dto) {
        this.previewDTO = dto;
    }

    public DeleteRoomImpactPreviewDTO getPreviewDTO() {
        return previewDTO;
    }
}
