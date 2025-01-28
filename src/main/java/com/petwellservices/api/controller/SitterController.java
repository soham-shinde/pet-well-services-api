package com.petwellservices.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petwellservices.api.dto.SitterAppointmentDto;
import com.petwellservices.api.dto.SitterDto;
import com.petwellservices.api.entities.Sitter;
import com.petwellservices.api.entities.SitterAppointment;
import com.petwellservices.api.entities.Slot;
import com.petwellservices.api.entities.User;
import com.petwellservices.api.request.CreateSitterRequest;
import com.petwellservices.api.request.CreateSlotRequest;
import com.petwellservices.api.response.ApiResponse;
import com.petwellservices.api.service.appointment.ISitterAppointmentService;
import com.petwellservices.api.service.sitter.ISitterService;
import com.petwellservices.api.service.slot.ISlotService;
import com.petwellservices.api.service.user.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sitters")
public class SitterController {
    private final ISitterService sitterService;
    private final IUserService userService;

    private final ISitterAppointmentService sitterAppointmentService;

    private final ISlotService slotService;

    @GetMapping("/{sitterId}/appointments")
    public ResponseEntity<ApiResponse> getAllSitterAppointments(@PathVariable Long sitterId) {

        try {

            List<SitterAppointmentDto> appointments = sitterAppointmentService.getSitterAppointments(sitterId);
            return ResponseEntity.ok(new ApiResponse("success", appointments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/appointments/{appointmentId}/info")
    public ResponseEntity<ApiResponse> getPetInfoWithUserDetails(@PathVariable Long appointmentId) {
        try {

            SitterAppointment appointmentInfo = sitterAppointmentService.getAppointmentDetails(appointmentId);
            return ResponseEntity.ok(new ApiResponse("success", appointmentInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));
        }
    }

    /**
     * Update appointment status.
     */
    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(@PathVariable Long appointmentId,
            @RequestParam com.petwellservices.api.enums.AppointmentStatus status) {
        try {
            sitterService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(new ApiResponse("success", "Appointment status updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));
        }
    }

    /**
     * Create new slots for a sitter.
     */
    @PostMapping("/{userId}/slots")
    public ResponseEntity<ApiResponse> createSlots(@PathVariable Long userId,
            @Valid @RequestBody CreateSlotRequest request) {
        try {

            User user = userService.getUserById(userId);
            Slot newSlot = new Slot();
            newSlot.setSlotTime(request.getSlotTime());
            newSlot.setUserType(request.getUserType());
            newSlot.setUser(user);
            slotService.createSlot(userId, newSlot);
            return ResponseEntity.ok(new ApiResponse("success", "Slots created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));
        }

    }

    /**
     * Get list of all sitters.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllSitters() {

        try {

            List<Sitter> sitters = sitterService.getAllSitters();
            return ResponseEntity.ok(new ApiResponse("success", sitters));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));
        }
    }

    /**
     * Get sitter information along with slots.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getSitterInfoWithSlots(@PathVariable Long userId) {
        try {
            SitterDto sitter = sitterService.getSitterInfoWithSlots(userId);
            return ResponseEntity.ok(new ApiResponse("success", sitter));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));

        }
    }

    @GetMapping("/sitterId/{sitterId}")
    public ResponseEntity<ApiResponse> getSitterInfoWithSlotsBySitterId(@PathVariable Long sitterId) {
        try {
            SitterDto sitter = sitterService.getSitterInfoWithSlotsBySitterId(sitterId);
            return ResponseEntity.ok(new ApiResponse("success", sitter));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));

        }
    }

    @PutMapping("/{sitterId}")
    public ResponseEntity<ApiResponse> updateSitter(
            @PathVariable Long sitterId,
            @RequestBody @Valid CreateSitterRequest updateSitterRequest) {
        try {
            Sitter updatedSitter = sitterService.updateSitter(sitterId, updateSitterRequest);

            return ResponseEntity.ok(new ApiResponse("success", updatedSitter));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", e.getMessage()));

        }
    }
}
