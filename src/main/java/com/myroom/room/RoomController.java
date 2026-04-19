package com.myroom.room;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.myroom.common.NotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;

    @GetMapping
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @GetMapping("/{id}")
    public Room findOne(@PathVariable Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("room.notFound", id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @RequestBody Room room) {
        Room existing = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("room.notFound", id));
        existing.setName(room.getName());
        existing.setDescription(room.getDescription());
        existing.setCapacity(room.getCapacity());
        return roomRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("room.notFound", id);
        }
        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
