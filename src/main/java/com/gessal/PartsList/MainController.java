package com.gessal.PartsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Optional;

@Controller
@RequestMapping(path="/Parts-List")
public class MainController {
    @Autowired
    private PartRepository partRepository;

    /******************Добавление записи********************/
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
        partRepository.save(part);
        return "Added";
    }

    /******************Удаление записи********************/
    @GetMapping(path="/del")
    public @ResponseBody String delPart (@RequestParam String id) {
        Optional<Part> part = partRepository.findById(Integer.parseInt(id));
        partRepository.delete(part.get());
        return "Deleted";
    }

    /******************Редактирование записи********************/
    @GetMapping(path="/edit")
    public @ResponseBody String editPart (@RequestParam String id, @RequestParam String name, @RequestParam String is_need, @RequestParam String count) {
        Part part = partRepository.findById(Integer.parseInt(id)).get();
        part.setName(name);
        if (is_need.equals("true")) {
            part.setIs_need(true);
        } else {
            part.setIs_need(false);
        }
        part.setCount(Integer.parseInt(count));
        partRepository.save(part);
        return "Changed";
    }

    /******************Поиск записи********************/
    @GetMapping(path="/find")
    public @ResponseBody Iterable<Part> findPart (@RequestParam String name) {
        return partRepository.findByName(name);
    }

    /******************Вывод всех записей********************/
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Part> getAllParts() {
        return partRepository.findAll();
    }
}
