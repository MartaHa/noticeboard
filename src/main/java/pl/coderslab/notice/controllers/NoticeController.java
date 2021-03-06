package pl.coderslab.notice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.notice.entity.Category;
import pl.coderslab.notice.entity.Notice;
import pl.coderslab.notice.entity.User;
import pl.coderslab.notice.repository.CategoryRepository;
import pl.coderslab.notice.repository.NoticeRepository;
import pl.coderslab.notice.service.CurrentUser;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notice")
public class NoticeController {


    private final NoticeRepository noticeRepository;
    private final CategoryRepository categoryRepository;

    public NoticeController(NoticeRepository noticeRepository, CategoryRepository categoryRepository) {
        this.noticeRepository = noticeRepository;
        this.categoryRepository = categoryRepository;
    }

    /* addNotice */


    @GetMapping("/addNotice")
    public String addNotice(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice/addNotice";
    }


    @PostMapping("/addNotice")

    public String performNotice(@AuthenticationPrincipal CurrentUser customUser, @ModelAttribute @Valid Notice notice, BindingResult result) {
        if (result.hasErrors()) {
            return "notice/addNotice";
        }
        User noticeOwner = customUser.getUser();
        notice.setUser(noticeOwner);
        noticeRepository.save(notice);

        return "redirect:/notice/showAll";

    }


    /* show All Users */

    @GetMapping("/showAll")
    public String showAll(Model model) {
        model.addAttribute("notices", noticeRepository.findAll());
        return "notice/showAll";
    }

    /* Show one notice */

    @GetMapping("/showNotice/{id}")

    public String showOne(Model model, @PathVariable long id) {
        model.addAttribute("notices", noticeRepository.getOne(id));
        return "notice/showNotice";

    }

    /* update Notice */

    @GetMapping("/update/{id}")
    public String showFormNotice(Model model, @PathVariable long id) {
        model.addAttribute("notice", noticeRepository.findById(id));
        return "notice/updateNotice";
    }


    @PostMapping("/update")

    public String performUpdate(@ModelAttribute @Valid Notice notice, BindingResult result) {
        if (result.hasErrors()) {
            return "notice/updateNotice";
        }
        noticeRepository.save(notice);
        return "redirect:/notice/showAll";

    }

    /* categories list */

    @ModelAttribute("categoriesList")
    public Collection<Category> populateCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    /* add Category To Notice */

    @GetMapping("addCategoryToNotice/{id}")
    public String showForm(Model model, @PathVariable long id) {

        model.addAttribute("notice", noticeRepository.findById(id));
        return "notice/addCategoryToNotice";
    }

    @PostMapping("/addCategoryToNotice")
    public String performUpdateToNotice(@ModelAttribute Notice notice) {
        noticeRepository.save(notice);
        return "redirect:/welcomeAd";

    }

    /* show Notices */

    @GetMapping("showUsersNotice/{id}")
    public String showUsersNotice(Model model, @PathVariable long id) {

        model.addAttribute("notices", noticeRepository.findAllNoticesbyUserId(id));
        return "admin/showNotices";
    }

    /* show current users adds  */

    @GetMapping("/showUserAds")
    public String showUserAds(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        User thisUser = currentUser.getUser();
        long userId = thisUser.getId();
        model.addAttribute("notices", noticeRepository.findAllNoticesbyUserId(userId));
        return "user/showUsersAds";
    }


    /*deletenotice */

    @GetMapping("/delete/{id}")

    public String delete(@PathVariable long id) {
        noticeRepository.deleteById(id);
        return "redirect:/welcome";
    }
}

