# Lowfer

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) &nbsp;
[![Build Status](https://travis-ci.org/mbouchenoire/lowfer.svg?branch=0.1.0)](https://travis-ci.org/mbouchenoire/lowfer) &nbsp;
[![Docker Hub](https://img.shields.io/docker/pulls/mbouchenoire/lowfer.svg)](https://hub.docker.com/r/mbouchenoire/lowfer) &nbsp;

Lowfer is a simple tool that helps software engineers and architects document, discuss & analyse software design and architecture.

You can use Lowfer locally in your browser at http://localhost:8080 using Docker only:

```shell script
docker run \
    -e APPLICATION_ARCHITECTURES_REPOSITORY_URI=https://github.com/mbouchenoire/lowfer.git \
    -e APPLICATION_ARCHITECTURES_REPOSITORY_BRANCH=0.1.0 \
    -e APPLICATION_ARCHITECTURES_REPOSITORY_PATH=src/test/resources/architectures/demo \
    -p 8080:8080
    mbouchenoire/lowfer:latest
```
