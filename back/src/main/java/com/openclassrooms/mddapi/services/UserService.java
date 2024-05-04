    package com.openclassrooms.mddapi.services;

    import com.openclassrooms.mddapi.dto.UserDTO;
    import com.openclassrooms.mddapi.entity.User;
    import com.openclassrooms.mddapi.repository.UserRepository;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.dao.DataIntegrityViolationException;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;


    @Service
    @Slf4j
    @AllArgsConstructor
    public class UserService implements UserDetailsService {
        private final UserRepository userRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        public void register(User user) {
            try {
                if (!PasswordValidator.isValid(user.getPassword())) {
                    throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
                }
                String cryptedPassword = this.passwordEncoder.encode(user.getPassword());
                user.setPassword(cryptedPassword);
                this.userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                String errorMessage = e.getMessage();
                if (errorMessage.contains("users_email")) {
                    throw new IllegalArgumentException("Email already used");
                } else if (errorMessage.contains("users_username")) {
                    throw new IllegalArgumentException("Username already used");
                } else {
                    throw e;
                }
            }
        }

        @Override
        public User loadUserByUsername(String username) throws UsernameNotFoundException {
            return userRepository.findByEmailOrUsername(username, username)
                    .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur ne correspond à cet identifiant"));
        }

        public User findByEmail(String email) {
            return userRepository.findByEmail(email).orElse(null);
        }

        public User findByUsername(String username) {
            return userRepository.findByUsername(username).orElse(null);
        }

        public User updateUser(User currentUser, UserDTO userDTO) {
            User userToUpdate = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
            if (userDTO.getEmail() != null) {
                userToUpdate.setEmail(userDTO.getEmail());
            }
            if (userDTO.getUsername() != null) {
                userToUpdate.setUsername(userDTO.getUsername());
            }
            if (userDTO.getPassword() != null) {
                if (!PasswordValidator.isValid(userDTO.getPassword())) {
                    throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
                }
                userToUpdate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }


            return userRepository.save(userToUpdate);
        }

        public class PasswordValidator {

            public static boolean isValid(String password) {
                if (password.length() < 8) {
                    return false;
                }


                boolean hasUpperCase = false;
                boolean hasLowerCase = false;
                boolean hasDigit = false;
                boolean hasSpecialChar = false;

                for (char c : password.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        hasUpperCase = true;
                    } else if (Character.isLowerCase(c)) {
                        hasLowerCase = true;
                    } else if (Character.isDigit(c)) {
                        hasDigit = true;
                    } else {
                        hasSpecialChar = true;
                    }
                }

                return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
            }

            public static void main(String[] args) {
                String password = "Test123!";
                if (isValid(password)) {
                    System.out.println("Le mot de passe est valide.");
                } else {
                    System.out.println("Le mot de passe n'est pas valide.");
                }
            }
        }


    }
