package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Double.*;


@Controller
@Slf4j
public class PageController {

    Filters prev_params;
    Games[] games;
    Games randomGame;


    @RequestMapping("/random")
    public String random(Model model) {
        try {
            gamelist(new Filters(), true);                         //search games in sql
        } catch (Exception e) {
            model.addAttribute("error", "Seems like we are updating our DB, sorry, try again later");
            log.error("SQL is not working!");
            return "error";
        }
        int a = randomGame.getReviews();
        int b = randomGame.getPositive();
        DecimalFormat df = new DecimalFormat("#.##");
        model.addAttribute("game", randomGame);               //push attribute
        model.addAttribute("revs", new int[]{a / 100 * b, a / 100 * (100 - b)});
        model.addAttribute("oldcost", df.format(randomGame.getPrice()/(100-randomGame.getSale())*100));
        return "random";
    }


    @RequestMapping("/search")
    public String search(@RequestParam Map<String, String> queryParameters, Model model) {
        Filters params = paramsAsNeeded(queryParameters);                  //param initializing
        if (! params.equals(prev_params)) {
            prev_params = params;
            try {
                gamelist(params, false);                           //search games in sql
            } catch (Exception e) {
                model.addAttribute("error", "Seems like we are updating our DB, sorry, try again later");
                log.error("SQL is not working!");
                return "error";
            }
        }
        int page_length = 25;
        int max_pages = games.length / page_length;
        if (params.getPage() < 0 || params.getPage() > max_pages) {        //number of page to be returned
            params.setPage(0);
        }
        int index = params.getPage() * page_length;
        Games[] page = new Games[Integer.min(games.length - index, page_length)];
        System.arraycopy(games, index, page, 0, Integer.min(games.length - index, page_length));
        for (int i = 0; i < Integer.min(games.length - index, page_length); i++) {
            page[i].setElementId(i);
        }
        log.info("Search by params:" + params);
        model.addAttribute("games", page);                     //push attributes
        model.addAttribute("page", params.getPage() + 1);
        model.addAttribute("maxPage", max_pages + 1);
        model.addAttribute("gamesAmount", games.length);
        return "tables";                                                   //return search-page
    }

    //all games by filters(max 1000)   search/random
    private void gamelist(Filters filter, Boolean random) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "Client", "");
        String question = ("FROM steam WHERE Title LIKE '%%%s%%' and Developer LIKE '%%%s%%' and Publisher LIKE '%%%s%%'" +
                " and Price between %d and %d and Positive between %d and 100 order by Reviews DESC LIMIT 1000;")
                .formatted(filter.getByTitle(), filter.getByDeveloper(), filter.getByPublisher(),
                        (int) filter.getByPriceMin() * 100, (int) filter.getByPriceMax() * 100, filter.getByPositive());
        int columns_amount = 13;
        String[] Row = new String[columns_amount];
        ResultSet rs;
        ResultSet amount;
        if (! random) {
            rs = conn.createStatement().executeQuery("SELECT * " + question);
            amount = conn.createStatement().executeQuery("SELECT COUNT(*) " + question);
        } else {
            Random rand = new Random();
            amount = conn.createStatement().executeQuery("SELECT COUNT(*) FROM steam");
            amount.next();
            int num = rand.nextInt(Math.toIntExact(amount.getInt(1))) + 1;
            rs = conn.createStatement().executeQuery("SELECT * FROM steam WHERE Indeks LIKE " + num);
            rs.next();
            for (int i = 1; i <= columns_amount; i++) {
                Row[i - 1] = rs.getString(i);
            }
            randomGame = arrayToGames(Row);
            log.info("Successfully returned random game");
            return;
        }
        amount.next();
        games = new Games[Math.min(amount.getInt(1), 1000)];
        int n = 0;
        while (rs.next()) {
            for (int i = 1; i <= columns_amount; i++) {
                Row[i - 1] = rs.getString(i);
            }
            games[n++] = arrayToGames(Row);
        }
        log.info("Successfully returned games");
    }

    //set object Games from ResultSet(string[])
    private Games arrayToGames(String[] line) {
        Games game = new Games();
        int n = 1;
        game.setTitle(line[n++]);
        game.setIMG_small(line[n++]);
        game.setIMG_big(line[n++]);
        game.setDeveloper(line[n++]);
        game.setPublisher(line[n++]);
        game.setLink(line[n++]);
        game.setPrice(parseDouble(line[n++]) / 100d);
        game.setReviews(Integer.parseInt(line[n++]));
        game.setPositive(Integer.parseInt(line[n++]));
        game.setSale(Integer.parseInt(line[n++]));
        game.setDate(line[n++]);
        game.setPlatform("images/" + line[n] + "_logo.png");
        return game;
    }

    //a little fix for all filters + handling some errors user can make
    private Filters paramsAsNeeded(Map<String, String> params) {
        Filters new_params = new Filters();

        if (params.get("Title") == null) {
            new_params.setByTitle("");
        } else {
            new_params.setByTitle(params.get("Title"));
        }

        if (params.get("Developer") == null) {
            new_params.setByDeveloper("");
        } else {
            new_params.setByDeveloper(params.get("Developer"));
        }

        if (params.get("Publisher") == null) {
            new_params.setByPublisher("");
        } else {
            new_params.setByPublisher(params.get("Publisher"));
        }

        try {
            if (! Objects.equals(params.get("PriceMin"), "") & ! Objects.equals(params.get("PriceMax"), "")
                    & params.get("PriceMin") != null & params.get("PriceMax") != null) {
                if (parseDouble(params.get("PriceMax")) >= parseDouble(params.get("PriceMin"))) {
                    new_params.setByPriceMin(parseDouble(params.get("PriceMin")));
                    new_params.setByPriceMax(parseDouble(params.get("PriceMax")));
                } else {
                    log.warn("filter PriceMin > PriceMax, fixed");
                    new_params.setByPriceMin(parseDouble(params.get("PriceMax")));
                    new_params.setByPriceMax(parseDouble(params.get("PriceMin")));
                }
            } else {
                new_params.setByPriceMin(0);
                new_params.setByPriceMax(1000);
            }
        } catch (Exception e) {
            log.warn("Price: " + e);
            new_params.setByPriceMin(0);
            new_params.setByPriceMax(1000);
        }

        try {
            if (! Objects.equals(params.get("Positive"), "") || params.get("Positive") != null) {
                new_params.setByPositive(Integer.parseInt(params.get("Positive")));
            } else {
                new_params.setByPositive(0);
            }
        } catch (Exception e) {
            log.warn("Positive: " + e);
            new_params.setByPositive(0);
        }
        try {
            if (Objects.equals(params.get("Page"), "") || params.get("Page") == null) {
                new_params.setPage(0);
            } else {
                new_params.setPage(Integer.parseInt(params.get("Page")) - 1);
            }
        } catch (Exception e) {
            log.warn("Page: " + e);
            new_params.setPage(0);
        }

        return new_params;
    }
}