#!/bin/bash

source .env

while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        --commit-message)
          COMMIT_MESSAGE="$2"
          shift
          shift
          ;;
        --version)
          VERSION="$2"
          shift
          shift
          ;;
        *)
          echo "Unknown option: $key"
          exit 1
          ;;
    esac
done

print_status() {
    echo "$(date +"%Y-%m-%d %T"): $1"
}

if [ -z "$COMMIT_MESSAGE" ]; then
    print_status "Commit message is required."
    exit 1
fi

if [ -z "$VERSION" ]; then
    print_status "version number is required."
    exit 1
fi

# Change directory to project directory
print_status "Changing directory to project directory ..."
cd "$PROJECT_DIRECTORY" || { print_status "Changing directory failed."; exit 1; }

# Running maven clean and package
print_status "Running maven clean package ..."
./mvnw clean package -DskipTests || { print_status "Running maven failed."; exit 1; }

# Building docker image
print_status "Building $DOCKER_IMAGE_NAME:$VERSION ..."
docker build -t "$DOCKER_IMAGE_NAME:$VERSION" . || { print_status "Docker build failed."; exit 1; }

# Pushing docker image
print_status "Pushing $DOCKER_IMAGE_NAME:$VERSION ..."
docker push "$DOCKER_IMAGE_NAME:$VERSION" || { print_status "Docker push failed."; exit 1; }

# Tagging docker image as latest
print_status "Tagging docker image as latest ..."
docker tag "$DOCKER_IMAGE_NAME:$VERSION" "$DOCKER_IMAGE_NAME:latest" || { print_status "Docker tag failed."; exit 1; }

# Pushing docker image (latest)
print_status "Pushing $DOCKER_IMAGE_NAME:latest ..."
docker push "$DOCKER_IMAGE_NAME:latest" || { print_status "Docker push failed."; exit 1; }

# Add changes
print_status "Adding git changes ..."
git add . || { print_status "Git add failed."; exit 1; }

print_status "Committing git changes with message: $COMMIT_MESSAGE"
git commit -m "$COMMIT_MESSAGE" || { print_status "Git commit failed."; exit 1; }

# Push changes
print_status "Pushing git changes..."
git push || { print_status "Git push failed."; exit 1; }

# SSH to remote server and run specific bash file
print_status "SSH to remote server and bash script ..."
ssh "$SSH_USERNAME@$SSH_HOST" "$SSH_COMMAND" || { print_status "SSH connection to remote server failed."; exit 1; }

# Saving last version in text file
print_status "Writing last version in versions file ..."
echo "$VERSION" >> versions.txt

print_status "Deployment completed successfully."
exit 0;