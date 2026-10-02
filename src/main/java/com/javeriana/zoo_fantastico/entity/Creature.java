package com.javeriana.zoo_fantastico.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.javeriana.zoo_fantastico.entity.Zone;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Creature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String species;
    @Min(0)
    private double size;
    @Min(1)
    @Max(10)
    private int dangerLevel;
    private String healthStatus;
    @ManyToOne
    @JoinColumn(name = "zone.id")
    private Zone zone;
}
