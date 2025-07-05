## Install Guide

1. Clone git repository
2. Install Docker Desktop : "https://www.docker.com/products/docker-desktop/"
3. Run the command : "docker-compose -f docker-compose.dev.yml up --build"
4. Both of the projects will start inside of their respective docker containers

## Usage guide
- Go to "http://localhost:8080/swagger-ui/index.html#" to access the swagger UI to directly call the backend endpoints
- Go to "http://localhost:5173/" for the React UI

## Info about the project
- ImageMagick is installed on the same docker container as the backend, you can run commands manually if you go the Exec tab of the docker container if you want to test it out
- Live reload works on the Frontend even though it is inside a docker container, so you can just save your changes in your local file system and the React UI will automatically update, no need to rerun the docker-compose command
- Rerun the docker-compose command if you want to recompile the backend
