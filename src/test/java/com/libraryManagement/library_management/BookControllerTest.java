/*package com.libraryManagement.library_management;

public class BookControllerTest {

}*/
package com.libraryManagement.library_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BookService bookService;

    @BeforeEach
    public void setUp() {
    }
    private final Book book = new Book(1L, "Title1", "Author1", "123456789", LocalDate.parse("2023-01-01"), 5);
    private final Book book1 = new Book(2L, "Title2", "Author2", "1234567890", LocalDate.parse("2023-01-02"), 4);


    @Nested
    class CreateBookTests {

        @Test
        public void createBook_Success() throws Exception {
            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is(book.getTitle())))
                    .andExpect(jsonPath("$.author", is(book.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(book.getPublishedDate().toString())))
                    .andExpect(jsonPath("$.availableCopies", is(book.getAvailableCopies())));
        }
    }


        @Test
        public void createBook_Success() throws Exception {
            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is(book.getTitle())))
                    .andExpect(jsonPath("$.author", is(book.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(book.getPublishedDate().toString())))
                    .andExpect(jsonPath("$.availableCopies", is(book.getAvailableCopies())));
        }


    @Nested
    class GetBookByIdTests {

        @Test
        public void getBookById_Success() throws Exception {
            // update the id according to data in database
            mockMvc.perform(get("/api/books/{id}", 1)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$.title", is(book.getTitle())))
                    .andExpect(jsonPath("$.author", is(book.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(book.getPublishedDate().toString())))
                    .andExpect(jsonPath("$.availableCopies", is(book.getAvailableCopies())));
        }

        @Test
        public void getBookById_NotFound() throws Exception {
            mockMvc.perform(get("/api/books/{id}", 3)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetAllBooksTests {

        @Test
        public void getAllBooks_Success() throws Exception {
            mockMvc.perform(get("/api/books")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$[0].title", is(book.getTitle())))
                    .andExpect(jsonPath("$[0].author", is(book.getAuthor())))
                    .andExpect(jsonPath("$[0].isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$[0].publishedDate", is(book.getPublishedDate().toString())))
                    .andExpect(jsonPath("$[0].availableCopies", is(book.getAvailableCopies())));
        }
    }

    @Nested
    class UpdateBookTests {

        @Test
        public void updateBook_Success() throws Exception {
            Book updatedBook = new Book(1L, "Updated Title", "Updated Author", "987654321", LocalDate.parse("2023-01-01"), 10);
            mockMvc.perform(put("/api/books/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedBook)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(updatedBook.getId().intValue())))
                    .andExpect(jsonPath("$.title", is(updatedBook.getTitle())))
                    .andExpect(jsonPath("$.author", is(updatedBook.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(updatedBook.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(updatedBook.getPublishedDate().toString())))
                    .andExpect(jsonPath("$.availableCopies", is(updatedBook.getAvailableCopies())));
        }

        @Test
        public void updateBook_NotFound() throws Exception {
            mockMvc.perform(put("/api/books/{id}", 7)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteBookTests {

        @Test
        public void deleteBook_Success() throws Exception {
            doNothing().when(bookService).deleteBook(anyLong());
            mockMvc.perform(delete("/api/books/{id}", 1)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteBook_NotFound() throws Exception {
            doThrow(new BookNotAvailableException("book not found with id: 1")).when(bookService).deleteBook(anyLong());

            mockMvc.perform(delete("/api/books/{id}", 1)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class SearchBooksTests {

        @Test
        public void searchBooks_Success() throws Exception {
            mockMvc.perform(get("/api/books/search")
                            .param("title", "Title1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(book1.getId().intValue())))
                    .andExpect(jsonPath("$[0].title", is(book1.getTitle())))
                    .andExpect(jsonPath("$[0].author", is(book1.getAuthor())))
                    .andExpect(jsonPath("$[0].isbn", is(book1.getIsbn())))
                    .andExpect(jsonPath("$[0].publishedDate", is(book1.getPublishedDate())))
                    .andExpect(jsonPath("$[0].availableCopies", is(book1.getAvailableCopies())));
        }
    }
}

