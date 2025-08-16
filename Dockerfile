# Use Maven with Java 17 as the base image
FROM maven:3.9.6-eclipse-temurin-17

# Install dependencies for Playwright and GUI (headed mode)
RUN apt-get update && \
    apt-get install -y wget unzip xvfb libnss3 libatk-bridge2.0-0 libgtk-3-0 libxss1 libasound2 libgbm-dev libxshmfence1 libxcomposite1 libxrandr2 libu2f-udev libatk1.0-0 libpangocairo-1.0-0 libpango-1.0-0 libcups2 libxdamage1 libxfixes3 libxext6 libx11-xcb1 && \
    rm -rf /var/lib/apt/lists/*

# Set environment variable for virtual display
ENV DISPLAY=:99

# Set working directory inside the container
WORKDIR /project

# Copy all files
COPY . .

# Pre-download Maven dependencies
RUN mvn dependency:go-offline

# Install Playwright Browsers
RUN mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Start Xvfb and run tests in GUI mode
CMD ["sh", "-c", "xvfb-run --server-num=99 mvn clean test -Dheadless=false"]
