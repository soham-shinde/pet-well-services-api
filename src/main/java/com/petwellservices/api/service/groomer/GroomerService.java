
package com.petwellservices.api.service.groomer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.petwellservices.api.dto.GroomerDto;
import com.petwellservices.api.dto.SitterDto;
import com.petwellservices.api.dto.SlotDto;
import com.petwellservices.api.entities.Groomer;
import com.petwellservices.api.entities.GroomerAppointment;
import com.petwellservices.api.entities.Slot;
import com.petwellservices.api.entities.Sitter;
import com.petwellservices.api.enums.AppointmentStatus;
import com.petwellservices.api.enums.UserStatus;
import com.petwellservices.api.exception.ResourceNotFoundException;
import com.petwellservices.api.repository.GroomerAppointmentRepository;
import com.petwellservices.api.repository.GroomerRepository;
import com.petwellservices.api.repository.SlotRepository;
import com.petwellservices.api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GroomerService implements IGroomerService {

    private final GroomerRepository groomerRepository;

    private final SlotRepository slotRepository;
    private final UserRepository userRepository;

    private final GroomerAppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Groomer> getAllGroomers() {
        return groomerRepository.findAllByStatus(UserStatus.APPROVED);
    }

    @Override
    public List<Groomer> getGroomersByCityId(Long cityId) {
        return groomerRepository.findByCityCityIdAndStatus(cityId, UserStatus.APPROVED);
    }

    @Override
    public List<Groomer> getGroomersByAreaId(Long areaId) {
        return groomerRepository.findByAreaAreaIdAndStatus(areaId, UserStatus.APPROVED);
    }

    @Override
    public GroomerDto getGroomerInfoWithSlots(Long userId) {
        Groomer groomer = groomerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Groomer ID"));

        GroomerDto groomerDto = modelMapper.map(groomer, GroomerDto.class);

        List<Slot> slots = slotRepository.findByUserUserId(groomer.getUser().getUserId());

        // Check if each slot is booked in SitterAppointment
        List<SlotDto> slotDtos = slots.stream().map(slot -> {
            LocalDate today = LocalDate.now();
            boolean isBooked = appointmentRepository
                    .existsByGroomerGroomerIdAndDateAndSlotSlotId(groomer.getGroomerId(), today, slot.getSlotId());
            SlotDto slotDto = new SlotDto();
            slotDto.setSlotId(slot.getSlotId());
            slotDto.setSlotTime(slot.getSlotTime());
            slotDto.setAvailable(!isBooked); // Slot is available if not booked
            return slotDto;
        }).collect(Collectors.toList());

        groomerDto.setSlots(slotDtos);

        return groomerDto;
    }
    @Override
    public GroomerDto getGroomerInfoWithSlotsGroomerId(Long groomerId) {
        Groomer groomer = groomerRepository.findById(groomerId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Groomer ID"));

        GroomerDto groomerDto = modelMapper.map(groomer, GroomerDto.class);

        List<Slot> slots = slotRepository.findByUserUserId(groomer.getUser().getUserId());

        // Check if each slot is booked in SitterAppointment
        List<SlotDto> slotDtos = slots.stream().map(slot -> {
            LocalDate today = LocalDate.now();
            boolean isBooked = appointmentRepository
                    .existsByGroomerGroomerIdAndDateAndSlotSlotId(groomer.getGroomerId(), today, slot.getSlotId());
            SlotDto slotDto = new SlotDto();
            slotDto.setSlotId(slot.getSlotId());
            slotDto.setSlotTime(slot.getSlotTime());
            slotDto.setAvailable(!isBooked); // Slot is available if not booked
            return slotDto;
        }).collect(Collectors.toList());

        groomerDto.setSlots(slotDtos);

        return groomerDto;
    }

    @Override
    public GroomerAppointment createAppointment(GroomerAppointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<GroomerAppointment> getAppointmentsByGroomerId(Long groomerId) {
        return appointmentRepository.findByGroomerGroomerId(groomerId);
    }

    @Override
    public List<Slot> getSlotsByGroomerId(Long groomerId) {
        return slotRepository.findByUserUserId(groomerRepository.findById(groomerId).get().getGroomerId());
    }

    @Override
    public Groomer createGroomer(Groomer groomer) {
        return groomerRepository.save(groomer);
    }

    @Override
    public Groomer updateGroomerStatus(Long groomerId, String status) {
        Groomer groomer = groomerRepository.findById(groomerId)
                .orElseThrow(() -> new RuntimeException("Groomer not found"));
        groomer.setStatus(UserStatus.valueOf(status));
        return groomerRepository.save(groomer);
    }

    @Transactional
    @Override
    public Groomer deleteGroomer(Long groomerId) {
        Groomer groomer = groomerRepository.findById(groomerId)
                .orElseThrow(() -> new RuntimeException("Groomer not found"));
        appointmentRepository.deleteByGroomerGroomerId(groomerId);
        userRepository.deleteById(groomer.getUser().getUserId());
        groomerRepository.delete(groomer);
        return groomer;
    }

    @Override
    public Slot createSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    @Override
    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    @Override
    public GroomerAppointment updateAppointmentStatus(Long appointmentId, String status) {
        GroomerAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.valueOf(status));
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<GroomerDto> getGroomersByStatus(UserStatus status) {
        List<Groomer> groomer = groomerRepository.findByStatus(status);
        return groomer.stream()
                .map(g -> modelMapper.map(g, GroomerDto.class))
                .toList();
    }

  

}
