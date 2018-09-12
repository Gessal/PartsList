package com.gessal.PartsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path="/Parts-List")
public class MainController {
    @Autowired
    private PartRepository partRepository;

    /******************Добавление записи********************/
    @PostMapping("add")
    public String addNewPart (@RequestParam(required = false) String name, @RequestParam(required = false) Boolean is_need, @RequestParam(required = false) Integer count, Model model) {
        if (!name.isEmpty() && count != null) {
            Part part = new Part();
            part.setName(name);
            if (is_need == null) {
                part.setIs_need(false);
            } else {
                part.setIs_need(true);
            }
            part.setCount(count);
            partRepository.save(part);
        }
        Pageable page = new PageRequest(0, 10);
        Page<Part> partsPage = partRepository.findAll(page);
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }

    /******************Удаление записи********************/
    @GetMapping(path="/del")
    public String delPart (@RequestParam String id, Model model) {
        Optional<Part> part = partRepository.findById(Integer.parseInt(id));
        partRepository.delete(part.get());
        Pageable page = new PageRequest(0, 10);
        Page<Part> partsPage = partRepository.findAll(page);
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        
        return "mainPage";
    }

    /******************Форма редактирования записи********************/
    @GetMapping(path="/edit")
    public String editPartForm (@RequestParam String id, Model model) {
        Part part = partRepository.findById(Integer.parseInt(id)).get();
        model.addAttribute("id", id);
        model.addAttribute("name", part.getName());
        model.addAttribute("is_need", part.getIs_need());
        model.addAttribute("count", part.getCount());
        return "editPage";
    }

    /******************Редактирование записи********************/
    @PostMapping(path = "/edit")
    public String editPart (@RequestParam Integer id, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean is_need, @RequestParam(required = false) Integer count, Model model) {
        if (!name.isEmpty() && count != null) {
            Part part = partRepository.findById(id).get();
            part.setName(name);
            if (is_need == null) {
                part.setIs_need(false);
            } else {
                part.setIs_need(true);
            }
            part.setCount(count);
            partRepository.save(part);
        }
        Pageable page = new PageRequest(0, 10);
        Page<Part> partsPage = partRepository.findAll(page);
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
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
        model.addAttribute("find", findName);
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }
}
