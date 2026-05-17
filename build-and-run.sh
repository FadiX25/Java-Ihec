#!/bin/bash
# Build and Run Script for IHEC-JLearn Spring Boot Application

echo "======================================"
echo "IHEC-JLearn Spring Boot Build & Run"
echo "======================================"
echo ""

# Check Java version
echo "✓ Checking Java version..."
java -version

# Check Maven installation
echo ""
echo "✓ Checking Maven installation..."
mvn -version

# Clean build
echo ""
echo "✓ Cleaning previous build..."
mvn clean

# Install dependencies
echo ""
echo "✓ Installing dependencies..."
mvn install

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Build successful!"
    echo ""
    echo "======================================"
    echo "Starting Spring Boot Application..."
    echo "======================================"
    echo ""
    echo "🚀 Application will be available at:"
    echo "   http://localhost:8080"
    echo ""
    echo "📝 Default Credentials:"
    echo "   Create new account via registration"
    echo ""
    echo "Press Ctrl+C to stop the application"
    echo ""
    
    # Run the application
    mvn spring-boot:run
else
    echo ""
    echo "✗ Build failed! Please check the errors above."
    exit 1
fi
