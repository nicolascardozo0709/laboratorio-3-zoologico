package com.javeriana.zoo_fantastico.controller;
import com.javeriana.zoo_fantastico.dto.ZoneResponse;
import com.javeriana.zoo_fantastico.entity.Zone;
import com.javeriana.zoo_fantastico.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    private final ZoneService zoneService;
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }
    @PostMapping
    public ResponseEntity<Zone> createZone(
            @RequestBody Zone zone) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(zoneService.createZone(zone));
    }
    @GetMapping
    public List<ZoneResponse> getAllZones() {
        return zoneService.getAllZonesWithCount();
    }
    @GetMapping("/{id}")
    public Zone getZoneById(
            @PathVariable Long id) {
        return zoneService.getZoneById(id);
    }
    @PutMapping("/{id}")
    public Zone updateZone(
            @PathVariable Long id,
            @RequestBody Zone zone) {
        return zoneService.updateZone(id, zone);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(
            @PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }
}