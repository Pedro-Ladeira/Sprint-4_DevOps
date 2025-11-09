# Troubleshooting

This document contains troubleshooting tips and common fixes related to building and running the project.

## Maven "No compiler is provided" error

If you get "No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?", install a JDK (version 17) and ensure `JAVA_HOME` is set and added to `PATH`.

On Windows (cmd.exe):

1. Install JDK 17 (Temurin, Adoptium, or Oracle).
2. Set Java home (replace the path with your JDK install path):

```cmd
setx JAVA_HOME "C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.8.7-hotspot"
setx PATH "%JAVA_HOME%\\bin;%PATH%"
```

Then restart your terminal or run:

```cmd
set JAVA_HOME="C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.8.7-hotspot"
set PATH=%JAVA_HOME%\\bin;%PATH%
```

Verify:

```cmd
java -version
javac -version
mvn -version
```

## Running the project locally (H2 profile)

Option A — Quick local dev using H2 (no Oracle required):

```cmd
cd Sprint-Java
mvn -f mottu-api clean package -DskipTests
# Run using the H2 profile
mvn -f mottu-api spring-boot:run -Dspring-boot.run.profiles=h2
# or
java -jar mottu-api\target\mottu-api-1.0.0.jar --spring.profiles.active=h2
```

## Oracle driver (local install)

If you have `mottu-api/lib/ojdbc11.jar`, install it into your local Maven repository (Windows cmd.exe):

```cmd
mvn install:install-file -Dfile=mottu-api\lib\ojdbc11.jar -DgroupId=com.oracle.database.jdbc -DartifactId=ojdbc11 -Dversion=23.3.0 -Dpackaging=jar
```

Then build with the `oracle` profile:

```cmd
mvn -f mottu-api clean package -DskipTests -Poracle
mvn -f mottu-api spring-boot:run -Poracle
```

## Docker / docker-compose notes

- Use the provided `docker-compose.yml` and `.env` to supply database credentials and profile.
- For local dev with H2, set `SPRING_PROFILES_ACTIVE=h2` in `.env` or override in the `docker-compose.yml` service configuration.

## Additional tips

- If IntelliJ opens a dialog to choose an application to open `mvn`, check for a stray file `C:\\Windows\\System32\\mvn` and remove/rename it (requires Administrator). After removal `where mvn` should point to your Maven installation path.
- If you change `JAVA_HOME`, restart your terminal/IDE so environment variables are refreshed.

If you hit a specific error, paste the relevant logs and I will help troubleshoot further.

