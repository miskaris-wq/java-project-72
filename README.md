_### Hexlet tests and linter status:
[![Actions Status](https://github.com/miskaris-wq/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/miskaris-wq/java-project-72/actions)


[![Maintainability](https://api.codeclimate.com/v1/badges/e8f7dae9a71a67aea0c5/maintainability)](https://codeclimate.com/github/miskaris-wq/java-project-71/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/e8f7dae9a71a67aea0c5/test_coverage)](https://codeclimate.com/github/miskaris-wq/java-project-71/test_coverage)

# java-project-72: Hexlet Project - Pages Analyzer

## Description

This repository contains a Java project completed as part of the Hexlet training program. This Java web app built with Javalin lets users add URLs, perform status checks by fetching and parsing page content, and view the history of these checks.

## Project Goal

The main goal of this project is to demonstrate Java programming skills through:

* Building a web application using Javalin.
* Managing URLs and storing data in a relational database.
* Performing HTTP requests and parsing HTML content.
* Implementing server-side templating with dynamic views.
* Handling user input, sessions, and error feedback.
* Organizing clean, modular, and maintainable code.

## Features

* Add URLs — Submit and normalize URLs for monitoring.
* List URLs — View all added URLs with their latest check results.
* URL Details — See detailed info and full check history for each URL.
* Perform URL Checks — Send HTTP GET requests, parse HTML content, and save status codes and metadata.
* User Feedback — Displays success and error messages using flash sessions.

## Technologies Used

* Java 11+
* Javalin — lightweight Java web framework
* JTE — template engine for rendering views
* Unirest — HTTP client for web requests
* Jsoup — HTML parser to extract data
* JDBC and SQL database for persistent storage
* SLF4J for logging

## How to Use

1.  Clone the repository:

    ```
    git clone https://github.com/miskaris-wq/java-project-72.git
    cd java-project-72

    ```

2.  Build the project (using Make):

    ```
    make build
    ```

3.  Run the project (via Make):

    ```
    make run
    ```
4. Visit http://localhost:7070/

5. Use the app:
    * Add URLs via the web form;
    * View the list of URLs;
    * Perform checks on URLs;
    * View details and history of checks.

## Requirements

*   Java 11 or higher.
*   Make installed (for building and running with `make build` and `make run` commands).
*   A relational database accessible via JDBC
*   Maven or Gradle for building (or you can run from IDE)

## Dependencies

The project has no external dependencies other than the standard Java library.

## Authors

*   miskaris-wq

## Acknowledgments

Thanks to Hexlet for the educational program and the provided platform for developing this project._

