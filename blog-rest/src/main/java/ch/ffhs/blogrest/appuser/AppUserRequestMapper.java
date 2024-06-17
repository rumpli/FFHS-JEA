package ch.ffhs.blogrest.appuser;

import ch.ffhs.blogrest.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AppUserRequestMapper implements Mapper<AppUserRequest, AppUser> {
    @Override
    public AppUser map(AppUserRequest source) {
        return new AppUser(source.id(), source.username());
    }
}
