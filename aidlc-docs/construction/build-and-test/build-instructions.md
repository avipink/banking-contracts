# Build Instructions — banking-contracts

## Prerequisites
- JDK 17+
- Gradle 8.x on PATH (for initial wrapper bootstrap only)

## Step 1: Bootstrap Gradle Wrapper (one-time)
```bash
cd banking-contracts/
gradle wrapper --gradle-version 8.5
```
> If Gradle is not on PATH, copy gradlew + gradlew.bat + gradle/wrapper/gradle-wrapper.jar from any existing Gradle project.

## Step 2: Build
```bash
./gradlew build
```
Expected: `BUILD SUCCESSFUL`

## Step 3: Publish to mavenLocal
```bash
./gradlew publishToMavenLocal
```
Expected artifact: `~/.m2/repository/com/digitalbank/banking-contracts/1.0.0/`

## Step 4: Verify artifact (optional)
```bash
# Linux/Mac
ls ~/.m2/repository/com/digitalbank/banking-contracts/1.0.0/

# Windows
dir %USERPROFILE%\.m2\repository\com\digitalbank\banking-contracts\1.0.0\
```
Expected files: `banking-contracts-1.0.0.jar`, `banking-contracts-1.0.0.pom`
