package org.vvchebotar.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vvchebotar.crud.domain.Book;
import org.vvchebotar.crud.service.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping("/page")
    public String page(@RequestParam String page, Model model) {
        List<Book> books = bookService.getBooksByPage(page);
        model.addAttribute("books", books);

        List<String> pages = new ArrayList();
        pages.add("1");
        int numberOfBooks = bookService.getAllBooksCount();
        //int numberOfBooks = 35;
        int numberOfPages = numberOfBooks / 10 + 1;
        if (numberOfBooks > 1) {
            for (int i = 2; i <= numberOfPages; i++) {
                pages.add(String.valueOf(i));
            }
        }
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        return "books";
    }

    @RequestMapping("/book")
    public String list(@RequestParam String id, @RequestParam String currentPage, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("currentPage", currentPage);
        return "book";
    }

    @RequestMapping("/remove")
    public String remove(@RequestParam String id, @RequestParam String currentPage) {
        bookService.deleteBookById(id);
        return "forward:/books/page?page=" + currentPage;
    }

    @RequestMapping("/mark")
    public String marc(@RequestParam String id, @RequestParam String currentPage) {
        bookService.markBookById(id);
        return "forward:/books/page?page=" + currentPage;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAddNewBookForm(@RequestParam String currentPage, Model model) {
        Book newBook = new Book();
        model.addAttribute("newBook", newBook);
        model.addAttribute("currentPage", currentPage);
        return "addBook";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddNewBookForm(@ModelAttribute("newBook") Book newBook, @RequestParam String currentPage) {
        newBook.setReadAlready(false);
        bookService.create(newBook);
        return "redirect:/books/page?page=" + currentPage;
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEditBookForm(@RequestParam String id, @RequestParam String currentPage, Model model) {
        Book editBook = bookService.getBookById(id);
        model.addAttribute("editBook", editBook);
        model.addAttribute("currentPage", currentPage);
        return "editBook";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String processEditBookForm(@ModelAttribute("editBook") Book editBook, @RequestParam String id, @RequestParam String author, @RequestParam String currentPage) {
        editBook.setReadAlready(false);
        editBook.setId(Long.parseLong(id));
        editBook.setAuthor(author);
        bookService.update(editBook);
        return "redirect:/books/page?page=" + currentPage;
    }

    @RequestMapping("/filter")
    public String getProductsByFilter(@RequestParam String searchFromYear, @RequestParam String searchToYear, Model model) {
        if (searchFromYear == null || searchFromYear.isEmpty()) {
            searchFromYear = "-9999";
        }
        if (searchToYear == null || searchToYear.isEmpty()) {
            searchToYear = "9999";
        }
        List<Book> books = bookService.getBooksByYear(searchFromYear, searchToYear);
        model.addAttribute("books", books);
        model.addAttribute("pages", "1");
        model.addAttribute("currentPage", "1");
        return "books";
    }
}
