#!/bin/bash
echo "Building Celebrity Catalog Project..."

# Set project directory variables
SRC_DIR="src"
OUT_DIR="out/production/CelebrityCatalog"
MAIN_CLASS="Main"

# Create output directory if it doesn’t exist
mkdir -p $OUT_DIR

# Compile all Java files
javac -d $OUT_DIR $SRC_DIR/*.java $SRC_DIR/view/*.java $SRC_DIR/controller/*.java $SRC_DIR/model/*.java

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"

# main.java.Run the program
echo "Running the program..."
java -cp "$OUT_DIR" $MAIN_CLASS


