# LTL Check for Traces

## Description
Continuation of server script defined in [this](https://github.com/kaurjvpld/Incremental-Predictive-Monitoring-of-Business-Processes-with-A-priori-knowledge) project, used to determine the validity of a business process trace against a logic formula.

This project implements a prom plugin with the addition of a declare-parsing library able to use a declare model to check the compliance of a trace.

## Project structure

The ```src``` folder contains all the scripts used.

The ```formula_verificator``` folder contains all the used modules, while the ```StackEntryPoint.java``` script is tasked with starting the server and exposing its APIs.

## Getting started
This project is intended to be used in order to modify the functionalities of the [main](https://github.com/kaurjvpld/Incremental-Predictive-Monitoring-of-Business-Processes-with-A-priori-knowledge) project. Make sure to download it in order to test your changes.

### Prerequisites
Download the [main](https://github.com/kaurjvpld/Incremental-Predictive-Monitoring-of-Business-Processes-with-A-priori-knowledge) project

### Running the program
Start the ```StackEntryPoint.java``` script located in the ```src/main/formula_verificator``` folder.