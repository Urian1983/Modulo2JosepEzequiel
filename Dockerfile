FROM ubuntu:latest
LABEL authors="urian"

ENTRYPOINT ["top", "-b"]