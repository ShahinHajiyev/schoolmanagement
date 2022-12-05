package shako.schoolmanagement.config.auth;

import java.util.Optional;

public interface AppUserDao {

     Optional<AppUserDetails> selectAppUserByUserName(String username);
}
