FROM mysql:8.0.29

# SQL scripts put here will be run in alphabetical order
COPY setup.sql /docker-entrypoint-initdb.d

# Enable a health check to enable waiting until MySQL is running
HEALTHCHECK --interval=10s --timeout=5s --retries=5 CMD ["mysqladmin", "ping", "-h", "localhost"]
