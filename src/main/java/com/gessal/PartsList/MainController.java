package com.gessal.PartsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public @ResponseBody String editPart (@RequestParam String id, Model model) {
        Part part = partRepository.findById(Integer.parseInt(id)).get();
        model.addAttribute("name", part.getName());
        model.addAttribute("is_need", part.getIs_need());
        model.addAttribute("count", part.getCount());
        return "editPage";
    }

    /******************Вывод всех записей********************/
    @GetMapping(path="/all")
    public String getAllParts(@RequestParam(name="page", required=false, defaultValue="1") String pageN, @RequestParam(name="find", required=false, defaultValue="") String findName, Model model) {
        Pageable page = new PageRequest(Integer.parseInt(pageN)-1, 10);
        Page<Part> partsPage;
        if (findName.equals("")) {
            partsPage = partRepository.findAll(page);
        } else {
            partsPage = partRepository.findByName(findName, page);
        }
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        //--------------------
        model.addAttribute("find", findName);
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }
}
