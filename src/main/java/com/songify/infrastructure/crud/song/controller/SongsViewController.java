package com.songify.infrastructure.crud.song.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SongsViewController {
    Map<Integer, String> database = new HashMap<>();

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/songs")
    public String songs(Model model) {
        database.put(1, "shawn mendes song1");
        database.put(2, "ariana grande song2");
        database.put(3, "ariana grande song3");
        database.put(4, "ariana grande song4");

        model.addAttribute("songMap",database);

        return "songs";
    }

}
