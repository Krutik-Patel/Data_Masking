# Use an official Node.js runtime as the base image
FROM node:18

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

# Set environment variable for backend communication
ENV REACT_APP_BACKEND_URL=http://backend:8080

# Expose the port the app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
