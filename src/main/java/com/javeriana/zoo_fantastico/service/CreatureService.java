package com.javeriana.zoo_fantastico.service;
import com.javeriana.zoo_fantastico.entity.Creature;
import com.javeriana.zoo_fantastico.entity.Zone;
import com.javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import com.javeriana.zoo_fantastico.repository.CreatureRepository;
import com.javeriana.zoo_fantastico.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;
    private final ZoneRepository zoneRepository;
    @Autowired
    public CreatureService(
            CreatureRepository creatureRepository,
            ZoneRepository zoneRepository) {
        this.creatureRepository = creatureRepository;
        this.zoneRepository = zoneRepository;
    }
    public Creature createCreature(Creature creature) {
        if (creature.getZone() != null) {
            Zone zone = zoneRepository.findById(
                            creature.getZone().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Zone not found"));
            long creaturesInZone = creatureRepository.findAll()
                    .stream()
                    .filter(c -> c.getZone() != null)
                    .filter(c -> c.getZone().getId().equals(zone.getId()))
                    .count();
            if (creaturesInZone >= zone.getCapacity()) {
                throw new IllegalStateException(
                        "Zone is at maximum capacity");
            }
            creature.setZone(zone);
        }
        return creatureRepository.save(creature);
    }
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }
    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Creature not found"));
    }
    public Creature updateCreature(
            Long id,
            Creature updatedCreature) {
        Creature creature = getCreatureById(id);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        if (updatedCreature.getZone() != null) {
            Zone zone = zoneRepository.findById(
                            updatedCreature.getZone().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Zone not found"));
            long creaturesInZone = creatureRepository.findAll()
                    .stream()
                    .filter(c -> c.getZone() != null)
                    .filter(c -> c.getZone().getId().equals(zone.getId()))
                    .filter(c -> !c.getId().equals(creature.getId()))
                    .count();
            if (creaturesInZone >= zone.getCapacity()) {
                throw new IllegalStateException(
                        "Zone is at maximum capacity");
            }
            creature.setZone(zone);
        }
        return creatureRepository.save(creature);
    }
    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equalsIgnoreCase(
                creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException(
                    "Cannot delete a creature in critical health");
        }
    }
}