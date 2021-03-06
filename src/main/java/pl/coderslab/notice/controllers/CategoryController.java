package pl.coderslab.notice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.notice.entity.Category;
import pl.coderslab.notice.entity.Notice;
import pl.coderslab.notice.repository.CategoryRepository;
import pl.coderslab.notice.repository.NoticeRepository;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    Validator validator;

    private final CategoryRepository categoryRepository;
    private final NoticeRepository noticeRepository;

    public CategoryController(CategoryRepository categoryRepository, NoticeRepository noticeRepository) {
        this.categoryRepository = categoryRepository;
        this.noticeRepository = noticeRepository;
    }

    /* Add category */

    @GetMapping("/addCategory")
    public String showFormUser(Model model) {
        model.addAttribute("category", new Category());
        return "/category/add";
    }

    @PostMapping("/addCategory")

    public String perform(@ModelAttribute @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/category/add";
        }
        categoryRepository.save(category);
        return "redirect:/category/showCategories";

    }

    /* show All Categories */

    @GetMapping("/showCategories")
    public String showAll(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/showCategories";
    }

    /* show List of categories */

    @ModelAttribute("categories")
    public Collection<Category> populateCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    /* add Category to Notice */

    @GetMapping("addCategoryToNotice/{id}")
    public String showForm(Model model, @PathVariable long id) {

        model.addAttribute("notice", noticeRepository.findById(id));
        return "notice/updateNotice";
    }

    @PostMapping("/addCategoryToNotice")
    public String performUpdate(@ModelAttribute Category category, @ModelAttribute Notice notice) {
        categoryRepository.save(category);
        noticeRepository.save(notice);
        return "redirect:/welcomeAd";

    }

}
