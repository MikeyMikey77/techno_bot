package khpi.cs.se.db.semesterwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPagesController {

    @GetMapping("/help")
    public String helpPage(){
        return "help";
    }

    @GetMapping("/about")
    public String aboutPage(){
        return "about";
    }

}
