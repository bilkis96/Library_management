/*package com.libraryManagement.library_management;

public class BorrowControllerTest {
}*/

package com.libraryManagement.library_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.entity.Borrow;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BorrowService borrowService;

    private Borrow borrow;
    private Borrow borrow1;


    @BeforeEach
    public void setup() {
        borrow = new Borrow();
        borrow.setId(1L);
        borrow.setMemberId(2L);
        borrow.setBookId(2L);
        borrow.setBorrowedDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(14));

        borrow1 = new Borrow();
        borrow1.setId(2L);
        borrow1.setMemberId(2L);
        borrow1.setBookId(2L);
        borrow1.setBorrowedDate(LocalDate.now());
        borrow1.setDueDate(LocalDate.now().plusDays(14));
    }

    @Nested
    class CreateBorrowTests {

        @Test
        public void createBorrow_Success() throws Exception {
            mockMvc.perform(post("/api/borrows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(borrow)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId", is(borrow.getMemberId().intValue())))
                    .andExpect(jsonPath("$.bookId", is(borrow.getBookId().intValue())))
                    .andExpect(jsonPath("$.borrowedDate", is(borrow.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$.dueDate", is(borrow.getDueDate().toString())));

            mockMvc.perform(post("/api/borrows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(borrow1)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.memberId", is(borrow1.getMemberId().intValue())))
                    .andExpect(jsonPath("$.bookId", is(borrow1.getBookId().intValue())))
                    .andExpect(jsonPath("$.borrowedDate", is(borrow1.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$.dueDate", is(borrow1.getDueDate().toString())));
        }

        @Test
        public void createBorrow_BookNotAvailable() throws Exception {
            borrow.setBookId(4L);
            mockMvc.perform(post("/api/borrows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(borrow)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetBorrowByIdTests {

        @Test
        public void getBorrowById_Success() throws Exception {
            mockMvc.perform(get("/api/borrows/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(borrow.getId().intValue())))
                    .andExpect(jsonPath("$.memberId", is(borrow.getMemberId().intValue())))
                    .andExpect(jsonPath("$.bookId", is(borrow.getBookId().intValue())))
                    .andExpect(jsonPath("$.borrowedDate", is(borrow.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$.dueDate", is(borrow.getDueDate().toString())));
        }

        @Test
        public void getBorrowById_NotFound() throws Exception {
            mockMvc.perform(get("/api/borrows/{id}", 5L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetAllBorrowsTests {

        @Test
        public void getAllBorrows_Success() throws Exception {
            List<Borrow> borrows = Arrays.asList(borrow);
            mockMvc.perform(get("/api/borrows")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(borrow.getId().intValue())))
                    .andExpect(jsonPath("$[0].memberId", is(borrow.getMemberId().intValue())))
                    .andExpect(jsonPath("$[0].bookId", is(borrow.getBookId().intValue())))
                    .andExpect(jsonPath("$[0].borrowedDate", is(borrow.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$[0].dueDate", is(borrow.getDueDate().toString())));
        }
    }

    @Nested
    class UpdateBorrowTests {

        @Test
        public void updateBorrow_Success() throws Exception {
            Borrow updatedBorrow = new Borrow();
            updatedBorrow.setId(1L);
            updatedBorrow.setMemberId(6L);
            updatedBorrow.setBookId(6L);
            updatedBorrow.setBorrowedDate(LocalDate.now().minusDays(1));
            updatedBorrow.setDueDate(LocalDate.now().plusDays(13));
            mockMvc.perform(put("/api/borrows/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedBorrow)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(updatedBorrow.getId().intValue())))
                    .andExpect(jsonPath("$.memberId", is(updatedBorrow.getMemberId().intValue())))
                    .andExpect(jsonPath("$.bookId", is(updatedBorrow.getBookId().intValue())))
                    .andExpect(jsonPath("$.borrowedDate", is(updatedBorrow.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$.dueDate", is(updatedBorrow.getDueDate().toString())));
        }

        @Test
        public void updateBorrow_NotFound() throws Exception {
            mockMvc.perform(put("/api/borrows/{id}", 8L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(borrow)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book not found for borrow id : 8")));
        }
    }

    @Nested
    class DeleteBorrowTests {

        @Test
        public void deleteBorrow_Success() throws Exception {
            mockMvc.perform(delete("/api/borrows/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteBorrow_NotFound() throws Exception {
            mockMvc.perform(delete("/api/borrows/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book not found for borrow id : 1")));
        }
    }

    @Nested
    class ReturnBookTests {

        @Test
        public void returnBook_Success() throws Exception {
            mockMvc.perform(put("/api/borrows/return/{id}", 2L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(borrow.getId().intValue())))
                    .andExpect(jsonPath("$.memberId", is(borrow.getMemberId().intValue())))
                    .andExpect(jsonPath("$.bookId", is(borrow.getBookId().intValue())))
                    .andExpect(jsonPath("$.borrowedDate", is(borrow.getBorrowedDate().toString())))
                    .andExpect(jsonPath("$.dueDate", is(borrow.getDueDate().toString())));
        }

        @Test
        public void returnBook_NotFound() throws Exception {
            mockMvc.perform(put("/api/borrows/return/{id}", 9L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Book is not available for borrowing with id: 1")));
        }
    }
}

