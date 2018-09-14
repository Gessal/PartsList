package com.gessal.PartsList.controller;

import com.gessal.PartsList.entity.Part;
import com.gessal.PartsList.repository.PartRepository;
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
    private final PartRepository partRepository;

    @Autowired
    public MainController(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    /* Вставка записи (детали) в базу.
     * Пока не понял как можно обработку двух форм запихать в один @PostMapping. */
    @PostMapping(params = {"addName", "addCount"})
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
        Pageable page =  PageRequest.of(0, 10);
        Page<Part> partsPage = partRepository.findAll(page);
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        Integer n = partRepository.selectMinCount();
        model.addAttribute("n", n);
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }

    @GetMapping(path="/edit")
    public String editPartForm (@RequestParam String id, Model model) {
        Optional<Part> op = partRepository.findById(Integer.parseInt(id));
        if (op.isPresent()) {
            Part part = op.get();
            model.addAttribute("id", id);
            model.addAttribute("name", part.getName());
            model.addAttribute("need", part.getNeed());
            model.addAttribute("count", part.getCount());
        }
        return "editPage";
    }

    /* Редактирование записи (детали) в базе.
     * Пока не понял как можно обработку двух форм запихать в один @PostMapping. */
    @PostMapping(params = "id")
    public String editPart (@RequestParam(name = "id") Integer id,
                            @RequestParam(name = "name", required = false) String name,
                            @RequestParam(name ="need", required = false) Boolean need,
                            @RequestParam(name = "count", required = false) Integer count,
                            Model model) {
        if (!name.isEmpty() && count != null) {
            Optional<Part> op = partRepository.findById(id);
            if (op.isPresent()) {
                Part part = op.get();
                part.setName(name);
                if (need == null) {
                    part.setNeed(false);
                } else {
                    part.setNeed(true);
                }
                part.setCount(count);
                partRepository.save(part);
            }
        }
        Pageable page = PageRequest.of(0, 10);
        Page<Part> partsPage = partRepository.findAll(page);
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        Integer n = partRepository.selectMinCount();
        model.addAttribute("n", n);
        model.addAttribute("pages", pages);
        model.addAttribute("parts", partsPage);
        return "mainPage";
    }

    /* Обработка всех GET запросов на странице "Parts-List".
     * Можно конечно и по отдельности, но мне показалось, что так будет лучше. */
    @GetMapping
    public String getAllParts(@RequestParam(required = false, defaultValue = "0") Integer id,
                              @RequestParam(name = "page", required = false, defaultValue = "1") String pageN,
                              @RequestParam(name = "find", required = false, defaultValue = "") String findName,
                              @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                              @RequestParam(name = "need", required = false, defaultValue = "all") String need,
                              Model model) {
        boolean selectedAll = true;
        boolean selectedY = false;
        boolean selectedN = false;
        Pageable page;
        switch (sort) {
            case "nameUp":
                page = PageRequest.of(Integer.parseInt(pageN) - 1, 10, Sort.Direction.ASC, "name");
                break;
            case "nameDown":
                page = PageRequest.of(Integer.parseInt(pageN) - 1, 10, Sort.Direction.DESC, "name");
                break;
            case "countUp":
                page = PageRequest.of(Integer.parseInt(pageN) - 1, 10, Sort.Direction.ASC, "count");
                break;
            case "countDown":
                page = PageRequest.of(Integer.parseInt(pageN) - 1, 10, Sort.Direction.DESC, "count");
                break;
            default:
                page = PageRequest.of(Integer.parseInt(pageN) - 1, 10);
                break;
        }
        Page<Part> partsPage;
        if (id != 0) {
            Optional<Part> part = partRepository.findById(id);
            part.ifPresent(partRepository::delete);
        }
        if (findName.equals("")) {
            switch (need) {
                case "all":
                    partsPage = partRepository.findAll(page);
                    break;
                case "y":
                    selectedY = true;
                    selectedAll = false;
                    partsPage = partRepository.findByNeed(true, page);
                    break;
                default:
                    selectedN = true;
                    selectedAll = false;
                    partsPage = partRepository.findByNeed(false, page);
                    break;
            }
        } else {
            switch (need) {
                case "all":
                    partsPage = partRepository.findByName(findName, page);
                    break;
                case "y":
                    selectedY = true;
                    selectedAll = false;
                    partsPage = partRepository.findByNameAndNeed(findName, true, page);
                    break;
                default:
                    selectedN = true;
                    selectedAll = false;
                    partsPage = partRepository.findByNameAndNeed(findName, false, page);
                    break;
            }
        }
        int[] pages = new int[partsPage.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = i+1;
        }
        Integer n = partRepository.selectMinCount();
        model.addAttribute("sort", sort);
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
