FROM nginx:stable

# Copy output from npm run build into container
COPY dist /usr/share/nginx/html

# Copy configuration for nginx
COPY default.conf /etc/nginx/conf.d/
