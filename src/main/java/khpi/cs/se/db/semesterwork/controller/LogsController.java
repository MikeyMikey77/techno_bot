package khpi.cs.se.db.semesterwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;


@Controller
@RequestMapping
public class LogsController {



    @RequestMapping("/")
    public String mainPage(Model model) {
        StringBuilder logs = new StringBuilder();
        try {
            File file = new File("./application.log");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            while (line != null) {
                logs.append(line+System.lineSeparator());
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("data", logs.toString());
        return "main";
    }


    @RequestMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

}
