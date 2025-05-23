To execute you must have a linux shell open in the project root.
You must install docker, docker-compose adn allow execution of docker as a non privileged
user.

Then be sure you have $UID and $GID set to your current user id and group id 
(check whith id -u and id -g). Be sure your docker deamon is running.

docker compose up devel
This to build artifacts

docker compose up debug-admin
This to run admin in debug mode, it's not deployable since the artifact is only mounted

docker compose up debug-local
This is to run local in debug mode, it's not deployable since the artifact is only mounted
