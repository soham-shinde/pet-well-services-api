
package com.petwellservices.api.service.veterinary;

import java.util.List;
import java.util.Optional;

import com.petwellservices.api.dto.VeterinaryDto;
import com.petwellservices.api.entities.Slot;
import com.petwellservices.api.entities.Veterinary;
import com.petwellservices.api.entities.VeterinaryAppointment;
import com.petwellservices.api.enums.AppointmentStatus;
import com.petwellservices.api.enums.UserStatus;

public interface IVeterinaryService {
    List<VeterinaryAppointment> getAllAppointments(Long veterinaryId);
    List<Veterinary> getAllVeterinaries();
    List<Veterinary> getVeterinariesByCityId(Long cityId);
    List<Veterinary> getVeterinariesByAreaId(Long areaId);
    VeterinaryDto getVeterinaryInfoWithSlots(Long userId);
    VeterinaryDto getVeterinaryInfoWithSlotsByVeterinaryId(Long veterinaryId);
    void updateRequestStatus(Long veterinaryId, UserStatus status);
    void deleteVeterinary(Long veterinaryId);
    Slot createSlot(Slot slot);
    List<Slot> getSlotsByVeterinary(Long userId);
    VeterinaryAppointment createAppointment(VeterinaryAppointment appointment);
    void updateAppointmentStatus(Long appointmentId, AppointmentStatus status);
    List<VeterinaryDto> getVeterinariesByStatus(UserStatus status);
}
