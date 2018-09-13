package com.gessal.PartsList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/Parts-List")
public class MainController {
    @Autowired
    private PartRepository partRepository;

    @PostMapping("add")
    public String addNewPart (@RequestParam(name = "addName", required = false) String addName,
                              @RequestParam(name = "addNeed", required = false) Boolean addNeed,
                              @RequestParam(name = "addCount", required = false) Integer addCount, Model model) {
        if (!addName.isEmpty() && addCount != null) {
            Part part = new Part();
            part.setName(addName);
            if (addNeed == null) {
                part.setNeed(false);
            } else {
                part.setNeed(true);
            }
            part.setCount(addCount);
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

    @GetMapping(path="/edit")
    public String editPartForm (@RequestParam String id, Model model) {
        Part part = partRepository.findById(Integer.parseInt(id)).get();
        model.addAttribute("id", id);
        model.addAttribute("name", part.getName());
        model.addAttribute("need", part.getNeed());
        model.addAttribute("count", part.getCount());
        return "editPage";
    }

    @PostMapping(path = "/all")
    public String editPart (@RequestParam Integer id, @RequestParam(required = false) String name, @RequestParam(required = false) Boolean need, @RequestParam(required = false) Integer count, Model model) {
        if (!name.isEmpty() && count != null) {
            Part part = partRepository.findById(id).get();
            part.setName(name);
            if (need == null) {
                part.setNeed(false);
            } else {
                part.setNeed(true);
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
    @GetMapping(path="{all}") //TODO костыль для того, чтобы поиск работал после вставки элемента
    public String getAllParts(@RequestParam(required = false, defaultValue = "0") Integer id,
                              @RequestParam(name = "page", required = false, defaultValue = "1") String pageN,
                              @RequestParam(name = "find", required = false, defaultValue = "") String findName,
                              @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                              @RequestParam(name = "need", required = false, defaultValue = "all") String need,
                              Model model) {
        Boolean selectedAll = true;
        Boolean selectedY = false;
        Boolean selectedN = false;
        Pageable page;
        if (sort.equals("nameUp")) {
            page = new PageRequest(Integer.parseInt(pageN)-1, 10, Sort.Direction.ASC, "name");
        } else if (sort.equals("nameDown")) {
            page = new PageRequest(Integer.parseInt(pageN)-1, 10, Sort.Direction.DESC, "name");
        } else if (sort.equals("countUp")) {
            page = new PageRequest(Integer.parseInt(pageN)-1, 10, Sort.Direction.ASC, "count");
        } else if (sort.equals("countDown")) {
            page = new PageRequest(Integer.parseInt(pageN)-1, 10, Sort.Direction.DESC, "count");
        } else {
            page = new PageRequest(Integer.parseInt(pageN) - 1, 10);
        }
        Page<Part> partsPage;
        if (id != 0) {
            Optional<Part> part = partRepository.findById(id);
            partRepository.delete(part.get());
        }
        if (findName.equals("")) {
            if (need.equals("all")) {
                partsPage = partRepository.findAll(page);
            } else if (need.equals("y")) {
                selectedY = true;
                selectedAll = false;
                partsPage = partRepository.findByNeed(true, page);
            } else {
                selectedN = true;
                selectedAll = false;
                partsPage = partRepository.findByNeed(false, page);
            }
        } else {
            if (need.equals("all")) {
                partsPage = partRepository.findByName(findName, page);
            } else if (need.equals("y")) {
                selectedY = true;
                selectedAll = false;
                partsPage = partRepository.findByNameAndNeed(findName, true, page);
            } else {
                selectedN = true;
                selectedAll = false;
                partsPage = partRepository.findByNameAndNeed(findName, false, page);
            }
        }
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        Integer n = 0; ////TODO!!!!!!


        model.addAttribute("n", n);
        model.addAttribute("selectedAll", selectedAll);
        model.addAttribute("selectedY", selectedY);
        model.addAttribute("selectedN", selectedN);
        model.addAttribute("find", findName);
        model.addAttribute("need", need);
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }
}
