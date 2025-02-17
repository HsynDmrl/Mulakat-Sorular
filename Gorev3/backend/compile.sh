#!/bin/bash

mkdir -p out
find src -name "*.java" -exec javac -d out {} \;
