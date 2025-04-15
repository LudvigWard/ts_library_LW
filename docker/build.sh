#!/bin/sh

cd ../backend
mvn clean package
cd ..
cd frontend
npm install
npm run build
cd ../docker
docker-compose build
echo Done!
