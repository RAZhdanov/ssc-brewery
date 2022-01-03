package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerUrlTest() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/12345")
                                .param("apiKey", "spring")
                                .param("apiSecret", "guru")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerUrlBadCreds() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/12345")
                                .param("apiKey", "spring")
                                .param("apiSecret", "guruXXXXX")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beer/12345")
                                .header("Api-Key", "spring")
                                .header("Api-Secret", "guruXXXXX")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerTest() throws Exception {
        mockMvc.perform(
                delete("/api/v1/beer/12345")
                        .header("Api-Key", "spring")
                        .header("Api-Secret", "guru")
        )
                .andExpect(status().isOk());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df8c39-98c4-4ae8-b663-453e8e19c311")).andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/123456789")).andExpect(status().isOk());
    }
}

