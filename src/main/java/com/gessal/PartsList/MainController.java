package com.gessal.PartsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/Parts-List")
public class MainController {
    @Autowired
    private PartRepository userRepository;

    @GetMapping(path="/add")
    public @ResponseBody String addNewPart (@RequestParam String name, @RequestParam String is_need, @RequestParam String count) {
        Part part = new Part();
        part.setName(name);
        if (is_need.equals("true")) {
            part.setIs_need(true);
        } else {
            part.setIs_need(false);
        }
        part.setCount(Integer.parseInt(count));
        userRepository.save(part);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Part> getAllParts() {
        return userRepository.findAll();
    }
}
