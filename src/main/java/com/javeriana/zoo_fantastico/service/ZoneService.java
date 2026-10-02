package com.javeriana.zoo_fantastico.service;
import com.javeriana.zoo_fantastico.dto.ZoneResponse;
import com.javeriana.zoo_fantastico.entity.Zone;
import com.javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final CreatureRepository creatureRepository;
    public ZoneService(
            ZoneRepository zoneRepository,
            CreatureRepository creatureRepository) {
        this.zoneRepository = zoneRepository;
        this.creatureRepository = creatureRepository;
    }
    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }
    public List<ZoneResponse> getAllZonesWithCount() {
        return zoneRepository.findAll()
                .stream()
                .map(zone -> {
                    int count = (int) creatureRepository.findAll()
                            .stream()
                            .filter(c -> c.getZone() != null)
                            .filter(c ->
                                    c.getZone()
                                            .getId()
                                            .equals(zone.getId()))
                            .count();
                    return new ZoneResponse(
                            zone.getId(),
                            zone.getName(),
                            zone.getDescription(),
                            zone.getCapacity(),
                            count
                    );
                })
                .toList();
    }
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Zone not found"));
    }
    public Zone updateZone(
            Long id,
            Zone updatedZone) {
        Zone zone = getZoneById(id);
        zone.setName(updatedZone.getName());
        zone.setDescription(updatedZone.getDescription());
        zone.setCapacity(updatedZone.getCapacity());
        return zoneRepository.save(zone);
    }
    public void deleteZone(Long id) {
        Zone zone = getZoneById(id);
        boolean hasCreatures = creatureRepository.findAll()
                .stream()
                .anyMatch(c ->
                        c.getZone() != null &&
                                c.getZone().getId().equals(id));
        if (hasCreatures) {
            throw new IllegalStateException(
                    "Cannot delete zone with assigned creatures");
        }
        zoneRepository.delete(zone);
    }
}