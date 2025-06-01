# ---- Build Stage ----
FROM node:20-alpine AS build
WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm ci

# Copy source and build the Angular app
COPY . .
RUN npm run build -- --configuration production

# ---- Nginx Stage ----
FROM nginx:alpine
COPY --from=build /app/dist/document-portal/* /usr/share/nginx/html

# Remove default config and add custom nginx config
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
