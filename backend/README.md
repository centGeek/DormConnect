# Backend Configuration
Project run

Run the docker-compose.yml file and DormConnectApplication.java. All Spring dependencies should be correctly loaded.
Using the app in dev mode

Use the following URL: http://localhost:8091/

To log in, enter:

    login: user

    password: find it in the running logs of the main application. For example - `The generated security password is: 0dce0df9-baec-48cc-9c21-b4a0cbbef931`.
### PostgreSQL Configuration in `docker-compose.yml` file

Below is the PostgreSQL configuration that you can include in your `docker-compose.yml` file:

```yaml
services:
  postgres-dev:
    image: postgres:17
    container_name: postgres-db-dormConnect
    restart: always
    ports:
      - "5434:5432"
    environment:
      POSTGRES_USER: "admin"
      POSTGRES_PASSWORD: "admin"
      POSTGRES_DB: "dormConnect"
    volumes:
      - postgres_data_dev:/var/lib/postgresql/data

volumes:
  postgres_data_dev:
```

#### Configuration Explanation
- **`image`**: Specifies the Docker image for PostgreSQL version 17.
- **`container_name`**: Sets the container name to `postgres-db-dormConnect`.
- **`restart`**: The container will automatically restart if it stops.
- **`ports`**: Maps the external port (`5434`) to the PostgreSQL internal port (`5432`) within the container.
- **`environment`**:
    - `POSTGRES_USER`: Admin username for the database (`admin`).
    - `POSTGRES_PASSWORD`: Admin password for the database (`admin`).
    - `POSTGRES_DB`: Default database name (`dormConnect`).
- **`volumes`**: PostgreSQL data is stored in the `postgres_data_dev` volume, making it persistent across container restarts.

#### How to Use
1. Copy the configuration above into your `docker-compose.yml` file.
2. Run the following command to start the container:

   ```bash
   docker-compose up -d
   ```

#### Project Structure

Create a folder for your group’s module inside the `dormConnect` directory. Within this folder, organize your code according to functionality by creating the following package structure:
```
dormConnect/
    └── <dorm>/
        ├── controllers/      
        ├── repositories/     
        └── services/         
 └── <wastebin>/
        ├── controllers/      
        ├── repositories/     
        └── services/         
 └── <nfc>/
        ├── controllers/      
        ├── repositories/     
        └── services/         
```
### Backend used tools

1. **Flyway** will be used for managing database migrations in a version-controlled manner.
2. **Repository Interfaces**:
    - Repositories should implement the `CrudRepository<T, ID>` interface provided by Spring Data. This interface provides basic CRUD operations such as `save()`, `findById()`, `delete()`, etc. Importantly, **you do not need to implement these methods manually**. The implementation is provided by Spring Data based on the method names following the **naming convention**.

   For example, if you want to find a user by their username, the method could be named `findByUsername`, and Spring Data will automatically generate the implementation behind the scenes.

   Example repository interface:
   ```java
   import org.springframework.data.repository.CrudRepository;
   import com.example.model.User;

   public interface UserRepository extends CrudRepository<User, Long> {
       // No need to implement basic CRUD methods
       // The following method will be automatically implemented by Spring Data
       User findByUsername(String username);
   }
   ```

   In the above example, the method `findByUsername` follows Spring Data's naming convention, and Spring will automatically generate the implementation for this method at runtime.
3. **Transactional Methods**:
    - Methods in the service layer that modify the state of the database (e.g., creating, updating, or deleting records) should be annotated with `@Transactional`. This ensures that these operations are executed within a transaction, maintaining data integrity.

   Example service method:
   ```java
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;

   @Service
   public class UserService {

       private final UserRepository userRepository;

       public UserService(UserRepository userRepository) {
           this.userRepository = userRepository;
       }

       @Transactional
       public void createUser(User user) {
           userRepository.save(user);  // This operation will be executed within a transaction.
       }
   }
   ```

---
### Using Spring Annotations (`@Service`, `@RestController`, `@Componetnt`, `@Bean`, etc.)

**@Service**: The @Service annotation is used to mark a class as a service component, typically containing business logic. This class will be automatically registered as a Spring Bean and can be injected into other components, such as controllers or other services.

Example:

   ```java
@Service
public class UserService {
    // Business logic for user management
}
```

**@RestController**: The @RestController annotation is used to define a Spring REST controller. We will use it for exposing only APIs, not HTML views.

Example:

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Define API endpoints here
}

**@Bean:** The @Bean annotation is used in configuration classes to define Spring beans manually. It can be used to provide custom configurations that Spring might not automatically detect through component scanning.

Example:
```java
@Configuration
public class AppConfig {
    
    @Bean
    public SomeService someService() {
        return new SomeServiceImpl();
    }
}
```

**Dependency Injection**: Spring resolves and injects dependencies at runtime using constructor injection.

Example with constructor injection:

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    
    // Automatic dependency injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### Summary of Best Practices

- **Use annotations** like `@Service`, `@RestController`, `@Repository` for defining your Spring Beans, services, controllers, and repositories.
- **Dependency injection** should be handled with auto wiring, typically using constructor injection for better clarity and testability.
- **Transactional methods** should be annotated with `@Transactional` to ensure that database operations are properly managed within a transaction.
- Ensure that each module follows the **package structure** (`repositories`, `services`, `controllers`) inside its own module folder and follows the **Git workflow** for branching and pushing to the remote repository.

