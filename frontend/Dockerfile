### STAGE 1: Build ###
FROM node:latest AS build
ENV TZ=Europe/Tallinn
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
FROM nginx:latest
ENV TZ=Europe/Tallinn
COPY default.conf /etc/nginx/conf.d/default.conf
COPY cert.pem /etc/nginx/certs/cert.pem
COPY key.pem /etc/nginx/certs/key.pem
COPY --from=build /usr/src/app/dist/frontend /usr/share/nginx/html
EXPOSE 80 443
