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
import com.petwellservices.api.dto.VeterinaryDto;
import com.petwellservices.api.entities.Slot;
import com.petwellservices.api.entities.User;
import com.petwellservices.api.entities.Veterinary;
import com.petwellservices.api.entities.VeterinaryAppointment;
import com.petwellservices.api.enums.AppointmentStatus;
import com.petwellservices.api.request.CreateSlotRequest;
import com.petwellservices.api.request.CreateVeterinaryRequest;
import com.petwellservices.api.response.ApiResponse;
import com.petwellservices.api.service.appointment.IVeterinaryAppointmentService;
import com.petwellservices.api.service.slot.ISlotService;
import com.petwellservices.api.service.user.IUserService;
import com.petwellservices.api.service.veterinary.IVeterinaryService;
import com.petwellservices.api.util.Constants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/veterinary")
public class VeterinaryController {
    private final IVeterinaryService veterinaryService;
    private final IUserService userService;

    private final IVeterinaryAppointmentService veterinaryAppointmentService;

    private final ISlotService slotService;

    @GetMapping("/{userId}/appointments")
    public ResponseEntity<ApiResponse> getAllVeterinaryAppointments(@PathVariable Long userId) {

        try {

            List<SitterAppointmentDto> appointments = veterinaryAppointmentService.getVeterinaryAppointments(userId);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, appointments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }
    }

    /**
     * Get pet information along with user details by appointment ID.
     */
    @GetMapping("/appointments/{appointmentId}/info")
    public ResponseEntity<ApiResponse> getPetInfoWithUserDetails(@PathVariable Long appointmentId) {
        try {

            VeterinaryAppointment appointmentInfo = veterinaryAppointmentService.getAppointmentDetails(appointmentId);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, appointmentInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }
    }

    /**
     * Update appointment status.
     */
    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(@PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {
        try {
            veterinaryService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS,  Constants.UPDATE_APPOINTMENT_SUCCESS));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }
    }

    /**
     * Create new slots for a veterinary.
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
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS,  Constants.SLOTS_CREATED));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }

    }

    /**
     * Get list of all veterinaries.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllVeterinaries() {

        try {

            List<Veterinary> veterinaries = veterinaryService.getAllVeterinaries();
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, veterinaries));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }
    }

    /**
     * Get veterinary information along with slots.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getVeterinaryInfoWithSlots(@PathVariable Long userId) {
        try {
            VeterinaryDto veterinary = veterinaryService.getVeterinaryInfoWithSlots(userId);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, veterinary));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));

        }
    }

    @GetMapping("/veterinaryId/{veterinaryId}")
    public ResponseEntity<ApiResponse> getVeterinaryInfoWithSlotsByVeterinaryId(@PathVariable Long veterinaryId) {
        try {
            VeterinaryDto veterinary = veterinaryService.getVeterinaryInfoWithSlotsByVeterinaryId(veterinaryId);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, veterinary));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));

        }
    }

    @PutMapping("/{veterinaryId}")
    public ResponseEntity<ApiResponse> updateVeterinary(
            @PathVariable Long veterinaryId,
            @RequestBody @Valid CreateVeterinaryRequest updateVeterinaryRequest) {
        try {
            Veterinary updatedVeterinary = veterinaryService.updateVeterinary(veterinaryId, updateVeterinaryRequest);
            return ResponseEntity.ok(new ApiResponse(Constants.SUCCESS, updatedVeterinary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(Constants.ERROR, e.getMessage()));
        }
    }
}
