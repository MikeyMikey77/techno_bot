package khpi.cs.se.db.semesterwork.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import khpi.cs.se.db.semesterwork.model.Order;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductType;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotKeyboards {

    private ProductTypeService productTypeService;

    @Autowired
    public BotKeyboards(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    public ReplyKeyboardMarkup infoKeyboard(User u) {
        String[][] buttons = new String[6][];
        if (u.getUserRole().getTitle().equals("ROLE_ADMIN")) {
            buttons[0] = new String[]{"Name", "Surname"};
            buttons[1] = new String[]{"Username", "Password"};
            buttons[2] = new String[]{"Date of birth", "Gender"};
            buttons[3] = new String[]{"Country", "City"};
            buttons[4] = new String[]{"Street", "House"};
        } else {
            buttons[0] = new String[]{"Name", "Surname"};
            buttons[1] = new String[]{"Date of birth", "Gender"};
            buttons[2] = new String[]{"Country", "City"};
            buttons[3] = new String[]{"Street", "House"};
            buttons[4] = new String[]{""};
        }
        buttons[5] = new String[]{"Back to main menu..."};
        return new ReplyKeyboardMarkup(buttons,
                true,
                false,
                true);
    }

    public ReplyKeyboardMarkup categoryKeyboard() {
        List<ProductType> types =
                (List<ProductType>) productTypeService.getByIdProductType();
        int rows;
        if (types.size() % 2 == 0) {
            rows = types.size() / 2;
        } else {
            rows = types.size() / 2 + 1;
        }
        String[][] buttons = new String[rows + 1][2];
        for (int i = 0; i < rows + 1; i++) {
            buttons[i][0] = "";
            buttons[i][1] = "";
        }
        int row = 0, column = 0;
        for (ProductType type : types) {
            buttons[row][column++] = type.getTitle();
            if (column == 2) {
                row++;
                column = 0;
            }
        }
        buttons[rows][0] = "Back to main menu...";
        return new ReplyKeyboardMarkup(buttons,
                true, false, true);
    }

    public ReplyKeyboardMarkup mainKeyboard() {
        return new ReplyKeyboardMarkup(new String[][]
                {{"Categories", "Basket"},
                        {"Orders", "About market"},
                        {"My info", "Help"}},
                true, false, true);
    }

    public ReplyKeyboardMarkup basketKeyboard(User u) {
        return new ReplyKeyboardMarkup(new String[][]{
                {"Confirm order"}, {"Back to main menu..."}
        },
                true,
                false,
                true);
    }

    public ReplyKeyboardMarkup backToMainMenuKeyboard() {
        return new ReplyKeyboardMarkup(new String[][]{{"Back to main menu..."}},
                true,
                false,
                true);
    }

    public InlineKeyboardMarkup forCategoryProductList(Product prod, String addOrAdded) {
        if (addOrAdded.equals("add")) {
            return new InlineKeyboardMarkup(new InlineKeyboardButton[][]
                    {
                            {new InlineKeyboardButton("Add to basket").callbackData(prod.getIdProduct() + "|add_to_basket")}
                    });
        } else {
            return new InlineKeyboardMarkup(new InlineKeyboardButton[][]
                    {
                            {new InlineKeyboardButton("Added to basket").callbackData(prod.getIdProduct() + "|added")}
                    });
        }
    }

    public InlineKeyboardMarkup forBasketProductList(Product prod, Integer count) {
        return new InlineKeyboardMarkup(new InlineKeyboardButton[][]
                {
                        {new InlineKeyboardButton("<-").callbackData("|<-|" + prod.getIdProduct()),
                                new InlineKeyboardButton(count.toString()).callbackData("|count"),
                                new InlineKeyboardButton("->").callbackData("|->|" + prod.getIdProduct())},
                        {new InlineKeyboardButton("Delete from basket").callbackData(prod.getIdProduct() + "|delete_from_basket")}
                });
    }

    public InlineKeyboardMarkup forOrderList(Order order) {
        return new InlineKeyboardMarkup(new InlineKeyboardButton[]{
                new InlineKeyboardButton("Delete order").callbackData(order.getIdOrder()+"|delete_order")
        });
    }
}
