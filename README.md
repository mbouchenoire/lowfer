# Lowfer

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) &nbsp;
[![CI/CD](https://github.com/mbouchenoire/lowfer/actions/workflows/ci-cd.yml/badge.svg?branch=master)](https://github.com/mbouchenoire/lowfer/actions/workflows/ci-cd.yml)
[![Docker Hub](https://img.shields.io/docker/pulls/mbouchenoire/lowfer.svg)](https://hub.docker.com/r/mbouchenoire/lowfer) &nbsp;

Lowfer is a simple tool that helps software engineers and architects document, discuss & analyse software design and architecture.

You can use Lowfer locally in your browser at http://localhost:8080 using Docker only:

```shell script
docker run \
    -e ARCHITECTURES_REPOSITORY_URI=https://github.com/mbouchenoire/lowfer.git \
    -e ARCHITECTURES_REPOSITORY_BRANCH=master \
    -e ARCHITECTURES_REPOSITORY_PATH=src/test/resources/architectures/demo \
    -p 8080:8080 \
    mbouchenoire/lowfer:latest
```
