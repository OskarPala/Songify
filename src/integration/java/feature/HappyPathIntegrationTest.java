package feature;

import com.songify.SongifyApplication;
import com.songify.infrastructure.security.jwt.JwtAuthConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = SongifyApplication.class)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class HappyPathIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    public MockMvc mockMvc;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Test
    public void happy_path() throws Exception {
//    1. when I go to /songs without jwt then I can see no songs
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));
//      SECURITY STEP when I go to /songs with jwt token then I can see no songs
        mockMvc.perform(get("/songs")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", empty()));
//      SECURITY STEP when I post to /songs without jwt with Song "Till i collapse" then 401 Unauthorized is returned
        mockMvc.perform(post("/songs"))
                .andExpect(status().isUnauthorized());

//      SECURITY STEP when I post to /songs as role user with Song then 403 Forbidden is returned
        mockMvc.perform(post("/songs")
                        .with(authentication(createJwtWithUserRole())))
                .andExpect(status().isForbidden());

//    2. when I post to /songs with Song "Till i collapse" then Song "Till i collapse" is returned with id 1
        mockMvc.perform(post("/songs")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                 "name": "Till i collapse",
                                 "releaseDate": "2024-03-15T13:55:21.850Z",
                                 "duration": 0,
                                 "language": "ENGLISH"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.id", is(1)))
                .andExpect(jsonPath("$.song.name", is("Till i collapse")))
                .andExpect(jsonPath("$.song.genre.id", is(1)))
                .andExpect(jsonPath("$.song.genre.name", is("default")));


//    3. when I post to /songs with Song "Lose Yourself" then Song "Lose Yourself" is returned with id 2

        mockMvc.perform(post("/songs")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                 "name": "Lose Yourself",
                                 "releaseDate": "2024-03-15T13:55:21.850Z",
                                 "duration": 0,
                                 "language": "ENGLISH"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.id", is(2)))
                .andExpect(jsonPath("$.song.name", is("Lose Yourself")))
                .andExpect(jsonPath("$.song.genre.id", is(1)))
                .andExpect(jsonPath("$.song.genre.name", is("default")));


//    4. when I go to /genres then I can see only default genre with id 1
        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("default")));


//    5. when I post to /genres with Genre "Rap" then Genre "Rap" is returned with id 2
        mockMvc.perform(post("/genres")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                  "name": "Rap"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Rap")));


//    6. when I go to /songs/1 then I can see default genre with id 1 and name default
        mockMvc.perform(get("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.name", is("Till i collapse")))
                .andExpect(jsonPath("$.song.id", is(1)))
                .andExpect(jsonPath("$.song.genre.id", is(1)))
                .andExpect(jsonPath("$.song.genre.name", is("default")));

//    7. when I put to /songs/1/genre/1 then Genre with id 2 ("Rap") is added to Song with id 1 ("Till i collapse")
        mockMvc.perform(put("/songs/1/genres/2")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("updated")));

//    8. when I go to /songs/1 then I can see "Rap" genre
        mockMvc.perform(get("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.genre.id", is(2)))
                .andExpect(jsonPath("$.song.genre.name", is("Rap")));
//    9. when I go to /albums then I can see no albums
        mockMvc.perform(get("/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albums", empty()));


//    10. when I post to /albums with Album "EminemAlbum1" and Song with id 1 then Album "EminemAlbum1" is returned with id 1
        mockMvc.perform(post("/albums")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                "title": "EminemAlbum1",
                                "releaseDate": "2024-04-15T10:53:23.820Z",
                                "songIds": [1]
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("EminemAlbum1")))
                .andExpect(jsonPath("$.songsIds", containsInAnyOrder(1)));


//    11. when I go to /albums/1 then I can not see any albums because there is no artist in system
        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Album with id: 1 not found")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")));

//    12. when I post to /artists with Artist "Eminem" then Artist "Eminem" is returned with id 1
        mockMvc.perform(post("/artists")
                        .with(authentication(createJwtWithAdminRole()))
                        .content("""
                                {
                                "name": "Eminem"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Eminem")));

//    13. when I put to /artists/1/albums/1 then Artist with id 1 ("Eminem") is added to Album with id 1 ("EminemAlbum1")
        mockMvc.perform(put("/artists/1/albums/1")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("probably assigned artist to album")));


//    14. when I go to /albums/1 then I can see album with single song with id 1 and single artist with id 1
        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.artists[*].id", containsInAnyOrder(1)));


//    15. when I put to /albums/1/songs/2 then Song with id 2 ("Lose Yourself") is added to Album with id 1 ("EminemAlbum1")
        mockMvc.perform(put("/albums/1/songs/2")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("EminemAlbum1")))
                .andExpect(jsonPath("$.songsIds[*]", containsInAnyOrder(1, 2)));


//    16. when I go to /albums/1 then I can see album with 2 songs (id1 and id2)
        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.artists[*].id", containsInAnyOrder(1)));
    }

    private JwtAuthenticationToken createJwtWithAdminRole() {
        Jwt jwt = Jwt.withTokenValue("123")
                .claim("email", "oskar.pala92@gmail.com")
                .header("alg", "none")
                .build();
        return jwtAuthConverter.convert(jwt);
    }

    private JwtAuthenticationToken createJwtWithUserRole() {
        Jwt jwt = Jwt.withTokenValue("123")
                .claim("email", "john@gmail.com")
                .header("alg", "none")
                .build();
        return jwtAuthConverter.convert(jwt);
    }
}
