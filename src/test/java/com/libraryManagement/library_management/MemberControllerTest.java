/*package com.libraryManagement.library_management;

public class MemberControllerTest {
}*/

package com.libraryManagement.library_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.library_management.entity.Member;
import com.libraryManagement.library_management.exceptions.MemberNotAvailableException;
import com.libraryManagement.library_management.service.MemberService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    private Member member;
    private Member member1;


    @BeforeEach
    public void setup() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setPhone("1234567890");
        member.setRegisteredDate(LocalDate.parse("2023-01-01"));

        member1 = new Member();
        member1.setId(2L);
        member1.setName("John xyz");
        member1.setPhone("12345678905");
        member1.setRegisteredDate(LocalDate.parse("2023-01-05"));
    }



    @Nested
    class CreateMemberTests {

        @Test
        public void createMember_Success() throws Exception {
            mockMvc.perform(post("/api/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(member.getId().intValue())))
                    .andExpect(jsonPath("$.name", is(member.getName())))
                    .andExpect(jsonPath("$.phone", is(member.getPhone())))
                    .andExpect(jsonPath("$.registeredDate", is(member.getRegisteredDate())));

            mockMvc.perform(post("/api/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member1)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(member1.getId().intValue())))
                    .andExpect(jsonPath("$.name", is(member1.getName())))
                    .andExpect(jsonPath("$.phone", is(member1.getPhone())))
                    .andExpect(jsonPath("$.registeredDate", is(member1.getRegisteredDate())));
        }


       /* @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        public void createMember_InvalidName(String name) throws Exception {
            member.setName(name);
            mockMvc.perform(post("/api/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors", hasSize(1)));
        }*/
    }

    @Nested
    class GetMemberByIdTests {

        @Test
        public void getMemberById_Success() throws Exception {
            mockMvc.perform(get("/api/members/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(member.getId().intValue())))
                    .andExpect(jsonPath("$.name", is(member.getName())))
                    .andExpect(jsonPath("$.phone", is(member.getPhone())))
                    .andExpect(jsonPath("$.registeredDate", is(member.getRegisteredDate())));
        }

        @Test
        public void getMemberById_NotFound() throws Exception {
            mockMvc.perform(get("/api/members/{id}", 3L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Member with id not found: 3")));
        }
    }

    @Nested
    class GetAllMembersTests {

        @Test
        public void getAllMembers_Success() throws Exception {
            //List<Member> members = Arrays.asList(member);
            mockMvc.perform(get("/api/members")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(member.getId().intValue())))
                    .andExpect(jsonPath("$[0].name", is(member.getName())))
                    .andExpect(jsonPath("$[0].phone", is(member.getPhone())))
                    .andExpect(jsonPath("$[0].registeredDate", is(member.getRegisteredDate())));
        }
    }

    @Nested
    class UpdateMemberTests {

        @Test
        public void updateMember_Success() throws Exception {
            Member updatedMember = new Member();
            updatedMember.setId(1L);
            updatedMember.setName("Jane Doe1");
            updatedMember.setPhone("09876543211");
            updatedMember.setRegisteredDate(LocalDate.parse("2023-12-30"));

            mockMvc.perform(put("/api/members/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedMember)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(updatedMember.getId().intValue())))
                    .andExpect(jsonPath("$.name", is(updatedMember.getName())))
                    .andExpect(jsonPath("$.phone", is(updatedMember.getPhone())))
                    .andExpect(jsonPath("$.registeredDate", is(updatedMember.getRegisteredDate())));
        }

        @Test
        public void updateMember_NotFound() throws Exception {
            mockMvc.perform(put("/api/members/{id}", 4L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(member)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Member with id not found: 4")));
        }
    }

    @Nested
    class DeleteMemberTests {

        @Test
        public void deleteMember_Success() throws Exception {
            mockMvc.perform(delete("/api/members/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        public void deleteMember_NotFound() throws Exception {
            mockMvc.perform(delete("/api/members/{id}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Member with id not found: 1")));
        }
    }
}

