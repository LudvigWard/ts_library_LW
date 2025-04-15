FROM eclipse-temurin:21

# Copy our things to the image
RUN mkdir -p /opt/library/migration
COPY entrypoint.sh /opt/library
COPY target/libraryapp-*.jar /opt/library
COPY src/test/resources/db/migration /opt/library/migration

# Make sure the script is runnable
RUN chmod +x /opt/library/entrypoint.sh

# Make it possible to access the web service from the outside
# if we need to debug anything
EXPOSE 8080/tcp

# Define that our script should start with the container
ENTRYPOINT ["/opt/library/entrypoint.sh"]
