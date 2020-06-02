package com.bailbots.tg.QuartersBot.dao;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity(name = "house_picture")
public class HouseImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;
}
