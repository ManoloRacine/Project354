# Use a lightweight Node.js base image
FROM node:24-alpine AS builder
# Create the app directory
WORKDIR /app
# Copy package.json and yarn.lock first for efficient caching
COPY package*.json ./
# Install dependencies
RUN yarn
# Copy the rest of the project files
COPY . .
EXPOSE 5173
CMD ["yarn", "dev"]