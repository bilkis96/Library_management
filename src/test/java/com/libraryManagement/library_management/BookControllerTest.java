/*package com.libraryManagement.library_management;

public class BookControllerTest {

}*/
/*package com.libraryManagement.library_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.service.BookService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Autowired
    private BookService bookService;

    private final Book book = new Book(2, "mystery", "Author", "123456789", "2023-01-01", 5);

    @Nested
    class CreateBookTests {

        @Test
        public void createBook_Success() throws Exception {
            given(bookService.createBook(book)).willReturn(book);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$.title", is(book.getTitle())))
                    .andExpect(jsonPath("$.author", is(book.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(book.getPublishedDate())))
                    .andExpect(jsonPath("$.availableCopies", is(book.getAvailableCopies())));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        public void createBook_InvalidTitle(String title) throws Exception {
            Book invalidBook = new Book(null, title, "Test Author", "123456789", "2023-01-01", 6);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidBook)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title", is("must not be blank")));
        }
    }

    @Nested
    class GetBookByIdTests {

        @Test
        public void getBookById_Success() throws Exception {
            given(bookService.getBookById(anyLong())).willReturn(book);

            mockMvc.perform(get("/api/books/{id}", 2)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$.title", is(book.getTitle())))
                    .andExpect(jsonPath("$.author", is(book.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(book.getPublishedDate())))
                    .andExpect(jsonPath("$.availableCopies", is(book.getAvailableCopies())));
        }

        @Test
        public void getBookById_NotFound() throws Exception {
            given(bookService.getBookById(anyLong())).willThrow(new BookNotAvailableException("Book not found with id: 1"));

            mockMvc.perform(get("/api/books/{id}", 2)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book not found with id: 2")));
        }
    }

    @Nested
    class GetAllBooksTests {

        @Test
        public void getAllBooks_Success() throws Exception {
            given(bookService.getAllBooks()).willReturn(Arrays.asList(book));

            mockMvc.perform(get("/api/books")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$[0].title", is(book.getTitle())))
                    .andExpect(jsonPath("$[0].author", is(book.getAuthor())))
                    .andExpect(jsonPath("$[0].isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$[0].publishedDate", is(book.getPublishedDate())))
                    .andExpect(jsonPath("$[0].availableCopies", is(book.getAvailableCopies())));
        }
    }

    @Nested
    class UpdateBookTests {

        @Test
        public void updateBook_Success() throws Exception {
            Book updatedBook = new Book(3, "Updated Title", "Updated Author", "987654321", "2024-01-01", 10);
           // given(bookService.updateBook(anyLong(), any(Book.class())).willReturn(updatedBook));

            mockMvc.perform(put("/api/books/{id}", 3)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedBook)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(updatedBook.getId().intValue())))
                    .andExpect(jsonPath("$.title", is(updatedBook.getTitle())))
                    .andExpect(jsonPath("$.author", is(updatedBook.getAuthor())))
                    .andExpect(jsonPath("$.isbn", is(updatedBook.getIsbn())))
                    .andExpect(jsonPath("$.publishedDate", is(updatedBook.getPublishedDate())))
                    .andExpect(jsonPath("$.availableCopies", is(updatedBook.getAvailableCopies())));
        }

        @Test
        public void updateBook_NotFound() throws Exception {
            //given(bookService.updateBook(anyLong(), any(Book.class))).willThrow(new BookNotAvailableException("Book not found with id: 1"));

            mockMvc.perform(put("/api/books/{id}", 7)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book not found with id: 7")));
        }
    }

    @Nested
    class DeleteBookTests {

        @Test
        public void deleteBook_Success() throws Exception {
            doNothing().when(bookService).deleteBook(anyLong());

            mockMvc.perform(delete("/api/books/{id}", 3)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteBook_NotFound() throws Exception {
            doThrow(new BookNotAvailableException("Book not found with id: 1")).when(bookService).deleteBook(anyLong());

            mockMvc.perform(delete("/api/books/{id}", 8)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book not found with id: 8")));
        }
    }

    @Nested
    class SearchBooksTests {

        @Test
        public void searchBooks_Success() throws Exception {
           // given(bookService.searchBooks(anyString(), anyString(), anyString())).willReturn(Arrays.asList(book));

            mockMvc.perform(get("/api/books/search")
                            .param("title", "mystery")
                            .param("author", "Author")
                            .param("isbn", "123456789")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(book.getId().intValue())))
                    .andExpect(jsonPath("$[0].title", is(book.getTitle())))
                    .andExpect(jsonPath("$[0].author", is(book.getAuthor())))
                    .andExpect(jsonPath("$[0].isbn", is(book.getIsbn())))
                    .andExpect(jsonPath("$[0].publishedDate", is(book.getPublishedDate())))
                    .andExpect(jsonPath("$[0].availableCopies", is(book.getAvailableCopies())));
        }
    }
}*/

