# ── STEP 1: Use a container that has Maven + Java to BUILD your app ──
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first — Docker caches this layer
# so it only re-downloads libraries when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Now copy your source code and build it into a .war file
COPY src ./src
RUN mvn package -DskipTests -q


# ── STEP 2: Use a container that has Tomcat to RUN your app ──
# (this image is much smaller — no Maven, no source code)
FROM tomcat:10.1-jdk17

# Remove Tomcat's default example pages
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the .war file built in Step 1 into Tomcat
# Naming it ROOT.war means it runs at / (not /my-jsf-app/)
COPY --from=builder /app/target/Fassinas-Box.war \
     /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
