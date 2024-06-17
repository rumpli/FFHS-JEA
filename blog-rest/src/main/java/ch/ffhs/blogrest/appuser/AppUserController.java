package ch.ffhs.blogrest.appuser;

import ch.ffhs.blogrest.Mapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;

@RestController()
@RequestMapping(AppUserController.PATH)
public class AppUserController {
    public static final String PATH = "/users";

    private final AppUserService appUserService;
    private final Mapper<AppUserRequest, AppUser> fromDto;

    public AppUserController(AppUserService appUserService, Mapper<AppUserRequest, AppUser> fromDto) {
        this.appUserService = appUserService;
        this.fromDto = fromDto;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_AUTH_SERVER')")
    public ResponseEntity<Void> create(@RequestBody AppUserRequest appUserRequest) {
        try {
            AppUser appUser = fromDto.map(appUserRequest);
            AppUser createdUser = appUserService.create(appUser);
            return ResponseEntity.created(new URI("/users/" + createdUser.getId())).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Couldn't create user");
        }
    }
}
